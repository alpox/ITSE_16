package com.g3.seapp.shared;

import java.util.Date;
import java.util.List;

public class Measurement {
	private String city;
	private float avg;
	private float error;
	private Coordinate coord;
	private Date date;
	
	public Measurement(String city, float avg, float err, Coordinate coord, Date date) {
		this.city = city;
		this.avg = avg;
		this.error = err;
		this.coord = coord;
		this.date = date;
	}
	
	public String getCity() {
		return city;
	}
	
	private float getAvg() {
		return avg;
	}
	
	private float getError() {
		return error;
	}
	
	private Coordinate getCoords() {
		return coord;
	}
	
	private Date getDate() {
		return date;
	}
}
