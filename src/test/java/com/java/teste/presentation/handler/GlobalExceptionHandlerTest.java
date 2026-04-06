package com.java.teste.presentation.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.java.teste.domain.exception.PersonAlreadyExistsException;
import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.presentation.dto.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Test
    @DisplayName("handlePersonNotFound deve retornar 404 com mensagem da exceção")
    void handlePersonNotFound() {
        PersonNotFoundException ex = new PersonNotFoundException(99L);

        ResponseEntity<ErrorResponse> response = handler.handlePersonNotFound(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(404);
        assertThat(response.getBody().getError()).isEqualTo("Not Found");
        assertThat(response.getBody().getMessage()).isEqualTo(ex.getMessage());
        assertThat(response.getBody().getTimestamp()).isNotNull();
    }

    @Test
    @DisplayName("handlePersonAlreadyExists deve retornar 409 com mensagem da exceção")
    void handlePersonAlreadyExists() {
        PersonAlreadyExistsException ex = new PersonAlreadyExistsException(1L);

        ResponseEntity<ErrorResponse> response = handler.handlePersonAlreadyExists(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(409);
        assertThat(response.getBody().getError()).isEqualTo("Conflict");
        assertThat(response.getBody().getMessage()).isEqualTo(ex.getMessage());
    }

    @Test
    @DisplayName("handleHttpMessageNotReadable com data inválida deve retornar 400 com campo e valor")
    void handleHttpMessageNotReadable_comDataInvalida() {
        InvalidFormatException invalidFormat = mock(InvalidFormatException.class);
        when(invalidFormat.getTargetType()).thenAnswer(inv -> LocalDate.class);
        when(invalidFormat.getValue()).thenReturn("13/0/1990");

        com.fasterxml.jackson.databind.JsonMappingException.Reference ref =
                new com.fasterxml.jackson.databind.JsonMappingException.Reference(null, "dataNascimento");
        when(invalidFormat.getPath()).thenReturn(List.of(ref));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", invalidFormat, null);

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadable(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("dataNascimento");
        assertThat(response.getBody().getMessage()).contains("13/0/1990");
        assertThat(response.getBody().getMessage()).contains("dd/MM/yyyy");
    }

    @Test
    @DisplayName("handleHttpMessageNotReadable sem InvalidFormatException deve retornar mensagem genérica")
    void handleHttpMessageNotReadable_semCausaEspecifica() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", (Throwable) null, null);

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadable(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Corpo da requisição inválido ou malformado.");
    }

    @Test
    @DisplayName("handleHttpMessageNotReadable com InvalidFormatException sem campo deve usar 'data'")
    void handleHttpMessageNotReadable_semCampoNoPath() {
        InvalidFormatException invalidFormat = mock(InvalidFormatException.class);
        when(invalidFormat.getTargetType()).thenAnswer(inv -> LocalDate.class);
        when(invalidFormat.getValue()).thenReturn("abc");
        when(invalidFormat.getPath()).thenReturn(List.of());

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("bad", invalidFormat, null);

        ResponseEntity<ErrorResponse> response = handler.handleHttpMessageNotReadable(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().getMessage()).contains("'data'");
    }

    @Test
    @DisplayName("handleMethodArgumentTypeMismatch deve retornar 400 com nome e valor do parâmetro")
    void handleMethodArgumentTypeMismatch() {
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("output");
        when(ex.getValue()).thenReturn("invalido");

        ResponseEntity<ErrorResponse> response = handler.handleMethodArgumentTypeMismatch(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatus()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).contains("output");
        assertThat(response.getBody().getMessage()).contains("invalido");
    }
}
