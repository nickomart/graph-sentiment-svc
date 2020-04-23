package com.nickom.reporting.web.controller;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.services.person.PersonService;
import com.nickom.reporting.web.common.Constant;
import com.nickom.reporting.web.request.CreatePersonRequest;
import com.nickom.reporting.web.request.ManagePersonRequest;
import com.nickom.reporting.web.response.BaseResponse;
import java.time.ZonedDateTime;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "report/v1/person", produces = {
    MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class PersonController {

  private static final Logger logger = LoggerFactory.getLogger(PersonController.class);

  private final PersonService personService;

  @Autowired
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<BaseResponse> addPerson(
      @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
      @Valid @RequestBody CreatePersonRequest request
  ) {
    logger.info("Accepting create request: {} for business: {}",
        ToStringBuilder.reflectionToString(request, ToStringStyle.MULTI_LINE_STYLE), businessId);
    Person person = personService.addPerson(
        businessId,
        request.getName(),
        request.getEmail()
    );
    return ResponseEntity.ok(new BaseResponse(person.getId(), ZonedDateTime.now().toString()));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PATCH)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<Person> managePerson(
      @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
      @PathVariable UUID id,
      @Valid @RequestBody ManagePersonRequest request
  ) {
    logger.info("Accepting manage request: {} for business: {}",
        ToStringBuilder.reflectionToString(request, ToStringStyle.MULTI_LINE_STYLE), businessId);
    return ResponseEntity
        .ok(personService.managePerson(businessId, id, request.getSubordinateIds()));
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<Person> getPerson(
      @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
      @PathVariable UUID id
  ) {
    return ResponseEntity.ok(personService.findPerson(businessId, id));
  }
}
