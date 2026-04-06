package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.presentation.dto.PersonPatchRequest;
import org.springframework.stereotype.Service;

@Service
public class PatchPersonUseCase {

    private final PersonRepository personRepository;

    public PatchPersonUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Person execute(Long id, PersonPatchRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        if (request.getNome() != null) {
            person.setNome(request.getNome());
        }
        if (request.getDataNascimento() != null) {
            person.setDataNascimento(request.getDataNascimento());
        }
        if (request.getDataAdmissao() != null) {
            person.setDataAdmissao(request.getDataAdmissao());
        }

        return personRepository.update(person);
    }
}
