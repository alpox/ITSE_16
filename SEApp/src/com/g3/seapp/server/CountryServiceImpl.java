package com.g3.seapp.server;

import java.util.ArrayList;
import java.util.List;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
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
}
