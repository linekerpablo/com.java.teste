package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.AgeOutputFormat;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonAgeUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private GetPersonAgeUseCase getPersonAgeUseCase;

    @Test
    @DisplayName("Deve retornar idade em dias corretamente")
    void deveRetornarIdadeEmDias() {
        LocalDate dataNascimento = LocalDate.now().minusDays(100);
        Person person = new Person(1L, "José da Silva", dataNascimento, LocalDate.of(2020, 5, 10));

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        long resultado = getPersonAgeUseCase.execute(1L, AgeOutputFormat.days);

        assertThat(resultado).isEqualTo(100L);
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar idade em meses corretamente")
    void deveRetornarIdadeEmMeses() {
        LocalDate dataNascimento = LocalDate.now().minusMonths(36);
        Person person = new Person(1L, "José da Silva", dataNascimento, LocalDate.of(2020, 5, 10));

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        long resultado = getPersonAgeUseCase.execute(1L, AgeOutputFormat.months);

        assertThat(resultado).isEqualTo(36L);
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar idade em anos corretamente")
    void deveRetornarIdadeEmAnos() {
        LocalDate dataNascimento = LocalDate.now().minusYears(22);
        Person person = new Person(1L, "José da Silva", dataNascimento, LocalDate.of(2020, 5, 10));

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        long resultado = getPersonAgeUseCase.execute(1L, AgeOutputFormat.years);

        assertThat(resultado).isEqualTo(22L);
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getPersonAgeUseCase.execute(99L, AgeOutputFormat.years))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).findById(99L);
    }
}
