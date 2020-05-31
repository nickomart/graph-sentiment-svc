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
public class EntitySentimentAnalysis extends AbstractSentimentAnalysis {
  @JsonIgnoreProperties({"managers", "subordinates", "sentiments"})
  @StartNode
  private Person author;

  @EndNode
  private Entity entity;

  public EntitySentimentAnalysis() {
    super(null, 0f, 0, new LinkedHashSet<>(), new LinkedHashSet<>());
  }

  public EntitySentimentAnalysis(UUID businessId, float quantifiedScore, int totalFeedback,
                                 Set<String> categories, Set<String> sentimentData, Person author, Entity entity) {
    super(businessId, quantifiedScore, totalFeedback, categories, sentimentData);
    this.author = author;
    this.entity = entity;
  }

  public Person getAuthor() {
    return author;
  }

  public Entity getEntity() {
    return entity;
  }

  @Override
  public boolean canBeMerged(AbstractSentimentAnalysis abstractSentimentAnalysis) {
    if (abstractSentimentAnalysis instanceof EntitySentimentAnalysis) {
      EntitySentimentAnalysis entitySentimentAnalysis =
          EntitySentimentAnalysis.class.cast(abstractSentimentAnalysis);
      return getBusinessId().equals(entitySentimentAnalysis.getBusinessId()) &&
          getEntity().getId().equals(entitySentimentAnalysis.getEntity().getId()) &&
          getEntity().getType().equals(entitySentimentAnalysis.getEntity().getType());
    }
    return false;
  }
}
