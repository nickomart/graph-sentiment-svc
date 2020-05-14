package com.nickom.reporting.small.helper;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.UUID;

public class TestHelper {

  public static final UUID BUSINESS_ID = UUID.randomUUID();

  public static Person createPerson() {
    return new Person(UUID.randomUUID(), BUSINESS_ID, "John Doe", "johndoe@mail.com");
  }

  public static Person createSubordinate() {
    return new Person(UUID.randomUUID(), BUSINESS_ID, "Jane Doe", "janedoe@mail.com");
  }

  public static SentimentAnalysis createSentimentAnalysis(Person author, Person subject) {
    return new SentimentAnalysis(UUID.randomUUID(), 0.5f, 1,
        new LinkedHashSet<>(Arrays.asList("Praise")), author, subject);
  }
}
