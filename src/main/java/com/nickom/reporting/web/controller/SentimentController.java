package com.nickom.reporting.web.controller;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.services.sentiment.SentimentService;
import com.nickom.reporting.web.common.Constant;
import com.nickom.reporting.web.request.AddSentimentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@RestController
@RequestMapping(value = "report/v1/sentiments", produces = {MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class SentimentController {

    private final SentimentService sentimentService;

    @Autowired
    public SentimentController(SentimentService sentimentService) {
        this.sentimentService = sentimentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<Person> addPerson(
            @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
            @Valid @RequestBody AddSentimentRequest request
    ) {
        return ResponseEntity.ok(this.sentimentService.aggregatePersonSentiment(businessId, request));
    }

}
