package com.g3.seapp.server;

import com.g3.seapp.shared.Coordinate;
import com.g3.seapp.shared.Measurement;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
	private static ArrayList<Measurement> measurements;

	public static ArrayList<Measurement> getMeasurements() {
		if(measurements == null)
			return new ArrayList<Measurement>();
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
    	ArrayList<Measurement> allMeasurements = new ArrayList<Measurement>();

        try {
        	String country = "";
        	String city = "";
        	ArrayList<Measurement> measurements = new ArrayList<Measurement>();
        	
            br = new BufferedReader(new FileReader(csvFile));
            br.readLine();
            while ((line = br.readLine()) != null) {
				lineNumber++;
				if (line.trim().isEmpty()) continue;

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
					float lat = Float.parseFloat(measurement[5].substring(0, measurement[5].length() - 1));
					float lon = Float.parseFloat(measurement[6].substring(0, measurement[6].length() - 1));

					// If we get south, we change to minus
					if (measurement[5].charAt(measurement[5].length() - 1) == 'S')
						lat = lat * -1;

					// If we get west, we change to minus
					if (measurement[6].charAt(measurement[6].length() - 1) == 'W')
						lon = lon * -1;

					Measurement newMeasurement = new Measurement(countryName, cityName, avg, error, new Coordinate(lat, lon), date);
					measurements.add(newMeasurement);
					allMeasurements.add(newMeasurement);
				} catch (Exception ex) {
					System.out.println("Was not able to parse line " + lineNumber + ": " + line + "\n Error: " + ex.getMessage());
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

        DataManager.measurements = allMeasurements;
        System.gc();
	}
}
