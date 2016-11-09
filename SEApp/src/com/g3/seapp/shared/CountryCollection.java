package com.g3.seapp.shared;

import java.util.ArrayList;
import java.util.List;

public class CountryCollection extends ArrayList<Country> {
	
	public CountryCollection() {}
	public CountryCollection(List<Country> countries) {
		super(countries);
	}
}
