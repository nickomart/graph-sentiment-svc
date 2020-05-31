package com.nickom.reporting.small.services;

import com.nickom.reporting.models.AbstractSentimentAnalysis;
import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.models.nlp.MeasurableSentiment;
import com.nickom.reporting.repositories.EntityRepository;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.repositories.SentimentRepository;
import com.nickom.reporting.services.sentiment.SentimentService;
import com.nickom.reporting.services.sentiment.impl.PersonSentimentServiceImpl;
import com.nickom.reporting.services.validation.PersonValidator;
import com.nickom.reporting.services.validation.impl.PersonValidatorImpl;
import com.nickom.reporting.small.helper.TestHelper;
import com.nickom.reporting.web.dao.NlpSentimentDao;
import com.nickom.reporting.web.dao.impl.MockNlpSentimentDaoImpl;
import com.nickom.reporting.web.request.FeedbackCommentRequest;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class SentimentServiceTest {

  private SentimentService sentimentService;

  @Mock
  private PersonRepository personRepository;

  @Mock
  private EntityRepository entityRepository;

  @Mock
  private SentimentRepository sentimentRepository;

  @Mock
  private NlpSentimentDao nlpSentimentDao;

  private PersonValidator personValidator;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.initMocks(this);
    personValidator = new PersonValidatorImpl();
    sentimentService =
        new PersonSentimentServiceImpl(personRepository, entityRepository, sentimentRepository,
            personValidator, nlpSentimentDao);
  }

  @AfterEach
  public void tearDown() {
    Mockito.reset(personRepository);
  }

  @Test
  public void givenNoExistingSentiment_whenMerge_thenDoNothing() {
    SentimentAnalysis sentimentAnalysis = Mockito.mock(SentimentAnalysis.class);
    sentimentService.aggregateSentiment(null, sentimentAnalysis);

    Mockito.verify(sentimentAnalysis, Mockito.never()).getTotalFeedback();
    Mockito.verify(sentimentAnalysis, Mockito.never()).getQuantifiedScore();
    Mockito.verify(sentimentAnalysis, Mockito.never()).getCategories();
  }

  @Test
  public void givenExistingSentiment_whenMergeNullSentiment_thenDoNothing() {
    SentimentAnalysis sentimentAnalysis = Mockito.mock(SentimentAnalysis.class);
    sentimentService.aggregateSentiment(sentimentAnalysis, null);

    Mockito.verify(sentimentAnalysis, Mockito.never()).getTotalFeedback();
    Mockito.verify(sentimentAnalysis, Mockito.never()).getQuantifiedScore();
    Mockito.verify(sentimentAnalysis, Mockito.never()).getCategories();
  }

  @Test
  public void givenExistingSentiment_whenMergeSentiment_thenCurrentSentimentUpdated() {
    SentimentAnalysis currentSentiment = TestHelper.createSentimentAnalysis(TestHelper.createPerson(), TestHelper.createSubordinate());
    SentimentAnalysis newSentiment = TestHelper.createSentimentAnalysis(TestHelper.createPerson(), TestHelper.createSubordinate());

    sentimentService.aggregateSentiment(currentSentiment, newSentiment);

    Assertions.assertEquals(2, currentSentiment.getTotalFeedback());
    Assertions.assertEquals(1, currentSentiment.getCategories().size());
    Assertions.assertEquals(Math.round(newSentiment.getQuantifiedScore() * 2), Math.round(currentSentiment.getQuantifiedScore()));
  }

  @Test
  public void giveFeedbackComment_whenAggregateToPerson_thenPersonSentimentUpdated() {
    Person author = TestHelper.createPerson();
    Person subject = TestHelper.createSubordinate();
    MeasurableSentiment measurableSentiment = new MockNlpSentimentDaoImpl.MockMeasurableSentiment();
    FeedbackCommentRequest feedbackCommentRequest = new FeedbackCommentRequest();
    feedbackCommentRequest.setAuthor(author.getId());
    feedbackCommentRequest.setSubject(subject.getId());
    feedbackCommentRequest.setText("Hello");
    Mockito.when(personRepository.findById(author.getId())).thenReturn(Optional.of(author));
    Mockito.when(personRepository.findById(subject.getId())).thenReturn(Optional.of(subject));
    Mockito.when(nlpSentimentDao.processText(Mockito.any())).thenReturn(measurableSentiment);
    ArgumentCaptor<Person> argumentCaptor = ArgumentCaptor.forClass(Person.class);

    sentimentService.aggregatePersonSentiment(TestHelper.BUSINESS_ID, feedbackCommentRequest);

    Mockito.verify(personRepository, Mockito.times(1)).save(argumentCaptor.capture());
    Person person = argumentCaptor.getValue();
    Assertions.assertEquals(1, person.getSentiments().size());
    AbstractSentimentAnalysis abstractSentimentAnalysis = person.getSentiments().get(0);
    Assertions.assertEquals(1, abstractSentimentAnalysis.getTotalFeedback());
    Assertions.assertTrue(abstractSentimentAnalysis instanceof SentimentAnalysis);
    SentimentAnalysis sentimentAnalysis = SentimentAnalysis.class.cast(abstractSentimentAnalysis);
    Assertions.assertEquals(subject, sentimentAnalysis.getSubject());
    Assertions.assertEquals(author, sentimentAnalysis.getAuthor());

  }
}
