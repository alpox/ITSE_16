package com.g3.seapp.shared;

import java.io.Serializable;
import java.util.ArrayList;

public class Country implements Serializable {
	private String name;
	private ArrayList<String> cityNames;
	private ArrayList<Measurement> measurements;
	
	public Country() {}
	
	public Country(String name, ArrayList<Measurement> measurements) {
		this.name = name;
		this.measurements = measurements;

		cityNames = new ArrayList<>();
		for(Measurement measurement : measurements)
			if(!cityNames.contains(measurement.getCity()))
				cityNames.add(measurement.getCity());
	}
	
	public String getName() {
		return name;
	}

	public ArrayList<String> getCityNames() { return cityNames; }
	
	public ArrayList<Measurement> getMeasurements() {
		return measurements;
	}
}
