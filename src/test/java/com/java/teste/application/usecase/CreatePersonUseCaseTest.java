package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonAlreadyExistsException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreatePersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private CreatePersonUseCase createPersonUseCase;

    @Test
    @DisplayName("Deve criar pessoa com id informado com sucesso")
    void deveCriarPessoaComIdInformadoComSucesso() {
        PersonRequest request = new PersonRequest(10L, "Ana Paula", LocalDate.of(1995, 3, 10), LocalDate.of(2022, 1, 15));
        Person personSalva = new Person(10L, "Ana Paula", LocalDate.of(1995, 3, 10), LocalDate.of(2022, 1, 15));

        when(personRepository.existsById(10L)).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenReturn(personSalva);

        Person resultado = createPersonUseCase.execute(request);

        assertThat(resultado.getId()).isEqualTo(10L);
        assertThat(resultado.getNome()).isEqualTo("Ana Paula");
        assertThat(resultado.getDataNascimento()).isEqualTo(LocalDate.of(1995, 3, 10));
        assertThat(resultado.getDataAdmissao()).isEqualTo(LocalDate.of(2022, 1, 15));

        verify(personRepository).existsById(10L);
        verify(personRepository).save(any(Person.class));
        verify(personRepository, never()).nextId();
    }

    @Test
    @DisplayName("Deve criar pessoa sem id informado e usar auto-incremento")
    void deveCriarPessoaSemIdEUsarAutoIncremento() {
        PersonRequest request = new PersonRequest(null, "Bruno Lima", LocalDate.of(1990, 6, 20), LocalDate.of(2019, 11, 1));
        Person personSalva = new Person(4L, "Bruno Lima", LocalDate.of(1990, 6, 20), LocalDate.of(2019, 11, 1));

        when(personRepository.nextId()).thenReturn(4L);
        when(personRepository.existsById(4L)).thenReturn(false);
        when(personRepository.save(any(Person.class))).thenReturn(personSalva);

        Person resultado = createPersonUseCase.execute(request);

        assertThat(resultado.getId()).isEqualTo(4L);
        assertThat(resultado.getNome()).isEqualTo("Bruno Lima");

        verify(personRepository).nextId();
        verify(personRepository).existsById(4L);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    @DisplayName("Deve lançar PersonAlreadyExistsException quando id já existe")
    void deveLancarExcecaoQuandoIdJaExiste() {
        PersonRequest request = new PersonRequest(1L, "Carlos Silva", LocalDate.of(1985, 8, 5), LocalDate.of(2015, 4, 20));

        when(personRepository.existsById(1L)).thenReturn(true);

        assertThatThrownBy(() -> createPersonUseCase.execute(request))
                .isInstanceOf(PersonAlreadyExistsException.class)
                .hasMessageContaining("1");

        verify(personRepository).existsById(1L);
        verify(personRepository, never()).save(any(Person.class));
        verify(personRepository, never()).nextId();
    }

    @Test
    @DisplayName("Deve lançar PersonAlreadyExistsException quando auto-incremento gera id já existente")
    void deveLancarExcecaoQuandoAutoIncrementoGeraIdJaExistente() {
        PersonRequest request = new PersonRequest(null, "Diego Costa", LocalDate.of(2000, 1, 1), LocalDate.of(2023, 5, 10));

        when(personRepository.nextId()).thenReturn(2L);
        when(personRepository.existsById(2L)).thenReturn(true);

        assertThatThrownBy(() -> createPersonUseCase.execute(request))
                .isInstanceOf(PersonAlreadyExistsException.class)
                .hasMessageContaining("2");

        verify(personRepository).nextId();
        verify(personRepository).existsById(2L);
        verify(personRepository, never()).save(any(Person.class));
    }
}
