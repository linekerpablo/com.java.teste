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
}
