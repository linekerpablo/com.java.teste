package com.java.teste.presentation.dto;

import com.java.teste.domain.model.Person;

import java.time.LocalDate;

public class PersonResponse {

    private Long id;
    private String nome;
    private LocalDate dataNascimento;
    private LocalDate dataAdmissao;

    private PersonResponse(Long id, String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public static PersonResponse from(Person person) {
        return new PersonResponse(
                person.getId(),
                person.getNome(),
                person.getDataNascimento(),
                person.getDataAdmissao()
        );
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }
}
