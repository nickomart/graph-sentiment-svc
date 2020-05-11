package com.nickom.reporting.web.dao.impl;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import java.io.IOException;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;

@Component("GcpNlp")
public class GcpNlpSentimentDaoImpl implements NlpSentimentDao {

  private static final Logger LOGGER = LoggerFactory.getLogger(GcpNlpSentimentDaoImpl.class);

  @Override
  public MeasurableSentiment processText(String text) {
    LOGGER.error("Try to process text: {} to GCP NLP API", text);
    try (LanguageServiceClient language = LanguageServiceClient.create()) {
      Document doc = Document.newBuilder().setContent(text).setType(Document.Type.PLAIN_TEXT).build();
      AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
      Sentiment sentiment = response.getDocumentSentiment();
      ClassifyTextResponse classifyTextResponse = language.classifyText(doc);
      LOGGER.error("GCP NLP API call is success with sentiment: {} and classifyTextResponse: {}",
          ToStringBuilder.reflectionToString(sentiment), ToStringBuilder.reflectionToString(classifyTextResponse));
      return new GcpNlpSentimentWrapper(sentiment, classifyTextResponse);
    } catch (IOException e) {
      LOGGER.error("Failed to due to IO error", e);
      throw new ResourceAccessException("Cannot reach GCP NLP api");
    }
  }

  public static class GcpNlpSentimentWrapper implements MeasurableSentiment {

    private Sentiment sentiment;
    private ClassifyTextResponse classifyTextResponse;

    public GcpNlpSentimentWrapper(Sentiment sentiment, ClassifyTextResponse classifyTextResponse) {
      this.sentiment = sentiment;
      this.classifyTextResponse = classifyTextResponse;
    }

    @Override
    public float getScore() {
      return sentiment.getScore();
    }

    @Override
    public String getCategory() {
      return classifyTextResponse.getCategoriesList().toString();
    }
  }
}
