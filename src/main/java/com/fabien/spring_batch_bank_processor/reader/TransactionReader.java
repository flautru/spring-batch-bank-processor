package com.fabien.spring_batch_bank_processor.reader;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.reader.converter.StringToLocalDateConverter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.separator.DefaultRecordSeparatorPolicy;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class TransactionReader {

    private Resource resource;

    public TransactionReader() {
        this.resource = new ClassPathResource("transactions.csv");
    }

    // Constructeur utilisé en test pour injecter une autre ressource
    public TransactionReader(Resource resource) {
        this.resource = resource;
    }

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
                .resource(resource)
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
}
