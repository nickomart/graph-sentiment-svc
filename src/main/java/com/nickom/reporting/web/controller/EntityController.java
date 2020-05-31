package com.nickom.reporting.web.controller;

import com.nickom.reporting.models.Entity;
import com.nickom.reporting.services.entity.EntityService;
import com.nickom.reporting.web.common.Constant;
import com.nickom.reporting.web.request.CreateEntityRequest;
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
@RequestMapping(value = "report/v1/entity", produces = {
    MediaType.APPLICATION_JSON_VALUE}, consumes = {MediaType.APPLICATION_JSON_VALUE})
public class EntityController {

  private static final Logger logger = LoggerFactory.getLogger(EntityController.class);

  private final EntityService entityService;

  @Autowired
  public EntityController(EntityService entityService) {
    this.entityService = entityService;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<BaseResponse> addEntity(
      @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
      @Valid @RequestBody CreateEntityRequest request
      ) {
    logger.info("Accepting create request: {} for business: {}",
        ToStringBuilder.reflectionToString(request, ToStringStyle.MULTI_LINE_STYLE), businessId);
    Entity entity = entityService.addEntity(
        businessId,
        request.getId(),
        request.getType(),
        request.getName(),
        request.getOwnerIds()
    );
    return ResponseEntity.ok(new BaseResponse(entity.getId(), ZonedDateTime.now().toString()));
  }

  @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
  @ResponseBody
  @ResponseStatus(value = HttpStatus.OK)
  public ResponseEntity<Entity> getEntity(
      @RequestHeader(value = Constant.BUSINESS_ID_HEADER) @NotNull UUID businessId,
      @PathVariable String type,
      @PathVariable UUID id
  ) {
    return ResponseEntity.ok(entityService.findEntity(businessId, id, type));
  }
}
