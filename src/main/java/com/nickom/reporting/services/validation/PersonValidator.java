package com.nickom.reporting.services.validation;

import com.nickom.reporting.models.Person;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface PersonValidator {

    void ensurePersonAndTenant(UUID businessId, Optional<Person> optionalPerson);

    void ensureSelfAndSubordinate(UUID id, Collection<UUID> subordinateIds);

    void ensureManagerAndSubordinate(Collection<UUID> managerIds, Collection<UUID> subordinateIds);
}
