package com.java.teste.presentation.controller;

import com.java.teste.application.usecase.CreatePersonUseCase;
import com.java.teste.application.usecase.ListPersonsUseCase;
import com.java.teste.presentation.dto.PersonRequest;
import com.java.teste.presentation.dto.PersonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final ListPersonsUseCase listPersonsUseCase;
    private final CreatePersonUseCase createPersonUseCase;

    public PersonController(ListPersonsUseCase listPersonsUseCase, CreatePersonUseCase createPersonUseCase) {
        this.listPersonsUseCase = listPersonsUseCase;
        this.createPersonUseCase = createPersonUseCase;
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
}
