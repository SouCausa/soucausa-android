package com.br.model;

public class Causa {
	private Integer id;
	private String description;
	private Integer resource;
	
	public Causa(Integer id, Integer resource, String description) {
		this.id = id;
		this.resource = resource;
		this.description = description;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getResource() {
		return resource;
	}
	public void setResource(Integer resource) {
		this.resource = resource;
	}

	
}
