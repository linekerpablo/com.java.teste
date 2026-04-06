package com.java.teste.infrastructure.persistence;

import com.java.teste.domain.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class PersonInMemoryRepositoryTest {

    private PersonInMemoryRepository repository;

    @BeforeEach
    void setUp() {
        repository = new PersonInMemoryRepository();
    }

    @Test
    @DisplayName("findAll deve retornar as 3 pessoas inicializadas")
    void findAll_deveRetornar3Pessoas() {
        List<Person> pessoas = repository.findAll();
        assertThat(pessoas).hasSize(3);
    }

    @Test
    @DisplayName("findById deve retornar pessoa existente")
    void findById_deveRetornarPessoaExistente() {
        Optional<Person> resultado = repository.findById(1L);
        assertThat(resultado).isPresent();
        assertThat(resultado.get().getNome()).isEqualTo("José da Silva");
    }

    @Test
    @DisplayName("findById deve retornar empty para ID inexistente")
    void findById_deveRetornarEmptyParaIdInexistente() {
        Optional<Person> resultado = repository.findById(99L);
        assertThat(resultado).isEmpty();
    }

    @Test
    @DisplayName("existsById deve retornar true para ID existente")
    void existsById_deveRetornarTrue() {
        assertThat(repository.existsById(1L)).isTrue();
    }

    @Test
    @DisplayName("existsById deve retornar false para ID inexistente")
    void existsById_deveRetornarFalse() {
        assertThat(repository.existsById(99L)).isFalse();
    }

    @Test
    @DisplayName("save deve persisitir nova pessoa e retorná-la")
    void save_devePersistirPessoa() {
        Person nova = new Person(4L, "Ana Lima", LocalDate.of(1995, 6, 10), LocalDate.of(2023, 1, 1));
        Person salva = repository.save(nova);

        assertThat(salva).isEqualTo(nova);
        assertThat(repository.existsById(4L)).isTrue();
    }

    @Test
    @DisplayName("update deve atualizar pessoa existente")
    void update_deveAtualizarPessoa() {
        Person atualizada = new Person(1L, "José Atualizado", LocalDate.of(1990, 1, 1), LocalDate.of(2020, 1, 1));
        Person resultado = repository.update(atualizada);

        assertThat(resultado.getNome()).isEqualTo("José Atualizado");
        assertThat(repository.findById(1L).get().getNome()).isEqualTo("José Atualizado");
    }

    @Test
    @DisplayName("deleteById deve remover a pessoa")
    void deleteById_deveRemoverPessoa() {
        repository.deleteById(1L);
        assertThat(repository.existsById(1L)).isFalse();
        assertThat(repository.findAll()).hasSize(2);
    }

    @Test
    @DisplayName("nextId deve retornar 4 com as 3 pessoas iniciais")
    void nextId_deveRetornarProximoId() {
        Long nextId = repository.nextId();
        assertThat(nextId).isEqualTo(4L);
    }

    @Test
    @DisplayName("nextId deve retornar 1 quando repositório estiver vazio")
    void nextId_deveRetornar1QuandoVazio() {
        repository.deleteById(1L);
        repository.deleteById(2L);
        repository.deleteById(3L);

        Long nextId = repository.nextId();
        assertThat(nextId).isEqualTo(1L);
    }
}
