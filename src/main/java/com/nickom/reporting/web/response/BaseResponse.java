package com.nickom.reporting.web.response;

import java.util.UUID;

public class BaseResponse {

  private UUID id;

  private String timestamp;

  public BaseResponse() {
  }

  public BaseResponse(UUID id, String timestamp) {
    this.id = id;
    this.timestamp = timestamp;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(String timestamp) {
    this.timestamp = timestamp;
  }
}
