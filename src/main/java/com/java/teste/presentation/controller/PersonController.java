package com.java.teste.presentation.controller;

import com.java.teste.application.usecase.ListPersonsUseCase;
import com.java.teste.presentation.dto.PersonResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonController {

    private final ListPersonsUseCase listPersonsUseCase;

    public PersonController(ListPersonsUseCase listPersonsUseCase) {
        this.listPersonsUseCase = listPersonsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<PersonResponse>> listAll() {
        List<PersonResponse> response = listPersonsUseCase.execute().stream()
                .map(PersonResponse::from)
                .toList();

        return ResponseEntity.ok(response);
    }
}
