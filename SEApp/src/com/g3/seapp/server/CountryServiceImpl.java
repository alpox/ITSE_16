package com.g3.seapp.server;

import com.g3.seapp.client.CountryService;
import com.g3.seapp.shared.CountryCollection;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class CountryServiceImpl extends RemoteServiceServlet implements CountryService {
	@Override
	public CountryCollection getCountries() {
		// TODO Auto-generated method stub
		return DataManager.getData();
	}
}
