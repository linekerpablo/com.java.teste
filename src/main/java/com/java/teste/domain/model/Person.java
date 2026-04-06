package com.java.teste.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Person {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;
}
