package com.fabien.spring_batch_bank_processor.config;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.processor.TransactionProcessor;
import com.fabien.spring_batch_bank_processor.reader.TransactionReader;
import com.fabien.spring_batch_bank_processor.writer.TransactionWriter;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@RequiredArgsConstructor
@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EntityManagerFactory entityManagerFactory;  // Injecté automatiquement par Spring
    private final TransactionReader transactionReader;
    private final TransactionWriter transactionWriter;

    @Bean
    public TransactionProcessor transactionProcessor() {
        return new TransactionProcessor();
    }

    @Bean
    public Step transactionStep() {
        return new StepBuilder("transactionStep", jobRepository)
                .<Transaction,Transaction>chunk(10,transactionManager)
                .reader(transactionReader.transactionFlatFileItemReader())
                .processor(transactionProcessor())
                .writer(transactionWriter.transactionJpaItemWriter())
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
