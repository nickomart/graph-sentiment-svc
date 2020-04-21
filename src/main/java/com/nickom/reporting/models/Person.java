package com.nickom.reporting.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nickom.reporting.common.RandomUUIDStrategy;
import com.nickom.reporting.common.UUIDConverter;
import org.neo4j.ogm.annotation.*;
import org.neo4j.ogm.annotation.typeconversion.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NodeEntity
public class Person {

    @Id
    @GeneratedValue(strategy = RandomUUIDStrategy.class)
	@Convert(value = UUIDConverter.class)
	private UUID id;

	@Index(unique = true)
    private String email;

    private String name;

	@Convert(value = UUIDConverter.class)
    private UUID businessId;

	public Person() {
	}

	public Person(UUID id, UUID businessId, String name, String email) {
		this.id = id;
		this.businessId = businessId;
		this.name = name;
		this.email = email;
	}

	public Person(UUID businessId, String name, String email) {
		this(null, businessId, name, email);
	}

	@JsonIgnoreProperties({"managers", "subordinates"})
	@Relationship(type = "REPORTING_TO")
	private List<Person> managers = new ArrayList<>();

	@JsonIgnoreProperties({"managers", "subordinates"})
	@Relationship(type = "SUPERVISING")
	private List<Person> subordinates = new ArrayList<>();

	// @JsonIgnoreProperties({"managers", "subordinates"})
	// @Relationship(type = "SENTIMENT_TO")
	// private List<Person> authored = new ArrayList<>();

	//@Relationship(type = "SENTIMENT_TO", direction = Relationship.INCOMING)
	@JsonIgnoreProperties({"author"})
	@Relationship(type = "SENTIMENT_TO")
	private List<SentimentAnalysis> sentiments;

	public void manage(Person person) {
		subordinates.add(person);
		person.managers.add(this);
	}

	//public void authored(Person person) {
	//	authored.add(person);
	//}

	public UUID getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getName() {
		return name;
	}

	public UUID getBusinessId() {
		return businessId;
	}

	public List<Person> getManagers() {
		return managers;
	}

	public List<Person> getSubordinates() {
		return subordinates;
	}

	public List<SentimentAnalysis> getSentiments() {
		return sentiments;
	}

	public void setSentiments(List<SentimentAnalysis> sentiments) {
		this.sentiments = sentiments;
	}
}