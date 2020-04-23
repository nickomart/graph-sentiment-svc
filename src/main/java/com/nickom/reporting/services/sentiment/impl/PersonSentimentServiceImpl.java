package com.nickom.reporting.services.sentiment.impl;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.services.sentiment.SentimentService;
import com.nickom.reporting.services.validation.PersonValidator;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import com.nickom.reporting.web.request.FeedbackCommentRequest;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PersonSentimentServiceImpl implements SentimentService {

  private static final Logger logger = LoggerFactory.getLogger(PersonSentimentServiceImpl.class);

  private final PersonRepository personRepository;

  private final PersonValidator personValidator;

  private final NlpSentimentDao nlpSentimentDao;

  @Autowired
  public PersonSentimentServiceImpl(PersonRepository personRepository,
                                    PersonValidator personValidator,
                                    NlpSentimentDao nlpSentimentDao) {
    this.personRepository = personRepository;
    this.personValidator = personValidator;
    this.nlpSentimentDao = nlpSentimentDao;
  }

  @Override
  public Person aggregatePersonSentiment(UUID businessId,
                                         FeedbackCommentRequest feedbackCommentRequest) {
    Optional<Person> optionalAuthor = personRepository.findById(feedbackCommentRequest.getAuthor());
    personValidator.ensurePersonAndTenant(businessId, optionalAuthor);
    Optional<Person> optionalSubject =
        personRepository.findById(feedbackCommentRequest.getSubject());
    personValidator.ensurePersonAndTenant(businessId, optionalSubject);
    Person author = optionalAuthor.get();
    Person subject = optionalSubject.get();
    MeasurableSentiment measurableSentiment =
        nlpSentimentDao.processText(feedbackCommentRequest.getText());
    SentimentAnalysis newSentiment = new SentimentAnalysis(
        businessId,
        measurableSentiment.getScore(),
        1,
        new LinkedHashSet<>(Arrays.asList(measurableSentiment.getCategory())),
        author,
        subject
    );
    SentimentAnalysis currentSentiment = CollectionUtils.isEmpty(author.getSentiments()) ? null :
        author.getSentiments()
            .stream()
            .filter(s -> s.getSubject().equals(newSentiment.getSubject()) &&
                s.getBusinessId().equals(newSentiment.getBusinessId()))
            .findFirst().orElse(null);
    if (CollectionUtils.isEmpty(author.getSentiments())) {
      author.setSentiments(Arrays.asList(newSentiment));
    } else {
      aggregateSentiment(currentSentiment, newSentiment);
    }
    logger.info("newSentiment: {}", ToStringBuilder.reflectionToString(newSentiment));
    logger.info("currentSentiment: {}", ToStringBuilder.reflectionToString(currentSentiment));
    return personRepository.save(author);
  }

  @Override
  public void aggregateSentiment(SentimentAnalysis currentSentiment,
                                 SentimentAnalysis newSentiment) {
    if (currentSentiment == null) {
      return;
    }
    int totalFeedback = currentSentiment.getTotalFeedback() + newSentiment.getTotalFeedback();
    currentSentiment.setQuantifiedScore(
        ((currentSentiment.getTotalFeedback() * currentSentiment.getQuantifiedScore())
            + (newSentiment.getTotalFeedback() * newSentiment.getQuantifiedScore())) / totalFeedback
    );
    currentSentiment.setTotalFeedback(totalFeedback);
    currentSentiment.getCategories().addAll(newSentiment.getCategories());
  }

}
