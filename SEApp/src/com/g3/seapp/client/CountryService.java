package com.g3.seapp.client;

import com.g3.seapp.shared.CountryCollection;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("countries")
public interface CountryService extends RemoteService {
	CountryCollection getCountries();
}