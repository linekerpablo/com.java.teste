package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;

@Service
public class FindPersonByIdUseCase {

    private final PersonRepository personRepository;

    public FindPersonByIdUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }
}
