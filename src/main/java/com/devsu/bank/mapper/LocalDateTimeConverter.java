package com.devsu.bank.mapper;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ReadingConverter
public class LocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {

    @Override
    public LocalDateTime convert(LocalDate source) {
        return source.atStartOfDay();
    }

}

