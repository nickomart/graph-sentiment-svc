package com.nickom.reporting.services.sentiment.impl;

import com.nickom.reporting.models.AbstractSentimentAnalysis;
import com.nickom.reporting.models.Entity;
import com.nickom.reporting.models.EntitySentimentAnalysis;
import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.models.SentimentData;
import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.repositories.EntityRepository;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.repositories.SentimentRepository;
import com.nickom.reporting.services.sentiment.SentimentService;
import com.nickom.reporting.services.validation.PersonValidator;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import com.nickom.reporting.web.request.FeedbackCommentRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class PersonSentimentServiceImpl implements SentimentService {

  private static final Logger logger = LoggerFactory.getLogger(PersonSentimentServiceImpl.class);

  private final PersonRepository personRepository;

  private final EntityRepository entityRepository;

  private final SentimentRepository sentimentRepository;

  private final PersonValidator personValidator;

  private final NlpSentimentDao nlpSentimentDao;

  @Autowired
  public PersonSentimentServiceImpl(PersonRepository personRepository,
                                    EntityRepository entityRepository,
                                    SentimentRepository sentimentRepository,
                                    PersonValidator personValidator,
                                    @Qualifier("GcpNlp") NlpSentimentDao nlpSentimentDao) {
    this.personRepository = personRepository;
    this.entityRepository = entityRepository;
    this.sentimentRepository = sentimentRepository;
    this.personValidator = personValidator;
    this.nlpSentimentDao = nlpSentimentDao;
  }

  @Override
  public Person aggregatePersonSentiment(UUID businessId,
                                         FeedbackCommentRequest feedbackCommentRequest) {
    Person author = findAndVerifyPerson(businessId, feedbackCommentRequest.getAuthor());
    Entity entity = null;
    Person subject = null;
    if (feedbackCommentRequest.getSubject() != null) {
      Optional<Person> optionalSubject =
          personRepository.findById(feedbackCommentRequest.getSubject());
      personValidator.ensurePersonAndTenant(businessId, optionalSubject);
      subject = optionalSubject.get();
    } else {
      entity = entityRepository
          .findByBusinessIdAndIdAndType(businessId, feedbackCommentRequest.getEntity().getId(),
              feedbackCommentRequest.getEntity().getType());
      if (entity == null) {
        throw new IllegalArgumentException("report.sentiment.entity.subject.empty");
      }
    }

    MeasurableSentiment measurableSentiment =
        nlpSentimentDao.processText(feedbackCommentRequest.getText());
    SentimentData sentimentData = new SentimentData(
        feedbackCommentRequest.getId() != null ? feedbackCommentRequest.getId() : UUID.randomUUID(),
        businessId,
        feedbackCommentRequest.getText(),
        measurableSentiment.getScore(), measurableSentiment.getMagnitude());
    AbstractSentimentAnalysis newSentiment = subject != null ? new SentimentAnalysis(
        businessId,
        measurableSentiment.getScore(),
        1,
        new LinkedHashSet<>(Arrays.asList(measurableSentiment.getCategory())),
        new LinkedHashSet<>(Arrays.asList(sentimentData.getId().toString())),
        author,
        subject
    ) : new EntitySentimentAnalysis(
        businessId,
        measurableSentiment.getScore(),
        1,
        new LinkedHashSet<>(Arrays.asList(measurableSentiment.getCategory())),
        new LinkedHashSet<>(Arrays.asList(sentimentData.getId().toString())),
        author,
        entity
    );
    logger.info("newSentiment: {}",
        ToStringBuilder.reflectionToString(newSentiment, ToStringStyle.MULTI_LINE_STYLE));

    AbstractSentimentAnalysis currentSentiment =
        CollectionUtils.isEmpty(author.getSentiments()) ? null :
            author.getSentiments()
                .stream()
                .filter(s -> s.canBeMerged(newSentiment))
                .findFirst().orElse(null);
    if (currentSentiment == null) {
      if (CollectionUtils.isEmpty(author.getSentiments())) {
        author.setSentiments(Arrays.asList(newSentiment));
      } else {
        author.getSentiments().add(newSentiment);
      }
    } else {
      aggregateSentiment(currentSentiment, newSentiment);
      logger.info("currentSentiment: {}", ToStringBuilder.reflectionToString(currentSentiment));
    }
    sentimentRepository.save(sentimentData);
    return personRepository.save(author);
  }

  @Override
  public void aggregateSentiment(AbstractSentimentAnalysis currentSentiment,
                                 AbstractSentimentAnalysis newSentiment) {
    if (currentSentiment == null || newSentiment == null) {
      return;
    }
    int totalFeedback = currentSentiment.getTotalFeedback() + newSentiment.getTotalFeedback();
    currentSentiment.setQuantifiedScore(
        ((currentSentiment.getTotalFeedback() * currentSentiment.getQuantifiedScore())
            + (newSentiment.getTotalFeedback() * newSentiment.getQuantifiedScore())) / totalFeedback
    );
    currentSentiment.setTotalFeedback(totalFeedback);
    currentSentiment.getCategories().addAll(newSentiment.getCategories());
    currentSentiment.getSentimentDataIds().addAll(newSentiment.getSentimentDataIds());
  }

  @Override
  public List<SentimentData> findSentimentData(UUID businessId, UUID authorId, UUID subjectId) {
    Person author = findAndVerifyPerson(businessId, authorId);
    Person subject = findAndVerifyPerson(businessId, subjectId);
    SentimentAnalysis sentimentAnalysisLookup = new SentimentAnalysis(
        businessId,
        0f,
        0,
        null,
        null,
        author,
        subject
    );
    return matchSentimentData(businessId, author, sentimentAnalysisLookup);
  }

  @Override
  public List<SentimentData> findSentimentData(UUID businessId, UUID authorId, UUID entityId,
                                               String type) {
    Person author = findAndVerifyPerson(businessId, authorId);
    Entity entity = entityRepository.findByBusinessIdAndIdAndType(businessId, entityId, type);
    EntitySentimentAnalysis sentimentAnalysisLookup = new EntitySentimentAnalysis(
        businessId,
        0f,
        0,
        null,
        null,
        author,
        entity
    );
    return matchSentimentData(businessId, author, sentimentAnalysisLookup);
  }

  private Person findAndVerifyPerson(UUID businessId, UUID id)  {
    Optional<Person> optionalPerson = personRepository.findById(id);
    personValidator.ensurePersonAndTenant(businessId, optionalPerson);
    return optionalPerson.get();
  }

  private List<SentimentData> matchSentimentData(UUID businessId, Person author, AbstractSentimentAnalysis sentimentAnalysisLookup) {
    AbstractSentimentAnalysis currentSentiment =
        author.getSentiments().stream().filter(s -> s.canBeMerged(sentimentAnalysisLookup))
            .findFirst().orElse(null);
    if (currentSentiment == null || CollectionUtils.isEmpty(currentSentiment.getSentimentDataIds())) {
      return new ArrayList<>(0);
    }
    return sentimentRepository.findByBusinessIdAndIdIn(businessId,
        currentSentiment.getSentimentDataIds().stream().map(s -> UUID.fromString(s)).collect(
            Collectors.toList()));
  }
}
