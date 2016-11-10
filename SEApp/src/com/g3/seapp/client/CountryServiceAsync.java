package com.g3.seapp.client;

import java.util.ArrayList;

import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("countries")
public interface CountryServiceAsync {
	void getCountries(AsyncCallback<CountryCollection> callback);
	void getMeasurements(int start, int end, String sortColumn, boolean asc,
			AsyncCallback<ArrayList<Measurement>> callback);
}
