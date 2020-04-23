package com.nickom.reporting.models;

import com.nickom.reporting.common.UUIDConverter;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;
import org.neo4j.ogm.annotation.typeconversion.Convert;

@RelationshipEntity(type = "SENTIMENT_TO")
public class SentimentAnalysis {

  @Id
  @GeneratedValue
  private Long id;

  @Convert(value = UUIDConverter.class)
  private UUID businessId;

  private float quantifiedScore;

  private int totalFeedback;

  private Set<String> categories;

  @StartNode
  private Person author;

  @EndNode
  private Person subject;

  public SentimentAnalysis() {
    this(null, 0f, 0, new LinkedHashSet<>(), null, null);
  }

  public SentimentAnalysis(UUID businessId, float quantifiedScore, int totalFeedback,
                           Set<String> categories, Person author, Person subject) {
    this.businessId = businessId;
    this.quantifiedScore = quantifiedScore;
    this.totalFeedback = totalFeedback;
    this.categories = categories;
    this.author = author;
    this.subject = subject;
  }

  public Long getId() {
    return id;
  }

  public float getQuantifiedScore() {
    return quantifiedScore;
  }

  public int getTotalFeedback() {
    return totalFeedback;
  }

  public Set<String> getCategories() {
    return categories;
  }

  public Person getAuthor() {
    return author;
  }

  public Person getSubject() {
    return subject;
  }

  public void setQuantifiedScore(float quantifiedScore) {
    this.quantifiedScore = quantifiedScore;
  }

  public void setTotalFeedback(int totalFeedback) {
    this.totalFeedback = totalFeedback;
  }

  public void setCategories(Set<String> categories) {
    this.categories = categories;
  }

  public UUID getBusinessId() {
    return businessId;
  }
}
