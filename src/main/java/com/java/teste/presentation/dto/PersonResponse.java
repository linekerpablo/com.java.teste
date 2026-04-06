package com.java.teste.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.java.teste.domain.model.Person;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados da pessoa retornados pela API")
public class PersonResponse {

    @Schema(description = "ID único da pessoa", example = "1")
    private Long id;

    @Schema(description = "Nome completo da pessoa", example = "José da Silva")
    private String nome;

    @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "10/05/1985")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Schema(description = "Data de admissão na empresa no formato dd/MM/yyyy", example = "15/03/2010")
    @JsonFormat(pattern = "dd/MM/yyyy")
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
