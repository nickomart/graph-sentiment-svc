package com.nickom.reporting.common;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.UUID;

public class UUIDConverter implements AttributeConverter<UUID, String> {
    @Override
    public String toGraphProperty(UUID value) {
        return value == null ? null : value.toString();
    }

    @Override
    public UUID toEntityAttribute(String value) {
        return value == null ? null : UUID.fromString(value);
    }
}
