package com.nickom.reporting.web.request;

import java.util.Set;
import java.util.UUID;

public class CreateEntityRequest extends BaseEntityRequest {

  private String name;

  private Set<UUID> ownerIds;

  public Set<UUID> getOwnerIds() {
    return ownerIds;
  }

  public void setOwnerIds(Set<UUID> ownerIds) {
    this.ownerIds = ownerIds;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
