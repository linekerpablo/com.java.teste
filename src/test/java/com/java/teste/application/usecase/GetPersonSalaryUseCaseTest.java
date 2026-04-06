package com.java.teste.application.usecase;

import com.java.teste.domain.exception.PersonNotFoundException;
import com.java.teste.domain.model.Person;
import com.java.teste.domain.model.SalaryOutputFormat;
import com.java.teste.domain.repository.PersonRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetPersonSalaryUseCaseTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private GetPersonSalaryUseCase getPersonSalaryUseCase;

    @Test
    @DisplayName("Deve retornar salário full em R$ com 2 anos de empresa arredondado para cima")
    void deveRetornarSalarioFullComDoisAnosDeEmpresa() {
        // Admissão: 10/05/2020, data referência do teste: 07/02/2023 → 2 anos completos
        // Ano 0: 1558.00
        // Ano 1: 1558.00 * 1.18 + 500 = 2338.44
        // Ano 2: 2338.44 * 1.18 + 500 = 3259.3592 → ceil 2 decimais = 3259.36
        LocalDate dataAdmissao = LocalDate.now().minusYears(2).minusMonths(8);
        Person person = new Person(1L, "José da Silva", LocalDate.of(2000, 4, 6), dataAdmissao);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        BigDecimal resultado = getPersonSalaryUseCase.execute(1L, SalaryOutputFormat.full);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("3259.36"));
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar salário min em salários mínimos com 2 anos de empresa arredondado para cima")
    void deveRetornarSalarioMinComDoisAnosDeEmpresa() {
        // 3259.3592 / 1302.00 = 2.5033... → ceil 2 decimais = 2.51
        LocalDate dataAdmissao = LocalDate.now().minusYears(2).minusMonths(8);
        Person person = new Person(1L, "José da Silva", LocalDate.of(2000, 4, 6), dataAdmissao);

        when(personRepository.findById(1L)).thenReturn(Optional.of(person));

        BigDecimal resultado = getPersonSalaryUseCase.execute(1L, SalaryOutputFormat.min);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("2.51"));
        verify(personRepository).findById(1L);
    }

    @Test
    @DisplayName("Deve retornar salário inicial quando admitido há menos de 1 ano")
    void deveRetornarSalarioInicialSemAnosCompletos() {
        LocalDate dataAdmissao = LocalDate.now().minusMonths(6);
        Person person = new Person(2L, "Maria Souza", LocalDate.of(1990, 8, 15), dataAdmissao);

        when(personRepository.findById(2L)).thenReturn(Optional.of(person));

        BigDecimal resultado = getPersonSalaryUseCase.execute(2L, SalaryOutputFormat.full);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("1558.00"));
        verify(personRepository).findById(2L);
    }

    @Test
    @DisplayName("Deve retornar salário full correto com 1 ano de empresa")
    void deveRetornarSalarioFullComUmAnoDeEmpresa() {
        // Ano 1: 1558.00 * 1.18 + 500 = 2338.44
        LocalDate dataAdmissao = LocalDate.now().minusYears(1).minusMonths(2);
        Person person = new Person(3L, "Carlos Oliveira", LocalDate.of(1985, 12, 20), dataAdmissao);

        when(personRepository.findById(3L)).thenReturn(Optional.of(person));

        BigDecimal resultado = getPersonSalaryUseCase.execute(3L, SalaryOutputFormat.full);

        assertThat(resultado).isEqualByComparingTo(new BigDecimal("2338.44"));
        verify(personRepository).findById(3L);
    }

    @Test
    @DisplayName("Deve lançar PersonNotFoundException quando id não existe")
    void deveLancarExcecaoQuandoIdNaoExiste() {
        when(personRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> getPersonSalaryUseCase.execute(99L, SalaryOutputFormat.full))
                .isInstanceOf(PersonNotFoundException.class)
                .hasMessageContaining("99");

        verify(personRepository).findById(99L);
    }
}
