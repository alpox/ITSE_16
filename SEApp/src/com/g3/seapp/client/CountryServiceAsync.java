package com.g3.seapp.client;

import com.g3.seapp.shared.CountryCollection;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CountryServiceAsync {
	void getCountries(AsyncCallback<CountryCollection> callback);
}
