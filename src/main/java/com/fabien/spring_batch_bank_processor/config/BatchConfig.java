package com.fabien.spring_batch_bank_processor.config;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.processor.TransactionProcessor;
import com.fabien.spring_batch_bank_processor.reader.converter.StringToLocalDateConverter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;  // Injecté automatiquement par Spring

    @Bean
    public FlatFileItemReader<Transaction> transactionFlatFileItemReader() {

        // Création du BeanWrapperFieldSetMapper avec ConversionService
        BeanWrapperFieldSetMapper<Transaction> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Transaction.class);

        // Ajout d'un ConversionService avec prise en charge de LocalDate
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToLocalDateConverter());
        fieldSetMapper.setConversionService(conversionService);

        return new FlatFileItemReaderBuilder<Transaction>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("transactions.csv"))
                .linesToSkip(1)
                .lineMapper(new DefaultLineMapper<Transaction>() {{
                    setLineTokenizer(new DelimitedLineTokenizer() {{
                        setNames("id", "date", "client", "amount", "category");
                        setDelimiter(",");
                    }});
                    setFieldSetMapper(fieldSetMapper);
                }})
                .recordSeparatorPolicy(new DefaultRecordSeparatorPolicy())
                .build();
    }

    @Bean
    public TransactionProcessor transactionProcessor() {
        return new TransactionProcessor();
    }

    @Bean
    public JpaItemWriter<Transaction> transactionJpaItemWriter(){
        JpaItemWriter<Transaction> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

    @Bean
    public Step transactionStep() {
        return new StepBuilder("transactionStep", jobRepository)
                .<Transaction,Transaction>chunk(10,transactionManager)
                .reader(transactionFlatFileItemReader())
                .processor(transactionProcessor())
                .writer(transactionJpaItemWriter())
                .build();
    }

    @Bean
    public Job transactionJob( Step transactionStep){
        return new JobBuilder("transactionJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transactionStep)
                .build();
    }

}
