package com.g3.seapp.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

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
	void getMeasurements(int start, int end, String sortCol, boolean asc, HashMap<String, String> filters,
			AsyncCallback<ArrayList<Measurement>> callback);
}
