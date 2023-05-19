package com.thomasvitale.instrumentservice.instrument;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;

@Entity
public class Instrument {
  
	@Id
	@GeneratedValue(strategy=GenerationType.UUID)
	private UUID id;

	@NotEmpty
	private String name;

	private String type;

	public Instrument() {}

	public Instrument(String name, String type) {
		this.name = name;
		this.type = type;
	}

	public Instrument(UUID id, String name, String type) {
		this.name = name;
		this.type = type;
	}

	public UUID getId() {
		return id;
	}

  	public void setId(UUID id) {
		this.id = id;
	}

  	public String getName() {
    	return name;
  	}

  	public void setName(String name) {
    	this.name = name;
  	}

  	public String getType() {
    	return type;
  	}

  	public void setType(String type) {
    	this.type = type;
  	}

}
