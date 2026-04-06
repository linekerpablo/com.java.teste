package com.java.teste.domain.model;

import java.util.Arrays;

public enum AgeOutputFormat {
    DAYS,
    MONTHS,
    YEARS;

    public static AgeOutputFormat fromValue(String value) {
        return Arrays.stream(values())
                .filter(format -> format.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato de idade inválido: " + value));
    }
}
