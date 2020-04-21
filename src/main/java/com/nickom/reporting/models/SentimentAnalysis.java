package com.nickom.reporting.models;

import com.nickom.reporting.common.UUIDConverter;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.*;

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

    public SentimentAnalysis(UUID businessId, float quantifiedScore, int totalFeedback, Set<String> categories, Person author, Person subject) {
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

    public void aggregateSentiment(SentimentAnalysis sentimentAnalysis) {
        totalFeedback++;
        quantifiedScore = (quantifiedScore + sentimentAnalysis.quantifiedScore)/totalFeedback;
        categories.addAll(sentimentAnalysis.categories);
    }
}
