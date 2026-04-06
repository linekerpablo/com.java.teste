package com.java.teste.infrastructure.config;

import com.java.teste.domain.model.SalaryOutputFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SalaryOutputFormatConverter implements Converter<String, SalaryOutputFormat> {

    @Override
    public SalaryOutputFormat convert(String source) {
        return SalaryOutputFormat.fromValue(source);
    }
}