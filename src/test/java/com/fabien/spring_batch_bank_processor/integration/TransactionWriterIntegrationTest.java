package com.fabien.spring_batch_bank_processor.integration;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import com.fabien.spring_batch_bank_processor.writer.TransactionWriter;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class TransactionWriterIntegrationTest {

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private TransactionWriter transactionWriter;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testWriteTransaction() throws Exception {

        Transaction transaction = new Transaction();
        transaction.setId(100L);
        transaction.setDate(LocalDate.of(2025, 6, 10));
        transaction.setClient("Jean Dupont");
        transaction.setAmount(150.0);
        transaction.setCategory("Food");

        JpaItemWriter<Transaction> writer = transactionWriter.transactionJpaItemWriter();

        Chunk<Transaction> chunk = new Chunk<>(Collections.singletonList(transaction));
        writer.write(chunk);

        entityManager.flush();
        entityManager.clear();

        Transaction found = entityManager.find(Transaction.class, 100L);
        assertThat(found).isNotNull();
        assertThat(found.getClient()).isEqualTo("Jean Dupont");
        assertThat(found.getAmount()).isEqualTo(150.0);
    }
}
