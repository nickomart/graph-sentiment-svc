package com.nickom.reporting.web.request;

import java.util.UUID;

public class FeedbackCommentRequest {

  private UUID id;

  private UUID author;

  private UUID subject;

  private BaseEntityRequest entity;

  private String text;

  public UUID getAuthor() {
    return author;
  }

  public void setAuthor(UUID author) {
    this.author = author;
  }

  public UUID getSubject() {
    return subject;
  }

  public void setSubject(UUID subject) {
    this.subject = subject;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public BaseEntityRequest getEntity() {
    return entity;
  }

  public void setEntity(BaseEntityRequest entity) {
    this.entity = entity;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }
}
