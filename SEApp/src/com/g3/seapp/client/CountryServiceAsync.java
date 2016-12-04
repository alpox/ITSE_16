package com.g3.seapp.client;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The asynchronous interface for the CountryService.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Defines an interface for gathering the weatherdata.
 */
@RemoteServiceRelativePath("countries")
public interface CountryServiceAsync {
	void getMeasurements(int start, int end, Measurement.MeasurementType sortCol, boolean asc, HashMap<Measurement.MeasurementType, String> filters,
						 AsyncCallback<ArrayList<Measurement>> callback);
	void getNames(Measurement.MeasurementType type, AsyncCallback<ArrayList<String>> callback);
	void getMeasurementEntrySize(HashMap<Measurement.MeasurementType, String> filters, AsyncCallback<Integer> callback);
	void getAverageTempOfYear(int year, AsyncCallback<HashMap<String, Float>> callback);
    void getAggregation(Measurement.MeasurementType measType, Measurement.AggregationType aggType, AsyncCallback<Float> async);

    void getAverageTempOfYearPerCity(int year, AsyncCallback<HashMap<String, Measurement>> async);
}
