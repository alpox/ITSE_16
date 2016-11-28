package com.g3.seapp.client;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The synchronous interface for the CountryService.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Defines an interface for gathering the weatherdata.
 */
@RemoteServiceRelativePath("countries")
public interface CountryService extends RemoteService {
	ArrayList<Measurement> getMeasurements(int start, int end, Measurement.MeasurementType sortCol, boolean asc,
										   HashMap<Measurement.MeasurementType, String> filters);
	ArrayList<String> getNames(Measurement.MeasurementType type);
	Integer getMeasurementEntrySize(HashMap<Measurement.MeasurementType, String> filters);
	HashMap<String, Float> getAverageTempOfYear(int year);
}