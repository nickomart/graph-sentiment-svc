package com.nickom.reporting.web.request;

import java.util.UUID;

public class FeedbackCommentRequest {

  private UUID author;

  private UUID subject;

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

}
