package com.nickom.reporting.web.request;

import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

public class ManagePersonRequest {

  @NotNull
  private Set<UUID> subordinateIds;

  public Set<UUID> getSubordinateIds() {
    return subordinateIds;
  }

  public void setSubordinateIds(Set<UUID> subordinateIds) {
    this.subordinateIds = subordinateIds;
  }
}
