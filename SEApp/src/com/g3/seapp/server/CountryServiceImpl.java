package com.g3.seapp.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jetty.util.log.Log;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The implementation of the CountryService.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Defines an endpoint for weatherdata.
 */
@SuppressWarnings("serial")
public class CountryServiceImpl extends RemoteServiceServlet implements CountryService {
	
	/**
	 * Sorts the given measurements list
	 * 
	 * @param measurements The measurements list to sort
	 * @param col The column description to sort for
	 * @param desc Is descending sort (false = ascending)
	 * @pre measurements != null
	 * @post measurements is sorted for the given column
	 * 
	 * @return nothing
	 */
	private void sortMeasurements(ArrayList<Measurement> measurements, String col, final boolean desc) {
		switch(col.toLowerCase()) {
		case "date":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			        int comp = s1.getDate().compareTo(s2.getDate());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "country": 
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			        int comp = s1.getCountry().compareTo(s2.getCountry());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "city":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = s1.getCity().compareTo(s2.getCity());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "average":
		case "avg":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getAvg(), s2.getAvg());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "error":
		case "err":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getError(), s2.getError());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "latitude":
		case "lat":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLat(), s2.getCoords().getLat());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		case "longitude":
		case "long":
		case "lon":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLon(), s2.getCoords().getLon());
			        return !desc ? comp : comp * -1;
			    }
			});
			break;
		}
	}

	/**
	 * Returns all the measurements from start to end,
	 * sorted for sortCol.
	 * 
	 * @param start Start of the measurement data range
	 * @param end End of the measurement data range
	 * @param sortCol The column descriptor for which the data should be sorted for
	 * @param desc Boolean - True if sort should be descending, false otherwise
	 * @pre end > start
	 * @post DataManager.getData() is sorted for sortCol
	 * 
	 * @return An array of measurements
	 */
	@Override
	public ArrayList<Measurement> getMeasurements(int start, int end, String sortCol, boolean asc) {//, ArrayList<String> sortColumns, ArrayList<Boolean> ascList) {
		// Create flat measurement list
		ArrayList<Measurement> range = new ArrayList<Measurement>();
		ArrayList<Measurement> measurements = DataManager.getMeasurements();
		
		if(sortCol != null && !sortCol.isEmpty())
			sortMeasurements(measurements, sortCol, asc);
		
		for(int i = start; i < end; i++)
			range.add(measurements.get(i));
		
		return range;
	}
}
