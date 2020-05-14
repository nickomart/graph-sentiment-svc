package com.nickom.reporting.web.dao.impl;

import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Component;

@Component("Mock")
public class MockNlpSentimentDaoImpl implements NlpSentimentDao {
  @Override
  public MeasurableSentiment processText(String text) {
    return new MockMeasurableSentiment();
  }

  public static class MockMeasurableSentiment implements MeasurableSentiment {

    static final List<String> CATEGORIES = Collections.unmodifiableList(Arrays.asList(
        "Confidence",
        "Documentation",
        "Aggressiveness",
        "Doubt",
        "Fear"
    ));

    float score;

    String category;

    public MockMeasurableSentiment() {
      float coin = RandomUtils.nextInt(0, 2) > 0 ? 1 : -1;
      float randomScore = RandomUtils.nextInt(0, 11);
      score = (coin * randomScore) / 10;
      category = CATEGORIES.get(RandomUtils.nextInt(0, CATEGORIES.size()));
    }

    @Override
    public float getScore() {
      return score;
    }

    @Override
    public String getCategory() {
      return category;
    }
  }
}
