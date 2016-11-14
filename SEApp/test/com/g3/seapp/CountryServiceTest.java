package com.g3.seapp;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.server.CountryServiceImpl;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.core.shared.GWT;
import com.ibm.icu.impl.Assert;

public class CountryServiceTest {
	
	private final CountryService countryService = new CountryServiceImpl();

	@Test
	public void testGetsDataFromService() {
		List<Measurement> countries = countryService.getMeasurements(0, 20, "", true);
		Assert.assrt("Result is not null", countries != null);
		Assert.assrt("Result is not empty", countries.size() > 0);
	}

}
