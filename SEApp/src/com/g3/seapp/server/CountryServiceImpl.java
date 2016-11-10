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
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CountryServiceImpl extends RemoteServiceServlet implements CountryService {
	@Override
	public CountryCollection getCountries() {
		// TODO Send data which is needed
		ArrayList<Country> countries = new ArrayList<Country>();
		countries.add(DataManager.getData().get(0));
		return new CountryCollection(countries);
	}
	
	private void sortMeasurements(ArrayList<Measurement> measurements, String col, final boolean asc) {
		switch(col.toLowerCase()) {
		case "country": 
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			        int comp = s1.getCountry().compareTo(s2.getCountry());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		case "city":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = s1.getCity().compareTo(s2.getCity());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		case "average":
		case "avg":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getAvg(), s2.getAvg());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		case "error":
		case "err":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getError(), s2.getError());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		case "latitude":
		case "lat":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLat(), s2.getCoords().getLat());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		case "longitude":
		case "long":
		case "lon":
			Collections.sort(measurements, new Comparator<Measurement>() {
			    public int compare(Measurement s1, Measurement s2) {
			    	int comp = Float.compare(s1.getCoords().getLon(), s2.getCoords().getLon());
			        return !asc ? comp : comp * -1;
			    }
			});
			break;
		}
	}

	@Override
	public ArrayList<Measurement> getMeasurements(int start, int end, String sortCol, boolean asc) {//, ArrayList<String> sortColumns, ArrayList<Boolean> ascList) {
		// Create flat measurement list
		ArrayList<Measurement> measurements = new ArrayList<Measurement>();
		ArrayList<Measurement> range = new ArrayList<Measurement>();
		
		CountryCollection countries = DataManager.getData();
		
		for(Country country : countries)
			measurements.addAll(country.getMeasurements());
		
		sortMeasurements(measurements, sortCol, asc);
		
		for(int i = start; i < end; i++)
			range.add(measurements.get(i));
		
		return range;
	}
}
