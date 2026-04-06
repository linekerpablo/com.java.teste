package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import com.java.teste.presentation.dto.PersonPatchRequest;
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
class PatchPersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PatchPersonUseCase patchPersonUseCase;

    @Test
    @DisplayName("Deve atualizar apenas o nome quando somente nome é informado")
    void deveAtualizarApenasONome() {
        Long id = 1L;
        Person personExistente = new Person(id, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));
        PersonPatchRequest request = new PersonPatchRequest("José Atualizado", null, null);

        when(personRepository.findById(id)).thenReturn(Optional.of(personExistente));
        when(personRepository.update(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        Person resultado = patchPersonUseCase.execute(id, request);

        assertThat(resultado.getNome()).isEqualTo("José Atualizado");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(2000, 4, 6));
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2020, 5, 10));

        verify(personRepository).findById(id);
        verify(personRepository).update(any(Person.class));
    }

    @Test
    @DisplayName("Deve atualizar apenas a data de admissão quando somente ela é informada")
    void deveAtualizarApenasDataAdmissao() {
        Long id = 1L;
        Person personExistente = new Person(id, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));
        PersonPatchRequest request = new PersonPatchRequest(null, null, LocalDate.of(2023, 1, 1));

        when(personRepository.findById(id)).thenReturn(Optional.of(personExistente));
        when(personRepository.update(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        Person resultado = patchPersonUseCase.execute(id, request);

        assertThat(resultado.getNome()).isEqualTo("José da Silva");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(2000, 4, 6));
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2023, 1, 1));

        verify(personRepository).findById(id);
        verify(personRepository).update(any(Person.class));
    }

    @Test
    @DisplayName("Deve atualizar todos os campos quando todos são informados")
    void deveAtualizarTodosOsCampos() {
        Long id = 1L;
        Person personExistente = new Person(id, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));
        PersonPatchRequest request = new PersonPatchRequest("José Novo", LocalDate.of(1999, 3, 3), LocalDate.of(2022, 2, 2));

        when(personRepository.findById(id)).thenReturn(Optional.of(personExistente));
        when(personRepository.update(any(Person.class))).thenAnswer(inv -> inv.getArgument(0));

        Person resultado = patchPersonUseCase.execute(id, request);

        assertThat(resultado.getNome()).isEqualTo("José Novo");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(1999, 3, 3));
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2022, 2, 2));

        verify(personRepository).findById(id);
        verify(personRepository).update(any(Person.class));
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        Long id = 99L;
        PersonPatchRequest request = new PersonPatchRequest("Qualquer", null, null);

        when(personRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> patchPersonUseCase.execute(id, request))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).findById(id);
        verify(personRepository, never()).update(any(Person.class));
    }
}
