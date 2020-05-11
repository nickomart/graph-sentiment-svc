package com.nickom.reporting.small.common;

import com.nickom.reporting.common.RandomUUIDStrategy;
import com.nickom.reporting.common.UUIDConverter;
import com.nickom.reporting.models.Person;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.neo4j.ogm.id.IdStrategy;
import org.neo4j.ogm.typeconversion.AttributeConverter;

public class UUIDConverterTest {

  private IdStrategy idStrategy;

  private AttributeConverter<UUID, String> attributeConverter;

  @BeforeEach
  public void setup() {
    idStrategy = new RandomUUIDStrategy();
    attributeConverter = new UUIDConverter();
  }

  @Test
  public void givenRandomUUIDStrategy_whenGenerate_thenReturnRandomUUID() {
    Object uuid1 = idStrategy.generateId(null);
    Assertions.assertTrue(uuid1 instanceof UUID);

    Object uuid2 = idStrategy.generateId(new Person());
    Assertions.assertTrue(uuid2 instanceof UUID);
    Assertions.assertNotEquals(uuid1, uuid2);
  }

  @Test
  public void givenUUID_whenConvertToGraphProperty_thenReturnString() {
    UUID id = UUID.randomUUID();
    String strUuid = attributeConverter.toGraphProperty(id);

    Assertions.assertNotNull(strUuid);
    Assertions.assertEquals(id, UUID.fromString(strUuid));
  }

  @Test
  public void givenString_whenConvertEntity_thenReturnUUID() {
    UUID id = attributeConverter.toEntityAttribute("00000000-0000-0000-0000-000000000000");

    Assertions.assertNotNull(id);
    Assertions.assertEquals("00000000-0000-0000-0000-000000000000", id.toString());
  }
}
