package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonAlreadyExistsException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.presentation.dto.PersonRequest;
import org.springframework.stereotype.Service;

@Service
public class CreatePersonUseCase {

    private final PersonRepository personRepository;

    public CreatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(PersonRequest request) {
        Long id = resolveId(request.getId());

        if (personRepository.existsById(id)) {
            throw new PersonAlreadyExistsException(id);
        }

        Person person = new Person(id, request.getNome(), request.getDataNascimento(), request.getDataAdmissao());
        return personRepository.save(person);
    }

    private Long resolveId(Long requestedId) {
        if (requestedId == null) {
            return personRepository.nextId();
        }
        return requestedId;
    }
}
