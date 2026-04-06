package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.presentation.dto.PersonRequest;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdatePersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private UpdatePersonUseCase updatePersonUseCase;

    @Test
    @DisplayName("Deve buscar a pessoa existente e atualizar todos os campos do body")
    void deveAtualizarPessoaComSucesso() {
        Long id = 1L;
        Person personExistente = new Person(id, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));
        PersonRequest request = new PersonRequest(null, "José Atualizado", LocalDate.of(2000, 4, 6), LocalDate.of(2021, 6, 1));
        Person personAtualizada = new Person(id, "José Atualizado", LocalDate.of(2000, 4, 6), LocalDate.of(2021, 6, 1));

        when(personRepository.findById(id)).thenReturn(Optional.of(personExistente));
        when(personRepository.update(any(Person.class))).thenReturn(personAtualizada);

        Person resultado = updatePersonUseCase.execute(id, request);

        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNome()).isEqualTo("José Atualizado");
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2021, 6, 1));

        verify(personRepository).findById(id);
        verify(personRepository).update(any(Person.class));
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        Long id = 99L;
        PersonRequest request = new PersonRequest(null, "Qualquer Nome", LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1));

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> updatePersonUseCase.execute(id, request))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).findById(id);
        verify(personRepository, never()).update(any(Person.class));
    }
}
