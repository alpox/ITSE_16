package com.g3.seapp.client;

import com.g3.seapp.shared.CountryCollection;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("countries")
public interface CountryServiceAsync {
	void getCountries(AsyncCallback<CountryCollection> callback);
}
