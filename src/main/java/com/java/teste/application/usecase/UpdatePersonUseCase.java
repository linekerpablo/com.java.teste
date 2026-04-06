package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.presentation.dto.PersonRequest;
import org.springframework.stereotype.Service;

@Service
public class UpdatePersonUseCase {

    private final PersonRepository personRepository;

    public UpdatePersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(Long id, PersonRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        person.setNome(request.getNome());
        person.setDataNascimento(request.getDataNascimento());
        person.setDataAdmissao(request.getDataAdmissao());

        return personRepository.update(person);
    }
}
