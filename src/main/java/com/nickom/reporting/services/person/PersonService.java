package com.nickom.reporting.services.person;

import com.nickom.reporting.models.Person;

import java.util.Set;
import java.util.UUID;

public interface PersonService {

  Person addPerson(UUID businessId, String name, String email);

  Person findPerson(UUID businessId, UUID id);

  Person managePerson(UUID businessId, UUID id, Set<UUID> subordinateIds);

}
