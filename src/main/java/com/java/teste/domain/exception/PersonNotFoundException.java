package com.java.teste.domain.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(Long id) {
        super("Pessoa não encontrada com o id: " + id);
    }
}
