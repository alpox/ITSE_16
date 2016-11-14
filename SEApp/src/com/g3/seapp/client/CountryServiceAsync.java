package com.g3.seapp.client;

import java.util.ArrayList;

import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The asynchronous interface for the CountryService.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @responsibilities 
 * 	Defines an interface for gathering the weatherdata.
 */
@RemoteServiceRelativePath("countries")
public interface CountryServiceAsync {
	/**
	 * Returns all the measurements from start to end,
	 * sorted for sortCol.
	 * 
	 * @param start Start of the measurement data range
	 * @param end End of the measurement data range
	 * @param sortCol The column descriptor for which the data should be sorted for
	 * @param desc Boolean - True if sort should be descending, false otherwise
	 * @param callback The callback which should be called with the return value
	 * 
	 * @pre end > start
	 * @post DataManager.getData() is sorted for sortCol
	 * 
	 * @return An array of measurements
	 */
	void getMeasurements(int start, int end, String sortColumn, boolean desc,
			AsyncCallback<ArrayList<Measurement>> callback);
}
