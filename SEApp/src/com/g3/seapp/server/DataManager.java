package com.g3.seapp.server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mortbay.log.Log;

import com.g3.seapp.shared.Coordinate;
import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;

public class DataManager {
	private static CountryCollection dataCache;
	
	public static final CountryCollection getData() {
		if(dataCache != null) return dataCache;
		
		String csvFile = "GlobalLandTemperaturesByMajorCity_v1.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int lineNumber = 1;
        
        CountryCollection countries = new CountryCollection();

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
	                Date date = dateParser.parse(measurement[0]);
	                float avg = Float.parseFloat(measurement[1]);
	                float error = Float.parseFloat(measurement[2]);
	                String cityName = measurement[3];
	                String countryName = measurement[4];
	                float lat = Float.parseFloat(measurement[5].substring(0, measurement[5].length()-1));
	                float lon = Float.parseFloat(measurement[6].substring(0, measurement[6].length()-1));
	                
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
                }
                catch(Exception ex) {
                	Log.warn("Was not able to parse line " + lineNumber + ": " + line + "\n Error: " + ex.getMessage());
                }
            }

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
        
        DataManager.dataCache = countries;
        return countries;
	}
}
