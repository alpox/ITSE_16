package com.g3.seapp;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.server.CountryServiceImpl;
import com.g3.seapp.server.DataManager;
import com.g3.seapp.shared.Measurement;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.g3.seapp.shared.Measurement.MeasurementType.*;

/**
 * Defines tests for the CountryServiceImpl class
 */
public class CountryServiceTest {
	
	private final CountryService countryService = new CountryServiceImpl();

	/**
	 * Initialize the data for testing
	 */
	@BeforeClass
	public static void initialize() {
		DataManager.loadData();
	}

	/**
	 * Tests if one can retrieve data from the service
	 */
	@Test
	public void testGetsDataFromService() {
		List<Measurement> countries = countryService.getMeasurements(0, 20, null, true, null);
		Assert.assertTrue("Result is not null", countries != null);
		Assert.assertTrue("Result is not empty", countries.size() > 0);
	}

	/**
	 * Tests if one can retrieve a specific datarange from the service (Indices)
	 */
	@Test
	public void testGetsRangeFromService() {
		List<Measurement> measurementRange = countryService.getMeasurements(10, 20, null, true, null);
		Assert.assertTrue("Result is not null", measurementRange != null);
		Assert.assertTrue("Result has right amount", measurementRange.size() == 10);
	}

	/**
	 * Tests if one can sort a string in ascending order
	 */
	@Test
	public void testCanSortStringAscending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, COUNTRY, false, null);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for ascending sort is Afghanistan.
			Assert.assertTrue(measurement.getCountry().equals("Afghanistan"));
		}
	}

	/**
	 * Tests if one can sort a string in descending order
	 */
	@Test
	public void testCanSortStringDescending() {
		List<Measurement> measurementRange = countryService.getMeasurements(0, 10, COUNTRY, true, null);
		
		for(Measurement measurement : measurementRange) {
			// In our dataset the uppermost for descending sort is Zimbabwe.
			Assert.assertTrue(measurement.getCountry().equals("Zimbabwe"));
		}
	}

	/**
	 * Tests if one can sort a float in ascending order
	 */
	@Test
	public void testCanSortFloatAscending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, AVG, false, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, AVG, false, null);

		Assert.assertTrue(first.get(0).getAvg() == -26.772f);
		Assert.assertTrue(last.get(0).getAvg() == 38.283f);
	}

	/**
	 * Tests if one can sort a float in descending order
	 */
	@Test
	public void testCanSortFloatDescending() {
		List<Measurement> first = countryService.getMeasurements(0, 1, AVG, true, null);
		List<Measurement> last = countryService.getMeasurements(228174, 228175, AVG, true, null);

		Assert.assertTrue(first.get(0).getAvg() == 38.283f);
		Assert.assertTrue(last.get(0).getAvg() == -26.772f);
	}

	/**
	 * Tests if one can sort a date in ascending order
	 * @throws ParseException An exception which is thrown if a date cannot be parsed
	 */
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

	/**
	 * Tests if one can sort a date in descending order
	 * @throws ParseException An exception which is thrown if a date cannot be parsed
	 */
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

	/**
	 * Tests if one can get the data size
	 */
	@Test
	public void testCanGetDataSize() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(COUNTRY, "France");
		int size = countryService.getMeasurementEntrySize(filters);

		Assert.assertEquals(size, 3166);
	}

	/**
	 * Tests if one can filter for a specific country
	 */
	@Test
	public void testCanFilterForCountry() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(COUNTRY, "France");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertEquals(measurement.getCountry(), "France");
	}

	/**
	 * Tests if one can filter for a specific city
	 */
	@Test
	public void testCanFilterForCity() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(CITY, "Kabul");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertEquals(measurement.getCity(), "Kabul");
	}

	/**
	 * Tests if one can filter for a specific average
	 */
	@Test
	public void testCanFilterForAvg() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(AVG, "2.081");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(Float.compare(measurement.getAvg(), 2.081f) == 0);
	}

	/**
	 * Tests if one can filter for a specific error
	 */
	@Test
	public void testCanFilterForError() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(ERROR, "1.749");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(Float.compare(measurement.getError(), 1.749f) == 0);
	}

	/**
	 * Tests if one can filter for an error limitation
	 */
	@Test
	public void testCanFilterForSmallerThanError() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(ERROR, "<2.5");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(measurement.getError() <= 2.5);
	}

	/**
	 * Tests if one can filter for a specific latitude
	 */
	@Test
	public void testCanFilterForLatitude() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(LAT, "49.03");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(Float.compare(measurement.getCoords().getLat(), 49.03f) == 0);
	}

	/**
	 * Test if one can filter for a specific longitude
	 */
	@Test
	public void testCanFilterForLongitude() {
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(LON, "2.45");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(Float.compare(measurement.getCoords().getLon(), 2.45f) == 0);
	}

	/**
	 * Tests if one can filter for a specific date
	 * @throws ParseException An exception which is thrown if a date cannot be parsed
	 */
	@Test
	public void testCanFilterForDate() throws ParseException {
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
		filters.put(DATE, "1743-11-01");

		int size = countryService.getMeasurementEntrySize(filters);
		ArrayList<Measurement> measurements = countryService.getMeasurements(0, size-1, null, false, filters);

		for(Measurement measurement : measurements)
			Assert.assertTrue(measurement.getDate().compareTo(dateParser.parse("1743-11-01")) == 0);
	}

	/**
	 * Tests if one can get possible country names
	 */
	@Test
	public void testCanGetCountryNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.COUNTRY);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible city names
	 */
	@Test
	public void testCanGetCityNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.CITY);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible average values
	 */
	@Test
	public void testCanGetAvgNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.AVG);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible error values
	 */
	@Test
	public void testCanGetErrorNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.ERROR);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible date values
	 */
	@Test
	public void testCanGetDateNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.DATE);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible latitude values
	 */
	@Test
	public void testCanGetLatitudeNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.LAT);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get possible longitude values
	 */
	@Test
	public void testCanGetLongitudeNames() {
		ArrayList<String> names = countryService.getNames(Measurement.MeasurementType.LON);

		Assert.assertNotNull(names);
		Assert.assertTrue(names.size() > 0);
	}

	/**
	 * Tests if one can get the temperature average per country
	 */
	@Test
	public void testCanGetTemperatureAveragePerCountry() {
		HashMap<String, Float> avTemp = countryService.getAverageTempOfYear(1833);

		Assert.assertNotNull(avTemp);
		Assert.assertTrue(!avTemp.keySet().isEmpty());
	}

	/**
	 * Tests if one can get an aggregation over the average temperature
	 */
	@Test
	public void testCanGetAvgAggregation() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.AVG, Measurement.AggregationType.AVG);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 18.126062f) == 0);
	}

	/**
	 * Tests if one can get a maximum aggregation
	 */
	@Test
	public void testCanGetMaxAggregation() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.AVG, Measurement.AggregationType.MAX);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 38.283f) == 0);
	}

	/**
	 * Tests if one can get a minimum aggregation
	 */
	@Test
	public void testCanGetMinAggregation() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.AVG, Measurement.AggregationType.MIN);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, -26.772f) == 0);
	}

	/**
	 * Tests if one can get a median aggregation
	 */
	@Test
	public void testCanGetMedianAggregation() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.AVG, Measurement.AggregationType.MEDIAN);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 20.428f) == 0);
	}

	/**
	 * Test if one can get an error aggregation
	 */
	@Test
	public void testCanGetAggregationOfError() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.ERROR, Measurement.AggregationType.MAX);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 14.037f) == 0);
	}

	/**
	 * Tests if one can get a latitude aggregation
	 */
	@Test
	public void testCanGetAggregationOfLat() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.LAT, Measurement.AggregationType.MAX);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 60.27f) == 0);
	}

	/**
	 * Tests if one can get a longitude aggregation
	 */
	@Test
	public void testCanGetAggregationOfLon() {
		Float agg = countryService.getAggregation(Measurement.MeasurementType.LON, Measurement.AggregationType.MAX);

		Assert.assertNotNull(agg);
		Assert.assertTrue(Float.compare(agg, 151.78f) == 0);
	}
}
