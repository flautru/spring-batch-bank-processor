package com.fabien.spring_batch_bank_processor.reader;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.reader.converter.StringToLocalDateConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransactionReaderTest {

    @Test
    void testReadTransaction() throws Exception {

        // Ligne CSV simulée en champs
        String[] tokens = {"1", "2024-06-01", "Fabien", "80.50", "Food"};

        // Les noms des colonnes (doivent correspondre à Transaction)
        String[] names = {"id", "date", "client", "amount", "category"};

        // Création d’un FieldSet simulé
        DefaultFieldSet fieldSet = new DefaultFieldSet(tokens, names);

        // Mapper avec ConversionService qui gère LocalDate
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new StringToLocalDateConverter());
                BeanWrapperFieldSetMapper<Transaction> mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(Transaction.class);
        mapper.setConversionService(conversionService);

        // Mappe le FieldSet en Transaction
        Transaction transaction = mapper.mapFieldSet(fieldSet);

        assertEquals(1, transaction.getId());
        assertEquals(LocalDate.of(2024, 6, 1), transaction.getDate());
        assertEquals("Fabien", transaction.getClient());
        assertEquals(80.50, transaction.getAmount());
        assertEquals("Food", transaction.getCategory());
    }


}
