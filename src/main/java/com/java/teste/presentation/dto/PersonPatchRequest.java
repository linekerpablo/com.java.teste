package com.java.teste.presentation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
