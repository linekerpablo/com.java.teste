package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeletePersonUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private DeletePersonUseCase deletePersonUseCase;

    @Test
    @DisplayName("Deve deletar pessoa com sucesso quando id existe")
    void deveDeletarPessoaComSucesso() {
        when(personRepository.existsById(1L)).thenReturn(true);

        assertThatCode(() -> deletePersonUseCase.execute(1L))
                .doesNotThrowAnyException();

        verify(personRepository).existsById(1L);
        verify(personRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(personRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> deletePersonUseCase.execute(99L))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).existsById(99L);
        verify(personRepository, never()).deleteById(99L);
    }
}
