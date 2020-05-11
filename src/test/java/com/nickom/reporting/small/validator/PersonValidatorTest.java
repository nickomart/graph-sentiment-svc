package com.nickom.reporting.small.validator;


import static com.nickom.reporting.small.helper.TestHelper.BUSINESS_ID;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.services.validation.PersonValidator;
import com.nickom.reporting.services.validation.impl.PersonValidatorImpl;
import com.nickom.reporting.small.helper.TestHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonValidatorTest {

  private PersonValidator personValidator;

  private Person person;

  private Person subordinate;

  @BeforeEach
  public void setup() {
    personValidator = new PersonValidatorImpl();
    person = TestHelper.createPerson();
    subordinate = TestHelper.createSubordinate();
  }

  @Test
  public void givenPersonExistsAndBusinessIdMatched_whenEnsurePerson_thenShouldSuccess() {
    personValidator.ensurePersonAndTenant(BUSINESS_ID, Optional.of(person));
  }

  @Test
  public void givenPersonExistsAndBusinessIdNotMatched_whenEnsurePerson_thenShouldThrowError() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      personValidator.ensurePersonAndTenant(UUID.randomUUID(), Optional.of(person));
    });
  }

  @Test
  public void givenPersonNotExists_whenEnsurePerson_thenShouldThrowError() {
    Assertions.assertThrows(RuntimeException.class, () -> {
      personValidator.ensurePersonAndTenant(BUSINESS_ID, Optional.of(null));
    });
  }

  @Test
  public void givenValidSubordinatesAndSelf_whenEnsureSelfAndSubordinates_thenShouldSuccess() {
    Collection<UUID> subs = Arrays.asList(subordinate).stream().map(s -> s.getId()).collect(Collectors.toList());
    personValidator.ensureSelfAndSubordinate(person.getId(), subs);
  }

  @Test
  public void givenEmptySubordinates_whenEnsureSelfAndSubordinates_thenShouldThrowError() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      personValidator.ensureSelfAndSubordinate(person.getId(), new ArrayList<>(0));
    });
  }

  @Test
  public void givenSubordinatesContainsSelf_whenEnsureSelfAndSubordinates_thenShouldThrowError() {
    Collection<UUID> subs = Arrays.asList(person, subordinate).stream().map(s -> s.getId()).collect(Collectors.toList());
    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      personValidator.ensureSelfAndSubordinate(person.getId(), subs);
    });
  }

  @Test
  public void givenManagerIsNotInSubordinates_whenEnsureManagerAndSubordinates_thenShouldSuccess() {
    Collection<UUID> managers = Arrays.asList(person).stream().map(s -> s.getId()).collect(Collectors.toList());
    Collection<UUID> subs = Arrays.asList(subordinate).stream().map(s -> s.getId()).collect(Collectors.toList());
    personValidator.ensureManagerAndSubordinate(new ArrayList<>(0), new ArrayList<>());
    personValidator.ensureManagerAndSubordinate(new ArrayList<>(0), subs);
    personValidator.ensureManagerAndSubordinate(managers, new ArrayList<>());
    personValidator.ensureManagerAndSubordinate(managers, subs);
  }

  @Test
  public void givenManagerInSubordinates_whenEnsureManagerAndSubordinates_thenShouldThrowError() {
    Collection<UUID> managers = Arrays.asList(person, subordinate).stream().map(s -> s.getId()).collect(Collectors.toList());
    Collection<UUID> subs = Arrays.asList(subordinate).stream().map(s -> s.getId()).collect(Collectors.toList());

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      personValidator.ensureManagerAndSubordinate(managers, subs);
    });

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      personValidator.ensureManagerAndSubordinate(subs, subs);
    });
  }
}
