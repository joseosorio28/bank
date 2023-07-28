package com.devsu.bank.mapper;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ReadingConverter
public class LocalDateToDateConverter implements Converter<LocalDate, LocalDateTime> {

    @Override
    public LocalDateTime convert(@NotNull LocalDate source) {
        return source.atStartOfDay();
    }
}

