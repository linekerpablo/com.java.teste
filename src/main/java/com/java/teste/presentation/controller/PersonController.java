package com.java.teste.presentation.controller;

import com.java.teste.application.usecase.CreatePersonUseCase;
import com.java.teste.application.usecase.DeletePersonUseCase;
import com.java.teste.application.usecase.ListPersonsUseCase;
import com.java.teste.application.usecase.UpdatePersonUseCase;
import com.java.teste.presentation.dto.PersonRequest;
import com.java.teste.presentation.dto.PersonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final ListPersonsUseCase listPersonsUseCase;
    private final CreatePersonUseCase createPersonUseCase;
    private final DeletePersonUseCase deletePersonUseCase;
    private final UpdatePersonUseCase updatePersonUseCase;

    public PersonController(ListPersonsUseCase listPersonsUseCase,
                            CreatePersonUseCase createPersonUseCase,
                            DeletePersonUseCase deletePersonUseCase,
                            UpdatePersonUseCase updatePersonUseCase) {
        this.listPersonsUseCase = listPersonsUseCase;
        this.createPersonUseCase = createPersonUseCase;
        this.deletePersonUseCase = deletePersonUseCase;
        this.updatePersonUseCase = updatePersonUseCase;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> listAll() {
        List<PersonResponse> response = listPersonsUseCase.execute().stream()
                .map(PersonResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<PersonResponse> create(@RequestBody PersonRequest request) {
        PersonResponse response = PersonResponse.from(createPersonUseCase.execute(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deletePersonUseCase.execute(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PersonResponse> update(@PathVariable Long id, @RequestBody PersonRequest request) {
        PersonResponse response = PersonResponse.from(updatePersonUseCase.execute(id, request));
        return ResponseEntity.ok(response);
    }
}
