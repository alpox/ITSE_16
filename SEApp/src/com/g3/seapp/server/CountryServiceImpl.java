package com.g3.seapp.server;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;

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
	
	private void filterMeasurements(ArrayList<Measurement> measurements, HashMap<String, String> filters) {
		ArrayList<Integer> toDelete = new ArrayList<Integer>();
		
		SimpleDateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd");
		
		for(int i = 0; i < measurements.size(); i++)
		for(String filter : filters.keySet())
			try {
			switch(filter) {
			case "date":
				if(measurements.get(i).getDate().compareTo(dateParser.parse(filters.get(filter))) != 0)
					toDelete.add(i);
				break;
			case "country": 
				if(!measurements.get(i).getCountry().toLowerCase().contains(filters.get(filter).toLowerCase()))
					toDelete.add(i);
				break;
			case "city":
				if(!measurements.get(i).getCity().toLowerCase().contains(filters.get(filter).toLowerCase()))
					toDelete.add(i);
				break;
			case "avg":
			case "average":
				if(Float.compare(measurements.get(i).getAvg(), Float.parseFloat(filters.get(filter))) != 0)
					toDelete.add(i);
				break;
			case "err":
			case "error":
				if(Float.compare(measurements.get(i).getError(), Float.parseFloat(filters.get(filter))) != 0)
					toDelete.add(i);
				break;
			case "lat":
			case "latitude":
				if(Float.compare(measurements.get(i).getCoords().getLat(), Float.parseFloat(filters.get(filter))) != 0)
					toDelete.add(i);
				break;
			case "lon":
			case "longitude":
				if(Float.compare(measurements.get(i).getCoords().getLon(), Float.parseFloat(filters.get(filter))) != 0)
					toDelete.add(i);
				break;
			}
			} catch(Exception ex) {
				System.out.println("Was not able to parse input: '" + filters.get(filter) + "'.");
			}
		
		for(Integer idx : toDelete)
			measurements.remove(idx);
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
	public ArrayList<Measurement> getMeasurements(int start, int end, String sortCol, boolean asc, HashMap<String, String> filters) {
		ArrayList<Measurement> measurements = DataManager.getMeasurements();
		
		if(measurements == null) return new ArrayList<Measurement>();
		
		if(sortCol != null && !sortCol.isEmpty())
			sortMeasurements(measurements, sortCol, asc);
		
		if(filters != null && !filters.isEmpty())
			filterMeasurements(measurements, filters);

		return new ArrayList<Measurement>(measurements.subList(start, end));
	}
}
