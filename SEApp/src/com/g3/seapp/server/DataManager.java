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
		String csvFile = "war/GlobalLandTemperaturesByMajorCity_v1.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";
        int lineNumber = 1;
        
        CountryCollection countries = new CountryCollection();

        try {
        	String country = "";
        	String city = "";
        	List<Measurement> measurements = new ArrayList<Measurement>();
        	
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
            	lineNumber++;

                // use comma as separator
                String[] measurement = line.split(cvsSplitBy);
                SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
                
                try {
	                Date date = dateParser.parse(measurement[0]);
	                float avg = Float.parseFloat(measurement[1]);
	                float error = Float.parseFloat(measurement[2]);
	                String cityName = measurement[3];
	                String countryName = measurement[4];
	                float lat = Float.parseFloat(measurement[5].substring(0, measurement[5].length()-2));
	                float lon = Float.parseFloat(measurement[6].substring(0, measurement[6].length()-2));
	                
	                if(countryName != country) {
	                	Country countryInstance = new Country(country, measurements);
	                	countries.add(countryInstance);

	                	country = countryName;
	                	city = cityName;
	                }
	                
	                Measurement newMeasurement = new Measurement(city, avg, error, new Coordinate(lat, lon), date);
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
