package com.nickom.reporting.web.request;

import java.util.UUID;

public class BaseEntityRequest {

  private UUID id;

  private String type;

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }
}
