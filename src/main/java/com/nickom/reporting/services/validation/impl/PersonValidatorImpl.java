package com.nickom.reporting.services.validation.impl;

import com.nickom.reporting.models.Person;
import com.nickom.reporting.services.validation.PersonValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonValidatorImpl implements PersonValidator {
    @Override
    public void ensurePersonAndTenant(UUID businessId, Optional<Person> optionalPerson) {
        if (!optionalPerson.isPresent() || !businessId.equals(optionalPerson.get().getBusinessId())) {
            throw new RuntimeException("report.person.not.found");
        }
    }

    @Override
    public void ensureSelfAndSubordinate(UUID id, Collection<UUID> subordinateIds) {
        if (CollectionUtils.isEmpty(subordinateIds)) {
            throw new IllegalArgumentException("report.subordinate.cannot.be.empty");
        }
        if (subordinateIds.contains(id)) {
            throw new IllegalArgumentException("report.subordinate.cannot.be.self");
        }
    }

    @Override
    public void ensureManagerAndSubordinate(Collection<UUID> managerIds, Collection<UUID> subordinateIds) {
        if (CollectionUtils.containsAny(managerIds, subordinateIds)) {
            throw new IllegalArgumentException("report.subordinate.cannot.be.manager");
        }
    }
}
