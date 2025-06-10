package com.fabien.spring_batch_bank_processor.writer;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TransactionWriter {

    private final EntityManagerFactory entityManagerFactory;

    public JpaItemWriter<Transaction> transactionJpaItemWriter(){
        JpaItemWriter<Transaction> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
}
