package com.nickom.reporting.repositories;

import com.nickom.reporting.models.SentimentData;
import java.util.List;
import java.util.UUID;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SentimentRepository extends MongoRepository<SentimentData, UUID> {

  List<SentimentData> findByBusinessIdAndIdIn(UUID businessId, List<UUID> ids);

}
