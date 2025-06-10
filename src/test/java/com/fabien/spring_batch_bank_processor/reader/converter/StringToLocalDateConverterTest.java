package com.fabien.spring_batch_bank_processor.reader.converter;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;


class StringToLocalDateConverterTest {
    private final StringToLocalDateConverter converter = new StringToLocalDateConverter();

    @Test
    void shouldConverValidDate(){
        String dateString = "2025-12-31";

        LocalDate result = converter.convert(dateString);

        assertEquals(LocalDate.of(2025,12,31),result);
    }

    @Test
    void shouldReturnNullWhenSourceIsEmpty(){
        LocalDate result = converter.convert("");

        assertNull(result);
    }

    @Test
    void shouldThrowExceptionForInvalidFormat(){
        String invalidDate= "31-12-2025";

        assertThrows(Exception.class, () -> converter.convert(invalidDate));
    }

}