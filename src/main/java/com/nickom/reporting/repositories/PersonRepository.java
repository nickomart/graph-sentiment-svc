package com.nickom.reporting.repositories;

import com.nickom.reporting.models.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, UUID> {

  Person findByEmail(String email);

}
