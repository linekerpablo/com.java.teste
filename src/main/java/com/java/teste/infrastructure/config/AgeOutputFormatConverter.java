package com.java.teste.infrastructure.config;

import com.java.teste.domain.model.AgeOutputFormat;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AgeOutputFormatConverter implements Converter<String, AgeOutputFormat> {

    @Override
    public AgeOutputFormat convert(String source) {
        return AgeOutputFormat.fromValue(source);
    }
}