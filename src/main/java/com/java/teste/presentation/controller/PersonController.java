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
import com.java.teste.presentation.dto.PersonPatchRequest;
import com.java.teste.presentation.dto.PersonRequest;
import com.java.teste.presentation.dto.PersonResponse;
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

    @GetMapping
    public ResponseEntity<List<PersonResponse>> listAll() {
        List<PersonResponse> response = listPersonsUseCase.execute().stream()
                .map(PersonResponse::from)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PersonResponse> findById(@PathVariable Long id) {
        PersonResponse response = PersonResponse.from(findPersonByIdUseCase.execute(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/age")
    public ResponseEntity<Long> getAge(@PathVariable Long id, @RequestParam AgeOutputFormat output) {
        return ResponseEntity.ok(getPersonAgeUseCase.execute(id, output));
    }

    @GetMapping("/{id}/salary")
    public ResponseEntity<BigDecimal> getSalary(@PathVariable Long id, @RequestParam SalaryOutputFormat output) {
        return ResponseEntity.ok(getPersonSalaryUseCase.execute(id, output));
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

    @PatchMapping("/{id}")
    public ResponseEntity<PersonResponse> patch(@PathVariable Long id, @RequestBody PersonPatchRequest request) {
        PersonResponse response = PersonResponse.from(patchPersonUseCase.execute(id, request));
        return ResponseEntity.ok(response);
    }
}
