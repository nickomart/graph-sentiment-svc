package com.nickom.reporting.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

@RelationshipEntity(type = "SENTIMENT_TO")
public class SentimentAnalysis extends AbstractSentimentAnalysis {

  @JsonIgnoreProperties({"managers", "subordinates", "sentiments"})
  @StartNode
  private Person author;

  @JsonIgnoreProperties({"managers", "subordinates", "sentiments"})
  @EndNode
  private Person subject;


  public SentimentAnalysis() {
    super(null, 0f, 0, new LinkedHashSet<>(), new LinkedHashSet<>());
  }

  public SentimentAnalysis(UUID businessId, float quantifiedScore, int totalFeedback,
                           Set<String> categories, Set<String> sentimentData, Person author, Person subject) {
    super(businessId, quantifiedScore, totalFeedback, categories, sentimentData);
    this.author = author;
    this.subject = subject;
  }

  public Person getAuthor() {
    return author;
  }

  public Person getSubject() {
    return subject;
  }

  @Override
  public boolean canBeMerged(AbstractSentimentAnalysis abstractSentimentAnalysis) {
    if (abstractSentimentAnalysis instanceof SentimentAnalysis) {
      SentimentAnalysis sentimentAnalysis = SentimentAnalysis.class.cast(abstractSentimentAnalysis);
      return getBusinessId().equals(sentimentAnalysis.getBusinessId()) && getSubject().getId().equals(sentimentAnalysis.getSubject().getId());
    }
    return false;
  }
}

