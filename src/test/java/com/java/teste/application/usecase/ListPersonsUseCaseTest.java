package com.java.teste.application.usecase;

import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListPersonsUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private ListPersonsUseCase listPersonsUseCase;

    @Test
    @DisplayName("Deve retornar lista de pessoas ordenada alfabeticamente por nome")
    void deveRetornarListaOrdenadaPorNome() {
        List<Person> pessoasDesordenadas = List.of(
                new Person(3L, "Carlos Oliveira", LocalDate.of(1985, 12, 20), LocalDate.of(2015, 7, 22)),
                new Person(1L, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10)),
                new Person(2L, "Ana Souza", LocalDate.of(1990, 8, 15), LocalDate.of(2018, 3, 1))
        );

        when(personRepository.findAll()).thenReturn(pessoasDesordenadas);

        List<Person> resultado = listPersonsUseCase.execute();

        assertThat(resultado).hasSize(3);
        assertThat(resultado.get(0).getNome()).isEqualTo("Ana Souza");
        assertThat(resultado.get(1).getNome()).isEqualTo("Carlos Oliveira");
        assertThat(resultado.get(2).getNome()).isEqualTo("José da Silva");

        verify(personRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há pessoas cadastradas")
    void deveRetornarListaVaziaQuandoNaoHaPessoas() {
        when(personRepository.findAll()).thenReturn(Collections.emptyList());

        List<Person> resultado = listPersonsUseCase.execute();

        assertThat(resultado).isEmpty();

        verify(personRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista com uma única pessoa")
    void deveRetornarListaComUmaUnicaPessoa() {
        List<Person> pessoas = List.of(
                new Person(1L, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10))
        );

        when(personRepository.findAll()).thenReturn(pessoas);

        List<Person> resultado = listPersonsUseCase.execute();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("José da Silva");

        verify(personRepository).findAll();
    }

    @Test
    @DisplayName("Deve manter ordem alfabética para nomes que começam com a mesma letra")
    void deveOrdenarCorretamenteNomesComMesmaLetraInicial() {
        List<Person> pessoas = List.of(
                new Person(2L, "Carlos Zanatta", LocalDate.of(1990, 1, 1), LocalDate.of(2019, 1, 1)),
                new Person(1L, "Carlos Almeida", LocalDate.of(1988, 5, 5), LocalDate.of(2017, 6, 10))
        );

        when(personRepository.findAll()).thenReturn(pessoas);

        List<Person> resultado = listPersonsUseCase.execute();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Carlos Almeida");
        assertThat(resultado.get(1).getNome()).isEqualTo("Carlos Zanatta");

        verify(personRepository).findAll();
    }
}
