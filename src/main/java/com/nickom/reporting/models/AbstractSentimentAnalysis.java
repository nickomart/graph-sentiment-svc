package com.nickom.reporting.models;

import com.nickom.reporting.common.UUIDConverter;
import java.util.Set;
import java.util.UUID;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.typeconversion.Convert;

public abstract class AbstractSentimentAnalysis {

  @Id
  @GeneratedValue
  private Long id;

  @Convert(value = UUIDConverter.class)
  private UUID businessId;

  private float quantifiedScore;

  private int totalFeedback;

  private Set<String> categories;

  private Set<String> sentimentDataIds;

  public AbstractSentimentAnalysis(UUID businessId, float quantifiedScore,
                                   int totalFeedback,
                                   Set<String> categories,
                                   Set<String> sentimentDataIds) {
    this.businessId = businessId;
    this.quantifiedScore = quantifiedScore;
    this.totalFeedback = totalFeedback;
    this.categories = categories;
    this.sentimentDataIds = sentimentDataIds;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public UUID getBusinessId() {
    return businessId;
  }

  public void setBusinessId(UUID businessId) {
    this.businessId = businessId;
  }

  public float getQuantifiedScore() {
    return quantifiedScore;
  }

  public void setQuantifiedScore(float quantifiedScore) {
    this.quantifiedScore = quantifiedScore;
  }

  public int getTotalFeedback() {
    return totalFeedback;
  }

  public void setTotalFeedback(int totalFeedback) {
    this.totalFeedback = totalFeedback;
  }

  public Set<String> getCategories() {
    return categories;
  }

  public void setCategories(Set<String> categories) {
    this.categories = categories;
  }

  public Set<String> getSentimentDataIds() {
    return sentimentDataIds;
  }

  public void setSentimentDataIds(Set<String> sentimentDataIds) {
    this.sentimentDataIds = sentimentDataIds;
  }

  public abstract boolean canBeMerged(AbstractSentimentAnalysis abstractSentimentAnalysis);
}
