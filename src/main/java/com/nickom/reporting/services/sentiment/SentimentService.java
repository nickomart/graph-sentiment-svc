package com.nickom.reporting.services.sentiment;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.web.request.FeedbackCommentRequest;

import java.util.UUID;

public interface SentimentService {

  Person aggregatePersonSentiment(UUID businessId, FeedbackCommentRequest feedbackCommentRequest);

  void aggregateSentiment(SentimentAnalysis currentSentiment, SentimentAnalysis newSentiment);

}
