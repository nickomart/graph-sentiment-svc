package com.nickom.reporting.services.person.impl;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.services.person.PersonService;
import com.nickom.reporting.services.validation.PersonValidator;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceNeo4JImpl implements PersonService {

  private static final Logger logger = LoggerFactory.getLogger(PersonServiceNeo4JImpl.class);

  private final PersonRepository personRepository;

  private final PersonValidator personValidator;

  @Autowired
  public PersonServiceNeo4JImpl(PersonRepository personRepository,
                                PersonValidator personValidator) {
    this.personRepository = personRepository;
    this.personValidator = personValidator;
  }

  @Override
  public Person addPerson(UUID businessId, String name, String email) {
    logger.info("Adding person with name: {} email: {} for business: {}", name, email, businessId);
    Person existingPerson = personRepository.findByEmail(email);
    if (null != existingPerson) {
      throw new IllegalArgumentException("report.email.already.exists");
    }
    return this.personRepository.save(new Person(businessId, name, email));
  }

  @Override
  public Person findPerson(UUID businessId, UUID id) {
    Optional<Person> person = this.personRepository.findById(id);
    personValidator.ensurePersonAndTenant(businessId, person);
    return person.get();
  }

  @Override
  public Person managePerson(UUID businessId, UUID id, Set<UUID> subordinateIds) {
    personValidator.ensureSelfAndSubordinate(id, subordinateIds);
    Optional<Person> optionalPerson = personRepository.findById(id);
    personValidator.ensurePersonAndTenant(businessId, optionalPerson);
    Person person = optionalPerson.get();
    personValidator.ensureManagerAndSubordinate(
        person.getManagers().stream().map(p -> p.getId()).collect(Collectors.toList()),
        subordinateIds
    );
    for (Person subordinate : personRepository.findAllById(subordinateIds)) {
      person.manage(subordinate);
    }
    return personRepository.save(person);
  }
}
