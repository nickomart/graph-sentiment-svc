package com.nickom.reporting.small.web;

import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import com.nickom.reporting.web.dao.impl.MockNlpSentimentDaoImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NlpSentimentDaoTest {

  @Test
  public void givenMockNlpSentimentDao_whenProces_thenReturnMockSentiment() {
    NlpSentimentDao dao = new MockNlpSentimentDaoImpl();

    MeasurableSentiment measurableSentiment = dao.processText("Hello");

    Assertions.assertNotNull(measurableSentiment.getScore());
    Assertions.assertNotNull(measurableSentiment.getCategory());
  }
}
