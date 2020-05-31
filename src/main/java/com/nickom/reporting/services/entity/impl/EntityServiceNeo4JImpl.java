package com.nickom.reporting.services.entity.impl;

import com.nickom.reporting.models.Entity;
import com.nickom.reporting.models.Person;
import com.nickom.reporting.repositories.EntityRepository;
import com.nickom.reporting.repositories.PersonRepository;
import com.nickom.reporting.services.entity.EntityService;
import com.nickom.reporting.services.validation.PersonValidator;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
public class EntityServiceNeo4JImpl implements EntityService {

  private final EntityRepository entityRepository;

  private final PersonRepository personRepository;

  private final PersonValidator personValidator;

  @Autowired
  public EntityServiceNeo4JImpl(EntityRepository entityRepository, PersonRepository personRepository, PersonValidator personValidator) {
    this.entityRepository = entityRepository;
    this.personRepository = personRepository;
    this.personValidator = personValidator;
  }

  @Override
  public Entity findEntity(UUID businessId, UUID id, String type) {
    return entityRepository.findByBusinessIdAndIdAndType(businessId, id, type);
  }

  @Override
  public Entity addEntity(UUID businessId, UUID id, String type, String name, Set<UUID> ownerIds) {
    if (findEntity(businessId, id, type) != null) {
      throw new IllegalArgumentException("report.entity.already.exists");
    }
    if (CollectionUtils.isEmpty(ownerIds)) {
      throw new IllegalArgumentException("report.entity.owner.is.empty");
    }
    List<Person> owners = new ArrayList<>(ownerIds.size());
    for (Person owner : personRepository.findAllById(ownerIds)) {
      personValidator.ensurePersonAndTenant(businessId, Optional.of(owner));
      owners.add(owner);
    }
    if (ownerIds.size() != owners.size()) {
      throw new IllegalArgumentException("report.entity.owner.mismatch");
    }
    return entityRepository.save(new Entity(id, businessId, name, type, owners));
  }
}
