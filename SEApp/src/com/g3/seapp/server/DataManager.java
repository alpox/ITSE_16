package com.g3.seapp.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.g3.seapp.shared.Coordinate;
import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;

/**
 * A DataManager for reading the weatherdata from
 * a csv.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Reads, caches and returns weatherdata from a
 *  csv.
 */
public class DataManager {
	private static CountryCollection countryCollection;
	private static ArrayList<Measurement> measurements;
	
	public static CountryCollection getCountryCollection() {
		return countryCollection;
	}
	
	public static ArrayList<Measurement> getMeasurements() {
		return measurements;
	}
	
	/**
	 * Gets the data from the csv
	 * @pre csv file GlobalLandTemperaturesByMajorCity_v1.csv exists in directory war/
	 * @post File reader is released
	 * @return A collection of countries which hold measurement data
	 */
	public static final void loadData() {
		
		// Set name of csv
		String csvFile = "GlobalLandTemperaturesByMajorCity_v1.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int lineNumber = 1;
        
        // Define a collection to collect the countries.
        CountryCollection countries = new CountryCollection();
    	ArrayList<Measurement> allMeasurements = new ArrayList<Measurement>();

        try {
        	String country = "";
        	String city = "";
        	ArrayList<Measurement> measurements = new ArrayList<Measurement>();
        	
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
            	lineNumber++;
            	if(line.trim().isEmpty()) continue;

                // use comma as separator
                String[] measurement = line.split(cvsSplitBy);
                SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
                
                try {
                	// Read the data
	                Date date = dateParser.parse(measurement[0]);
	                float avg = Float.parseFloat(measurement[1]);
	                float error = Float.parseFloat(measurement[2]);
	                String cityName = measurement[3];
	                String countryName = measurement[4];
	                float lat = Float.parseFloat(measurement[5].substring(0, measurement[5].length()-1));
	                float lon = Float.parseFloat(measurement[6].substring(0, measurement[6].length()-1));
	                
	                // If we get south, we change to minus
	                if(measurement[5].charAt(measurement[5].length() - 1) == 'S')
	                	lat = lat * -1;
	                
	                // If we get west, we change to minus
	                if(measurement[6].charAt(measurement[6].length() - 1) == 'W')
	                	lon = lon * -1;
	                
	                if(!countryName.equals(country)) {
	                	if(measurements.size() > 0) { // Don't add first
		                	Country countryInstance = new Country(country, measurements);
		                	countries.add(countryInstance);
	                	}
	                	measurements = new ArrayList<Measurement>();

	                	country = countryName;
	                	city = cityName;
	                }
	                
	                Measurement newMeasurement = new Measurement(countryName, cityName, avg, error, new Coordinate(lat, lon), date);
	                measurements.add(newMeasurement);
	                allMeasurements.add(newMeasurement);
                }
                catch(Exception ex) {
                	System.out.println("Was not able to parse line " + lineNumber + ": " + line + "\n Error: " + ex.getMessage());
                }
            }
            
            Country countryInstance = new Country(country, measurements);
        	countries.add(countryInstance);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
        DataManager.countryCollection = countries;
        DataManager.measurements = allMeasurements;
	}
}
