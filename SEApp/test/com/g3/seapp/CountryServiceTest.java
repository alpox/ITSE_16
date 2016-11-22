package com.g3.seapp;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.server.CountryServiceImpl;
import com.g3.seapp.server.DataManager;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.core.shared.GWT;
import com.ibm.icu.impl.Assert;

public class CountryServiceTest {
	
	private final CountryService countryService = new CountryServiceImpl();
	
	@BeforeClass
	public static void initialize() {
		DataManager.loadData();
	}

	@Test
	public void testGetsDataFromService() {
		List<Measurement> countries = countryService.getMeasurements(0, 20, "", true);
		Assert.assrt("Result is not null", countries != null);
		Assert.assrt("Result is not empty", countries.size() > 0);
	}
	
	@Test
	public void testGetsRangeFromService() {
		List<Measurement> measurementRange = countryService.getMeasurements(10, 20, "", true);
		Assert.assrt("Result is not null", measurementRange != null);
		Assert.assrt("Result has right amount", measurementRange.size() == 10);
	}

	@Test
	public void testCanSortStringAscending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, "country", false);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for ascending sort is Afghanistan.
			Assert.assrt(measurement.getCountry().equals("Afghanistan"));
		}
	}
	
	@Test
	public void testCanSortStringDescending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, "country", true);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for descending sort is Zimbabwe.
			Assert.assrt(measurement.getCountry().equals("Zimbabwe"));
		}
	}
	
	@Test
	public void testCanSortFloatAscending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, "avg", false);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, "avg", false);

		Assert.assrt(first.get(0).getAvg() == -26.772f);
		Assert.assrt(last.get(0).getAvg() == 38.283f);
	}
	
	@Test
	public void testCanSortFloatDescending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, "avg", true);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, "avg", true);

		Assert.assrt(first.get(0).getAvg() == 38.283f);
		Assert.assrt(last.get(0).getAvg() == -26.772f);
	}
	
	@Test
	public void testCanSortDateAscending() throws ParseException {
		List<Measurement> first = countryService.getMeasurements(0, 1, "date", false);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, "date", false);
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		Date firstRightDate = dateParser.parse("1743-11-01");
		Date lastRightDate = dateParser.parse("2013-09-01");

		Assert.assrt(first.get(0).getDate().equals(firstRightDate));
		Assert.assrt(last.get(0).getDate().equals(lastRightDate));
	}
	
	@Test
	public void testCanSortDateDescending() throws ParseException {
		List<Measurement> first = countryService.getMeasurements(0, 1, "date", true);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, "date", true);
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		Date firstRightDate = dateParser.parse("2013-09-01");
		Date lastRightDate = dateParser.parse("1743-11-01");

		Assert.assrt(first.get(0).getDate().equals(firstRightDate));
		Assert.assrt(last.get(0).getDate().equals(lastRightDate));
	}
}
