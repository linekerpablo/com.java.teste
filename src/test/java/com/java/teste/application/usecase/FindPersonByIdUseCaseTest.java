package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
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
class FindPersonByIdUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private FindPersonByIdUseCase findPersonByIdUseCase;

    @Test
    @DisplayName("Deve retornar a pessoa quando id existe")
    void deveRetornarPessoaQuandoIdExiste() {
        Long id = 1L;
        Person person = new Person(id, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));

        when(personRepository.findById(id)).thenReturn(Optional.of(person));

        Person resultado = findPersonByIdUseCase.execute(id);

        assertThat(resultado.getId()).isEqualTo(id);
        assertThat(resultado.getNome()).isEqualTo("José da Silva");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(2000, 4, 6));
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2020, 5, 10));

        verify(personRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        Long id = 99L;

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> findPersonByIdUseCase.execute(id))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).findById(id);
    }
}
