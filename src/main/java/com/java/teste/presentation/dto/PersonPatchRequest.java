package com.java.teste.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para atualização parcial de uma pessoa. Apenas os campos informados serão atualizados.")
public class PersonPatchRequest {

    @Schema(description = "Nome completo da pessoa", example = "João da Silva Atualizado", nullable = true)
    private String nome;

    @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "15/03/1990", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Schema(description = "Data de admissão na empresa no formato dd/MM/yyyy", example = "01/01/2020", nullable = true)
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAdmissao;

    public PersonPatchRequest(String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public PersonPatchRequest() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public LocalDate getDataAdmissao() {
        return dataAdmissao;
    }

    public void setDataAdmissao(LocalDate dataAdmissao) {
        this.dataAdmissao = dataAdmissao;
    }
}
