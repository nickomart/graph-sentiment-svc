package com.nickom.reporting.common;

import org.neo4j.ogm.id.IdStrategy;

import java.util.UUID;

public class RandomUUIDStrategy implements IdStrategy {
  @Override
  public Object generateId(Object entity) {
    return UUID.randomUUID();
  }
}
