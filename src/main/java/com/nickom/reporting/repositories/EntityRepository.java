package com.nickom.reporting.repositories;

import com.nickom.reporting.models.Entity;
import java.util.UUID;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EntityRepository extends Neo4jRepository<Entity, UUID> {

  Entity findByBusinessIdAndIdAndType(UUID businessId, UUID id, String type);

}
