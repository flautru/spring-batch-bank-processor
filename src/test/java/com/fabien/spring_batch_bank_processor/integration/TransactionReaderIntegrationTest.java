package com.fabien.spring_batch_bank_processor.integration;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.reader.TransactionReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Import(TransactionReader.class)
public class TransactionReaderIntegrationTest {

    private TransactionReader transactionReader;

    @BeforeEach
    void setup() {
        Resource testResource = new ClassPathResource("transactions-test.csv");
        transactionReader = new TransactionReader(testResource);
    }

    @Test
    void testReadTransactions() throws Exception{
        FlatFileItemReader<Transaction> reader = transactionReader.transactionFlatFileItemReader();
        reader.open(new ExecutionContext());


        Transaction transaction = reader.read();
        assertNotNull(transaction);
        assertEquals("Jean Dupont", transaction.getClient());

        transaction = reader.read();
        assertNotNull(transaction);
        assertEquals("Marie Curie", transaction.getClient());

        transaction = reader.read();
        assertNull(transaction); // fin du fichier

        reader.close();
    }
}
