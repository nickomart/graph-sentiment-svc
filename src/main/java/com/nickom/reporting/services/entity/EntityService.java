package com.nickom.reporting.services.entity;

import com.nickom.reporting.models.Entity;
import java.util.Set;
import java.util.UUID;

public interface EntityService {

  Entity findEntity(UUID businessId, UUID id, String type);

  Entity addEntity(UUID businessId, UUID id, String type, String name, Set<UUID> ownerIds);

}
