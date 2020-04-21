package com.nickom.reporting.services.sentiment.impl;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.models.SentimentAnalysis;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.services.sentiment.SentimentService;
import com.nickom.reporting.services.validation.PersonValidator;
import com.nickom.reporting.web.request.AddSentimentRequest;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Service
public class PersonSentimentServiceImpl implements SentimentService {

    private static final Logger logger = LoggerFactory.getLogger(PersonSentimentServiceImpl.class);

    private final PersonRepository personRepository;

    private final PersonValidator personValidator;

    @Autowired
    public PersonSentimentServiceImpl(PersonRepository personRepository, PersonValidator personValidator) {
        this.personRepository = personRepository;
        this.personValidator = personValidator;
    }

    @Override
    public Person aggregatePersonSentiment(UUID businessId, AddSentimentRequest addSentimentRequest) {
        Optional<Person> optionalAuthor = personRepository.findById(addSentimentRequest.getAuthor());
        personValidator.ensurePersonAndTenant(businessId, optionalAuthor);
        Optional<Person> optionalSubject = personRepository.findById(addSentimentRequest.getSubject());
        personValidator.ensurePersonAndTenant(businessId, optionalSubject);
        Person author = optionalAuthor.get();
        Person subject = optionalSubject.get();
        SentimentAnalysis newSentiment = new SentimentAnalysis(
                businessId,
                addSentimentRequest.getScore(),
                1,
                new LinkedHashSet<>(Arrays.asList(addSentimentRequest.getCategory())),
                author,
                subject
        );
        logger.info("newSentiment author: {}", ToStringBuilder.reflectionToString(newSentiment.getAuthor()));
        logger.info("newSentiment subject: {}", ToStringBuilder.reflectionToString(newSentiment.getSubject()));
        logger.info("newSentiment: {}", ToStringBuilder.reflectionToString(newSentiment));
        /*
        if (author.getSentiment() != null) {
            newSentiment.aggregateSentiment(author.getSentiment());
        }
        */
        if (CollectionUtils.isEmpty(author.getSentiments())) {
            author.setSentiments(new ArrayList<>());
        }
        author.getSentiments().add(newSentiment);
        //author.authored(subject);
        return personRepository.save(author);
    }

}
