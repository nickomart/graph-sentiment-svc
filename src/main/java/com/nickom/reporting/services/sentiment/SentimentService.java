package com.nickom.reporting.services.sentiment;

import com.nickom.reporting.models.AbstractSentimentAnalysis;
import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.models.SentimentData;
import com.nickom.reporting.web.request.FeedbackCommentRequest;

import java.util.List;
import java.util.UUID;

public interface SentimentService {

  Person aggregatePersonSentiment(UUID businessId, FeedbackCommentRequest feedbackCommentRequest);

  void aggregateSentiment(AbstractSentimentAnalysis currentSentiment, AbstractSentimentAnalysis newSentiment);

  List<SentimentData> findSentimentData(UUID businessId, UUID author, UUID subject);

  List<SentimentData> findSentimentData(UUID businessId, UUID author, UUID entityId, String type);

}
