package com.java.teste.infrastructure.persistence;

import com.java.teste.domain.model.Person;
import com.java.teste.domain.repository.PersonRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class PersonInMemoryRepository implements PersonRepository {

    private final Map<Long, Person> storage;

    public PersonInMemoryRepository() {
        this.storage = new HashMap<>();
        initializeData();
    }

    private void initializeData() {
        Person jose = new Person(1L, "José da Silva", LocalDate.of(2000, 4, 6), LocalDate.of(2020, 5, 10));
        Person maria = new Person(2L, "Maria Souza", LocalDate.of(1990, 8, 15), LocalDate.of(2018, 3, 1));
        Person carlos = new Person(3L, "Carlos Oliveira", LocalDate.of(1985, 12, 20), LocalDate.of(2015, 7, 22));

        storage.put(jose.getId(), jose);
        storage.put(maria.getId(), maria);
        storage.put(carlos.getId(), carlos);
    }

    @Override
    public List<Person> findAll() {
        return List.copyOf(storage.values());
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public boolean existsById(Long id) {
        return storage.containsKey(id);
    }

    @Override
    public Person save(Person person) {
        storage.put(person.getId(), person);
        return person;
    }

    @Override
    public Person update(Person person) {
        storage.put(person.getId(), person);
        return person;
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }

    @Override
    public Long nextId() {
        return storage.keySet().stream()
                .max(Long::compareTo)
                .map(maxId -> maxId + 1)
                .orElse(1L);
    }
}
