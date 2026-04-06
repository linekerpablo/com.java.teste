package com.java.teste.application.usecase;

import com.java.teste.domain.model.AgeOutputFormat;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.domain.exception.PersonNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class GetPersonAgeUseCase {

    private final PersonRepository personRepository;

    public GetPersonAgeUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public long execute(Long id, AgeOutputFormat output) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        LocalDate dataNascimento = person.getDataNascimento();
        LocalDate hoje = LocalDate.now();

        return switch (output) {
            case days -> ChronoUnit.DAYS.between(dataNascimento, hoje);
            case months -> ChronoUnit.MONTHS.between(dataNascimento, hoje);
            case years -> ChronoUnit.YEARS.between(dataNascimento, hoje);
        };
    }
}
