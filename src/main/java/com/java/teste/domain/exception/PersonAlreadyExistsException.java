package com.java.teste.domain.exception;

public class PersonAlreadyExistsException extends RuntimeException {

    public PersonAlreadyExistsException(Long id) {
        super("Já existe uma pessoa cadastrada com o id: " + id);
    }
}
