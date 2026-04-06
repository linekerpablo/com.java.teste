package com.java.teste.presentation.controller;

import com.java.teste.application.usecase.*;
import com.java.teste.domain.exception.PersonAlreadyExistsException;
import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.AgeOutputFormat;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.model.SalaryOutputFormat;
import com.java.teste.presentation.dto.PersonPatchRequest;
import com.java.teste.presentation.dto.PersonRequest;
import com.java.teste.presentation.dto.PersonResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonControllerTest {

    @Mock private ListPersonsUseCase listPersonsUseCase;
    @Mock private CreatePersonUseCase createPersonUseCase;
    @Mock private DeletePersonUseCase deletePersonUseCase;
    @Mock private UpdatePersonUseCase updatePersonUseCase;
    @Mock private PatchPersonUseCase patchPersonUseCase;
    @Mock private FindPersonByIdUseCase findPersonByIdUseCase;
    @Mock private GetPersonAgeUseCase getPersonAgeUseCase;
    @Mock private GetPersonSalaryUseCase getPersonSalaryUseCase;

    @InjectMocks
    private PersonController controller;

    private Person buildPerson(Long id) {
        return new Person(id, "José da Silva", LocalDate.of(1990, 1, 15), LocalDate.of(2020, 3, 1));
    }

    @Test
    @DisplayName("listAll deve retornar 200 com lista de pessoas")
    void listAll_deveRetornar200() {
        when(listPersonsUseCase.execute()).thenReturn(List.of(buildPerson(1L), buildPerson(2L)));

        ResponseEntity<List<PersonResponse>> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody().get(0).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("listAll deve retornar lista vazia quando não há pessoas")
    void listAll_deveRetornarListaVazia() {
        when(listPersonsUseCase.execute()).thenReturn(List.of());

        ResponseEntity<List<PersonResponse>> response = controller.listAll();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    @DisplayName("findById deve retornar 200 com a pessoa")
    void findById_deveRetornar200() {
        when(findPersonByIdUseCase.execute(1L)).thenReturn(buildPerson(1L));

        ResponseEntity<PersonResponse> response = controller.findById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("findById deve propagar PersonNotFoundException")
    void findById_devePropagar404() {
        when(findPersonByIdUseCase.execute(99L)).thenThrow(new PersonNotFoundException(99L));

        assertThatThrownBy(() -> controller.findById(99L))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("getAge deve retornar 200 com idade calculada em anos")
    void getAge_deveRetornarIdadeEmAnos() {
        when(getPersonAgeUseCase.execute(1L, AgeOutputFormat.YEARS)).thenReturn(34L);

        ResponseEntity<Long> response = controller.getAge(1L, AgeOutputFormat.YEARS);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(34L);
    }

    @Test
    @DisplayName("getAge deve retornar 200 com idade em dias")
    void getAge_deveRetornarIdadeEmDias() {
        when(getPersonAgeUseCase.execute(1L, AgeOutputFormat.DAYS)).thenReturn(12410L);

        ResponseEntity<Long> response = controller.getAge(1L, AgeOutputFormat.DAYS);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(12410L);
    }

    @Test
    @DisplayName("getAge deve propagar PersonNotFoundException")
    void getAge_devePropagar404() {
        when(getPersonAgeUseCase.execute(eq(99L), any())).thenThrow(new PersonNotFoundException(99L));

        assertThatThrownBy(() -> controller.getAge(99L, AgeOutputFormat.YEARS))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("getSalary deve retornar 200 com salário completo")
    void getSalary_deveRetornarSalarioFull() {
        when(getPersonSalaryUseCase.execute(1L, SalaryOutputFormat.FULL)).thenReturn(new BigDecimal("3250.47"));

        ResponseEntity<BigDecimal> response = controller.getSalary(1L, SalaryOutputFormat.FULL);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualByComparingTo("3250.47");
    }

    @Test
    @DisplayName("getSalary deve retornar 200 com salário em salários mínimos")
    void getSalary_deveRetornarSalarioMin() {
        when(getPersonSalaryUseCase.execute(1L, SalaryOutputFormat.MIN)).thenReturn(new BigDecimal("2.49"));

        ResponseEntity<BigDecimal> response = controller.getSalary(1L, SalaryOutputFormat.MIN);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualByComparingTo("2.49");
    }

    @Test
    @DisplayName("create deve retornar 201 com pessoa criada")
    void create_deveRetornar201() {
        PersonRequest request = new PersonRequest(null, "José da Silva",
                LocalDate.of(1990, 1, 15), LocalDate.of(2020, 3, 1));
        when(createPersonUseCase.execute(request)).thenReturn(buildPerson(4L));

        ResponseEntity<PersonResponse> response = controller.create(request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(4L);
    }

    @Test
    @DisplayName("create deve propagar PersonAlreadyExistsException")
    void create_devePropagar409() {
        PersonRequest request = new PersonRequest(1L, "José da Silva",
                LocalDate.of(1990, 1, 15), LocalDate.of(2020, 3, 1));
        when(createPersonUseCase.execute(request)).thenThrow(new PersonAlreadyExistsException(1L));

        assertThatThrownBy(() -> controller.create(request))
                .isInstanceOf(PersonAlreadyExistsException.class);
    }

    @Test
    @DisplayName("delete deve retornar 204")
    void delete_deveRetornar204() {
        ResponseEntity<Void> response = controller.delete(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(deletePersonUseCase).execute(1L);
    }

    @Test
    @DisplayName("delete deve propagar PersonNotFoundException")
    void delete_devePropagar404() {
        doThrow(new PersonNotFoundException(99L)).when(deletePersonUseCase).execute(99L);

        assertThatThrownBy(() -> controller.delete(99L))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("update deve retornar 200 com pessoa atualizada")
    void update_deveRetornar200() {
        PersonRequest request = new PersonRequest(null, "José Novo",
                LocalDate.of(1990, 1, 15), LocalDate.of(2020, 3, 1));
        when(updatePersonUseCase.execute(1L, request)).thenReturn(buildPerson(1L));

        ResponseEntity<PersonResponse> response = controller.update(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("update deve propagar PersonNotFoundException")
    void update_devePropagar404() {
        PersonRequest request = new PersonRequest(null, "José",
                LocalDate.of(1990, 1, 15), LocalDate.of(2020, 3, 1));
        when(updatePersonUseCase.execute(eq(99L), any())).thenThrow(new PersonNotFoundException(99L));

        assertThatThrownBy(() -> controller.update(99L, request))
                .isInstanceOf(PersonNotFoundException.class);
    }

    @Test
    @DisplayName("patch deve retornar 200 com pessoa atualizada parcialmente")
    void patch_deveRetornar200() {
        PersonPatchRequest request = new PersonPatchRequest("Novo Nome", null, null);
        when(patchPersonUseCase.execute(1L, request)).thenReturn(buildPerson(1L));

        ResponseEntity<PersonResponse> response = controller.patch(1L, request);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    @DisplayName("patch deve propagar PersonNotFoundException")
    void patch_devePropagar404() {
        PersonPatchRequest request = new PersonPatchRequest("Nome", null, null);
        when(patchPersonUseCase.execute(eq(99L), any())).thenThrow(new PersonNotFoundException(99L));

        assertThatThrownBy(() -> controller.patch(99L, request))
                .isInstanceOf(PersonNotFoundException.class);
    }
}
