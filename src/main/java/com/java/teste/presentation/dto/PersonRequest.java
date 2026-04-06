package com.java.teste.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

@Schema(description = "Dados para criação ou atualização completa de uma pessoa")
public class PersonRequest {

    @Schema(description = "ID da pessoa. Se omitido, será gerado automaticamente. Se informado e já existir, retorna 409.",
            example = "4", nullable = true)
    private Long id;

    @Schema(description = "Nome completo da pessoa", example = "João da Silva")
    private String nome;

    @Schema(description = "Data de nascimento no formato dd/MM/yyyy", example = "15/03/1990")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    @Schema(description = "Data de admissão na empresa no formato dd/MM/yyyy", example = "01/01/2020")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAdmissao;

    public PersonRequest(Long id, String nome, LocalDate dataNascimento, LocalDate dataAdmissao) {
        this.id = id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.dataAdmissao = dataAdmissao;
    }

    public PersonRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
