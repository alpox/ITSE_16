package com.g3.seapp;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.server.CountryServiceImpl;
import com.g3.seapp.server.DataManager;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;

public class CountryServiceTest {
	
	private final CountryService countryService = new CountryServiceImpl();
	
	@BeforeClass
	public static void initialize() {
		DataManager.loadData();
	}

	@Test
	public void testGetsDataFromService() {
		List<Measurement> countries = countryService.getMeasurements(0, 20, null, true, null);
		Assert.assertTrue("Result is not null", countries != null);
		Assert.assertTrue("Result is not empty", countries.size() > 0);
	}
	
	@Test
	public void testGetsRangeFromService() {
		List<Measurement> measurementRange = countryService.getMeasurements(10, 20, null, true, null);
		Assert.assertTrue("Result is not null", measurementRange != null);
		Assert.assertTrue("Result has right amount", measurementRange.size() == 10);
	}

	@Test
	public void testCanSortStringAscending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, Measurement.MeasurementType.COUNTRY, false, null);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for ascending sort is Afghanistan.
			Assert.assertTrue(measurement.getCountry().equals("Afghanistan"));
		}
	}
	
	@Test
	public void testCanSortStringDescending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, Measurement.MeasurementType.COUNTRY, true, null);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for descending sort is Zimbabwe.
			Assert.assertTrue(measurement.getCountry().equals("Zimbabwe"));
		}
	}
	
	@Test
	public void testCanSortFloatAscending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, Measurement.MeasurementType.AVG, false, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, Measurement.MeasurementType.AVG, false, null);

		Assert.assertTrue(first.get(0).getAvg() == -26.772f);
		Assert.assertTrue(last.get(0).getAvg() == 38.283f);
	}
	
	@Test
	public void testCanSortFloatDescending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, Measurement.MeasurementType.AVG, true, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, Measurement.MeasurementType.AVG, true, null);

		Assert.assertTrue(first.get(0).getAvg() == 38.283f);
		Assert.assertTrue(last.get(0).getAvg() == -26.772f);
	}
	
	@Test
	public void testCanSortDateAscending() throws ParseException {
		List<Measurement> first = countryService.getMeasurements(0, 1, Measurement.MeasurementType.DATE, false, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, Measurement.MeasurementType.DATE, false, null);
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		Date firstRightDate = dateParser.parse("1743-11-01");
		Date lastRightDate = dateParser.parse("2013-09-01");

		Assert.assertTrue(first.get(0).getDate().equals(firstRightDate));
		Assert.assertTrue(last.get(0).getDate().equals(lastRightDate));
	}
	
	@Test
	public void testCanSortDateDescending() throws ParseException {
		List<Measurement> first = countryService.getMeasurements(0, 1, Measurement.MeasurementType.DATE, true, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, Measurement.MeasurementType.DATE, true, null);
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		Date firstRightDate = dateParser.parse("2013-09-01");
		Date lastRightDate = dateParser.parse("1743-11-01");

		Assert.assertTrue(first.get(0).getDate().equals(firstRightDate));
		Assert.assertTrue(last.get(0).getDate().equals(lastRightDate));
	}
}
