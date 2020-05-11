package com.nickom.reporting.small.services;

import static com.nickom.reporting.small.helper.TestHelper.BUSINESS_ID;
import static org.mockito.ArgumentMatchers.any;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.services.person.PersonService;
import com.nickom.reporting.services.person.impl.PersonServiceNeo4JImpl;
import com.nickom.reporting.services.validation.impl.PersonValidatorImpl;
import com.nickom.reporting.small.helper.TestHelper;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class PersonServiceTest {

  private PersonService personService;

  @Mock
  private PersonRepository personRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    personService = new PersonServiceNeo4JImpl(personRepository, new PersonValidatorImpl());
  }

  @AfterEach
  public void tearDown() {
    Mockito.reset(personRepository);
  }

  @Test
  public void givenPersonNotExists_whenSavePerson_thenSuccess() {
    Person person = TestHelper.createPerson();
    Mockito.when(personRepository.findByEmail(person.getEmail())).thenReturn(null);

    personService.addPerson(BUSINESS_ID, person.getName(), person.getEmail());

    Mockito.verify(personRepository, Mockito.times(1)).save(any());
  }

  @Test
  public void givenPersonExists_whenSavePerson_thenShouldThrowError() {
    Person person = TestHelper.createPerson();
    Mockito.when(personRepository.findByEmail(person.getEmail())).thenReturn(person);

    Assertions.assertThrows(IllegalArgumentException.class, () -> {
      personService.addPerson(BUSINESS_ID, person.getName(), person.getEmail());
    });
  }

  @Test
  public void givenPersonExists_whenFindById_thenReturnPerson() {
    Person person = TestHelper.createPerson();
    Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));

    Person actualPerson = personService.findPerson(person.getBusinessId(), person.getId());

    Assertions.assertEquals(person, actualPerson);
  }

  @Test
  public void givenPersonNotExists_whenFindById_thenShouldThrowError() {
    Person person = TestHelper.createPerson();
    Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.empty());

    Assertions.assertThrows(RuntimeException.class, () -> {
      personService.findPerson(person.getBusinessId(), person.getId());
    });
  }

  @Test
  public void givenPersonExistsWithDifferentTenant_whenFindById_thenShouldThrowError() {
    Person person = TestHelper.createPerson();
    Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.empty());

    Assertions.assertThrows(RuntimeException.class, () -> {
      personService.findPerson(UUID.randomUUID(), person.getId());
    });
  }

  @Test
  public void givenPersonAndSubordinate_whenManage_thenShouldRelateBoth() {
    Person person = TestHelper.createPerson();
    Person subordinate = TestHelper.createSubordinate();

    Mockito.when(personRepository.findById(person.getId())).thenReturn(Optional.of(person));
    Mockito.when(personRepository.findAllById(any())).thenReturn(Arrays.asList(subordinate));
    ArgumentCaptor<Person> argumentCaptor = ArgumentCaptor.forClass(Person.class);

    personService.managePerson(BUSINESS_ID, person.getId(), new LinkedHashSet<>(Arrays.asList(subordinate.getId())));

    Mockito.verify(personRepository, Mockito.times(1)).save(argumentCaptor.capture());
    Person actualPerson = argumentCaptor.getValue();
    Assertions.assertEquals(1, actualPerson.getSubordinates().size());
    Assertions.assertEquals(subordinate.getId(), actualPerson.getSubordinates().get(0).getId());
    Assertions.assertEquals(person.getId(), actualPerson.getSubordinates().get(0).getManagers().get(0).getId());
  }
}
