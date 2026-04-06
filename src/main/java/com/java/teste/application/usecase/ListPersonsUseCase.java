package com.java.teste.application.usecase;

import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ListPersonsUseCase {

    private final PersonRepository personRepository;

    public ListPersonsUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> execute() {
        return personRepository.findAll().stream()
                .sorted(Comparator.comparing(Person::getNome))
                .toList();
    }
}
