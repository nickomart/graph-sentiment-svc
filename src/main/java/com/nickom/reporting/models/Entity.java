package com.nickom.reporting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nickom.reporting.common.UUIDConverter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@NodeEntity
public class Entity {

  @Id
  @GeneratedValue
  private Long internalId;

  @Index
  @Convert(value = UUIDConverter.class)
  private UUID id;

  @Index
  @Convert(value = UUIDConverter.class)
  private UUID businessId;

  private String name;

  @Index
  private String type;

  public Entity() {
  }

  public Entity(UUID id, UUID businessId, String name, String type,
                List<Person> owners) {
    this.id = id;
    this.businessId = businessId;
    this.name = name;
    this.type = type;
    this.owners = owners;
  }

  @JsonIgnoreProperties({"managers", "subordinates", "sentiments"})
  @Relationship(type = "BELONG_TO")
  private List<Person> owners = new ArrayList<>();

  public Long getInternalId() {
    return internalId;
  }

  public UUID getId() {
    return id;
  }

  public UUID getBusinessId() {
    return businessId;
  }

  public String getType() {
    return type;
  }

  public List<Person> getOwners() {
    return owners;
  }

  public String getName() {
    return name;
  }
}
