package com.java.teste.presentation.controller;

import com.java.teste.application.usecase.CreatePersonUseCase;
import com.java.teste.application.usecase.DeletePersonUseCase;
import com.java.teste.application.usecase.FindPersonByIdUseCase;
import com.java.teste.application.usecase.GetPersonAgeUseCase;
import com.java.teste.application.usecase.GetPersonSalaryUseCase;
import com.java.teste.application.usecase.ListPersonsUseCase;
import com.java.teste.application.usecase.PatchPersonUseCase;
import com.java.teste.application.usecase.UpdatePersonUseCase;
import com.java.teste.domain.model.AgeOutputFormat;
import com.java.teste.domain.model.SalaryOutputFormat;
import com.java.teste.presentation.dto.ErrorResponse;
import com.java.teste.presentation.dto.PersonPatchRequest;
import com.java.teste.presentation.dto.PersonRequest;
import com.java.teste.presentation.dto.PersonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "Pessoas", description = "Endpoints para gerenciamento de pessoas")
@RestController
@RequestMapping("/person")
public class PersonController {

    private final ListPersonsUseCase listPersonsUseCase;
    private final CreatePersonUseCase createPersonUseCase;
    private final DeletePersonUseCase deletePersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;
    private final PatchPersonUseCase patchPersonUseCase;
    private final FindPersonByIdUseCase findPersonByIdUseCase;
    private final GetPersonAgeUseCase getPersonAgeUseCase;
    private final GetPersonSalaryUseCase getPersonSalaryUseCase;

    public PersonController(ListPersonsUseCase listPersonsUseCase,
                            CreatePersonUseCase createPersonUseCase,
                            DeletePersonUseCase deletePersonUseCase,
                            UpdatePersonUseCase updatePersonUseCase,
                            PatchPersonUseCase patchPersonUseCase,
                            FindPersonByIdUseCase findPersonByIdUseCase,
                            GetPersonAgeUseCase getPersonAgeUseCase,
                            GetPersonSalaryUseCase getPersonSalaryUseCase) {
        this.listPersonsUseCase = listPersonsUseCase;
        this.createPersonUseCase = createPersonUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
        this.patchPersonUseCase = patchPersonUseCase;
        this.findPersonByIdUseCase = findPersonByIdUseCase;
        this.getPersonAgeUseCase = getPersonAgeUseCase;
        this.getPersonSalaryUseCase = getPersonSalaryUseCase;
    }

    @Operation(
            summary = "Listar todas as pessoas",
            description = "Retorna a lista de todas as pessoas cadastradas, ordenadas alfabeticamente pelo nome."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonResponse.class))))
    })
    @GetMapping
    public ResponseEntity<List<PersonResponse>> listAll() {
        List<PersonResponse> response = listPersonsUseCase.execute().stream()
                .map(PersonResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Buscar pessoa por ID",
            description = "Retorna os dados de uma pessoa específica com base no ID informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id) {
        PersonResponse response = PersonResponse.from(findPersonByIdUseCase.execute(id));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Calcular idade da pessoa",
            description = "Calcula a idade da pessoa na unidade especificada: `days` (dias), `months` (meses) ou `years` (anos)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Idade calculada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Long.class, example = "34"))),
            @ApiResponse(responseCode = "400", description = "Unidade de saída inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/age")
    public ResponseEntity<Long> getAge(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Unidade de saída: `days` (dias), `months` (meses) ou `years` (anos)",
                    example = "years", required = true,
                    schema = @Schema(allowableValues = {"days", "months", "years"}))
            @RequestParam AgeOutputFormat output) {
        return ResponseEntity.ok(getPersonAgeUseCase.execute(id, output));
    }

    @Operation(
            summary = "Calcular salário da pessoa",
            description = """
                    Calcula o salário atual da pessoa com base no tempo de empresa.
                    
                    Formatos de saída:
                    - `full`: retorna o salário completo em reais (R$)
                    - `min`: retorna o salário expresso em quantidade de salários mínimos (R$ 1.302,00)
                    
                    O salário inicial é de R$ 1.558,00 e aumenta 18% ao ano, acrescido de R$ 500,00 de bônus por ano.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Salário calculado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BigDecimal.class, example = "3250.47"))),
            @ApiResponse(responseCode = "400", description = "Formato de saída inválido",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{id}/salary")
    public ResponseEntity<BigDecimal> getSalary(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id,
            @Parameter(description = "Formato de saída: `full` (valor em reais) ou `min` (em salários mínimos)",
                    example = "full", required = true,
                    schema = @Schema(allowableValues = {"min", "full"}))
            @RequestParam SalaryOutputFormat output) {
        return ResponseEntity.ok(getPersonSalaryUseCase.execute(id, output));
    }

    @Operation(
            summary = "Cadastrar nova pessoa",
            description = """
                    Cadastra uma nova pessoa no sistema.
                    
                    - Se o campo `id` for omitido ou nulo, o ID será gerado automaticamente.
                    - Se um `id` for informado e já existir, a requisição será rejeitada com `409 Conflict`.
                    """
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pessoa cadastrada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponse.class))),
            @ApiResponse(responseCode = "409", description = "Já existe uma pessoa cadastrada com o ID informado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PersonResponse> create(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Dados da pessoa a ser cadastrada",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PersonRequest.class)))
            @RequestBody PersonRequest request) {
        PersonResponse response = PersonResponse.from(createPersonUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Remover pessoa",
            description = "Remove do sistema a pessoa com o ID especificado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Pessoa removida com sucesso",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id) {
        deletePersonUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Atualizar pessoa completamente (PUT)",
            description = "Substitui todos os dados de uma pessoa existente pelos valores informados no corpo da requisição."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Novos dados completos da pessoa",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PersonRequest.class)))
            @RequestBody PersonRequest request) {
        PersonResponse response = PersonResponse.from(updatePersonUseCase.execute(id, request));
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Atualizar pessoa parcialmente (PATCH)",
            description = "Atualiza apenas os campos informados da pessoa. Campos ausentes ou nulos no corpo da requisição são ignorados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PersonResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pessoa não encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> patch(
            @Parameter(description = "ID da pessoa", example = "1", required = true)
            @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Campos a serem atualizados. Apenas os campos informados serão alterados.",
                    required = true,
                    content = @Content(schema = @Schema(implementation = PersonPatchRequest.class)))
            @RequestBody PersonPatchRequest request) {
        PersonResponse response = PersonResponse.from(patchPersonUseCase.execute(id, request));
        return ResponseEntity.ok(response);
    }
}

