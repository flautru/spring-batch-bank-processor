package com.fabien.spring_batch_bank_processor.config;

import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class BatchConfigTest {

    @Autowired
    private Job transactionJob;

    @Autowired
    private Step transactionStep;

    @Test
    void contextLoads() {
        assertNotNull(transactionJob);
        assertNotNull(transactionStep);
    }
}