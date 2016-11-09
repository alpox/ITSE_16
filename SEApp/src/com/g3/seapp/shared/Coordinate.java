package com.g3.seapp.shared;

import java.io.Serializable;

public class Coordinate implements Serializable {
	private float lat;
	private float lon;
	
	public Coordinate() {}
	
	public Coordinate(float lat, float lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	public float getLat() {
		return lat;
	}
	
	public float getLon() {
		return lon;
	}
}
