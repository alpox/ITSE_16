package com.g3.seapp.shared;

import java.util.List;

public class Country {
	private String name;
	private List<Measurement> measurements;
	
	public Country(String name, List<Measurement> measurements) {
		this.name = name;
		this.measurements = measurements;
	}
	
	public String getName() {
		return name;
	}
	
	public List<Measurement> getMeasurements() {
		return measurements;
	}
}
