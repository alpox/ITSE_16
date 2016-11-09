package com.g3.seapp.shared;

public class Coordinate {
	private float lat;
	private float lon;
	
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
