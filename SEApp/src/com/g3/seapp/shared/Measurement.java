package com.g3.seapp.shared;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class Measurement implements Serializable {
	private String city;
	private float avg;
	private float error;
	private Coordinate coord;
	private Date date;
	
	public Measurement() {}
	
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
	
	public float getAvg() {
		return avg;
	}
	
	public float getError() {
		return error;
	}
	
	public Coordinate getCoords() {
		return coord;
	}
	
	public Date getDate() {
		return date;
	}
}
