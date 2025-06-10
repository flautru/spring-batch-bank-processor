package com.fabien.spring_batch_bank_processor.reader.converter;

import org.springframework.core.convert.converter.Converter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate convert(String source) {
        if (source == null || source.isEmpty()) {
            return null;
        }
        return LocalDate.parse(source, formatter);
    }
}