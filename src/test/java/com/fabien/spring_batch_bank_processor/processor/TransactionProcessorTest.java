package com.fabien.spring_batch_bank_processor.processor;

import com.fabien.spring_batch_bank_processor.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TransactionProcessorTest {

    private final TransactionProcessor transactionProcessor = new TransactionProcessor();

    @Test
    void shouldReturnNull_WithAmountNegative() throws Exception{
        Transaction transaction = new Transaction(1L,LocalDate.of(2025,12,31),"Jean",-15.0,"test");

        Transaction result = transactionProcessor.process(transaction);

        assertNull(result);
    }

    @Test
    void shouldReturnNull_WithNoAmount() throws Exception{
        Transaction expectedTransaction = new Transaction(1L,LocalDate.of(2025,12,31),"Jean",null,"test");

        Transaction result = transactionProcessor.process(expectedTransaction);

        assertNull(result);
    }

    @Test
    void shouldReturnTransaction_WithCompleteTransaction() throws Exception{
        Transaction expectedTransaction = new Transaction(1L,LocalDate.of(2025,12,31),"Jean",12.0,"test");

        Transaction result = transactionProcessor.process(expectedTransaction);

        assertEquals(result,expectedTransaction);
    }

    @Test
    void shouldSetCategoryToCourse_WhenAmountLessThanOrEqualTo100_AndNoCategory() throws Exception{
        Transaction expectedTransaction = new Transaction(1L,LocalDate.of(2025,12,31),"Jean",12.0,"");

        Transaction result = transactionProcessor.process(expectedTransaction);

        assert result != null;
        assertEquals("Courses",result.getCategory());
    }

    @Test
    void shouldSetCategoryToLoisirs_WhenAmountGreaterThanOrEqualTo100_AndNoCategory() throws Exception{
        Transaction expectedTransaction = new Transaction(1L,LocalDate.of(2025,12,31),"Jean",200.0,"");

        Transaction result = transactionProcessor.process(expectedTransaction);

        assert result != null;
        assertEquals("Loisirs",result.getCategory());
    }





}