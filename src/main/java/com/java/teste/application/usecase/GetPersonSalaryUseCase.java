package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.model.SalaryOutputFormat;
import com.java.teste.domain.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
public class GetPersonSalaryUseCase {

    private static final BigDecimal SALARIO_INICIAL = new BigDecimal("1558.00");
    private static final BigDecimal PERCENTUAL_AUMENTO = new BigDecimal("1.18");
    private static final BigDecimal BONUS_ANUAL = new BigDecimal("500.00");
    private static final BigDecimal SALARIO_MINIMO = new BigDecimal("1302.00");

    private final PersonRepository personRepository;

    public GetPersonSalaryUseCase(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public BigDecimal execute(Long id, SalaryOutputFormat output) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));

        long anosNaEmpresa = ChronoUnit.YEARS.between(person.getDataAdmissao(), LocalDate.now());

        BigDecimal salario = calcularSalario(anosNaEmpresa);

        return switch (output) {
            case full -> salario.setScale(2, RoundingMode.CEILING);
            case min -> salario.divide(SALARIO_MINIMO, 2, RoundingMode.CEILING);
        };
    }

    private BigDecimal calcularSalario(long anosNaEmpresa) {
        BigDecimal salario = SALARIO_INICIAL;
        for (long ano = 0; ano < anosNaEmpresa; ano++) {
            salario = salario.multiply(PERCENTUAL_AUMENTO).add(BONUS_ANUAL);
        }
        return salario;
    }
}
