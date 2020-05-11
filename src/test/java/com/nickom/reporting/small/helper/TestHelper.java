package com.nickom.reporting.small.helper;

import com.nickom.reporting.models.Person;
import java.util.UUID;

public class TestHelper {

  public static final UUID BUSINESS_ID = UUID.randomUUID();

  public static Person createPerson() {
    return new Person(UUID.randomUUID(), BUSINESS_ID, "John Doe", "johndoe@mail.com");
  }

  public static Person createSubordinate() {
    return new Person(UUID.randomUUID(), BUSINESS_ID, "Jane Doe", "janedoe@mail.com");
  }
}
