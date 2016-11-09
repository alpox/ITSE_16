package com.g3.seapp.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {
	private String name;
	private ArrayList<Measurement> measurements;
	
	public Country() {}
	
	public Country(String name, ArrayList<Measurement> measurements) {
		this.name = name;
		this.measurements = measurements;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}
}
