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

    private final float score;

    private final String category;

    private final float magnitude;


    public MockMeasurableSentiment() {
      float coin = RandomUtils.nextInt(0, 2) > 0 ? 1 : -1;
      float randomScore = RandomUtils.nextInt(0, 11);
      float randomMag = RandomUtils.nextInt(0, 21);
      score = (coin * randomScore) / 10;
      category = CATEGORIES.get(RandomUtils.nextInt(0, CATEGORIES.size()));
      magnitude = randomMag / 10;
    }

    @Override
    public float getScore() {
      return score;
    }

    @Override
    public String getCategory() {
      return category;
    }

    @Override
    public float getMagnitude() {
      return magnitude;
    }
  }
}
