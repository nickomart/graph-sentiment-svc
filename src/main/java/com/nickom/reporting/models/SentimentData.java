package com.nickom.reporting.models;

import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sentiment-data")
public class SentimentData {

  @Id
  private UUID id;

  private UUID businessId;

  private String text;

  private float score;

  private float magnitude;

  public SentimentData() {
  }

  public SentimentData(UUID id, UUID businessId, String text, float score, float magnitude) {
    this.id = id;
    this.businessId = businessId;
    this.text = text;
    this.score = score;
    this.magnitude = magnitude;
  }

  public String getText() {
    return text;
  }

  public float getScore() {
    return score;
  }

  public float getMagnitude() {
    return magnitude;
  }

  public UUID getId() {
    return id;
  }

  public UUID getBusinessId() {
    return businessId;
  }
}
