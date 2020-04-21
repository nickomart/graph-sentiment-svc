package com.nickom.reporting.services.sentiment;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.web.request.AddSentimentRequest;

import java.util.UUID;

public interface SentimentService {

    Person aggregatePersonSentiment(UUID businessId, AddSentimentRequest addSentimentRequest);

}
