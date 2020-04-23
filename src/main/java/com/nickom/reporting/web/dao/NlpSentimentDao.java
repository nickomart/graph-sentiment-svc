package com.nickom.reporting.web.dao;

import com.nickom.reporting.models.nlp.MeasurableSentiment;

public interface NlpSentimentDao {

  MeasurableSentiment processText(String text);

}
