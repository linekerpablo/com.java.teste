package com.java.teste.domain.repository;

import com.java.teste.domain.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    List<Person> findAll();

    Optional<Person> findById(Long id);

    boolean existsById(Long id);

    Person save(Person person);

    Person update(Person person);

    void deleteById(Long id);

    Long nextId();
}
