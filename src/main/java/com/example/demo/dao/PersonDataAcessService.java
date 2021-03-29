package com.example.demo.dao;

import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAcessService implements PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAcessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        String sql = "INSERT INTO person (id, name) VALUES (?, ?)";
        return jdbcTemplate.update(sql, new Object[]{id,person.getName()});
    }

    @Override
    public List<Person> selectAllPeople() {
        String sql = "SELECT id,name FROM person ";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
    }

    @Override
    public Optional<Person> selectPersonById(UUID id) {
        String sql = "SELECT id,name FROM person Where id = ?";
        Person person =  jdbcTemplate.queryForObject(sql,new Object[]{id},(resultSet, i) -> {
            UUID personId = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(personId, name);
        });
        return Optional.ofNullable(person);
    }

    @Override
    public int deletePersonById(UUID id) {
        String sql = "DELETE FROM person WHERE id = ?";
        return jdbcTemplate.update(sql, new Object[]{id});
    }

    @Override
    public int updatePerson(UUID id, Person person) {
        String sql = "UPDATE person set name = ? where id = ?";
        return jdbcTemplate.update(sql, new Object[]{person.getName(),id});
    }
}
