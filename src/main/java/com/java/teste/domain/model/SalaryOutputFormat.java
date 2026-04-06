package com.java.teste.domain.model;

import java.util.Arrays;

public enum SalaryOutputFormat {
    MIN,
    FULL;

    public static SalaryOutputFormat fromValue(String value) {
        return Arrays.stream(values())
                .filter(format -> format.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Formato de salário inválido: " + value));
    }
}
