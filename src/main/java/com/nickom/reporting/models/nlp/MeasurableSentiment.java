package com.nickom.reporting.models.nlp;

public interface MeasurableSentiment {

  // sentiment score -1 to 1
  float getScore();

  String getCategory();

  // magnitude or emotion, strength of sentiment, 0 to ~
  float getMagnitude();
}
