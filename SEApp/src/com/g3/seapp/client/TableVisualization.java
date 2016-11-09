package com.g3.seapp.client;

import org.mortbay.log.Log;

import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class TableVisualization implements IVisualization, IExportable {
	private FlexTable countryTable = new FlexTable();
	private CountryServiceAsync countryService = GWT.create(CountryService.class);
	
	private CountryCollection countries;

	@Override
	public void export() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Table Visualization";
	}

	@Override
	public void drawVisualization(final Panel container) {
		// TODO Auto-generated method stub
		AsyncCallback<CountryCollection> callback = new AsyncCallback<CountryCollection>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(CountryCollection result) {
				// TODO Auto-generated method stub
				countries = result;
				updateVisualization(container);
			}
		};
		
		countryService.getCountries(callback);
	}

	@Override
	public void updateVisualization(Panel container) {
		// TODO Auto-generated method stub
		countryTable.setText(0, 0, "Country");
		countryTable.setText(0, 1, "City");
		countryTable.setText(0, 2, "Average");
		countryTable.setText(0, 3, "Error");
		countryTable.setText(0, 4, "Latitude");
		countryTable.setText(0, 5, "Longitude");
		
		int row = 1;
		for(Country country : countries) {
			for(Measurement measurement : country.getMeasurements()) {
				countryTable.setText(row, 0, country.getName());
				countryTable.setText(row, 1, measurement.getCity());
				countryTable.setText(row, 2, String.valueOf(measurement.getAvg()));
				countryTable.setText(row, 3, String.valueOf(measurement.getError()));
				countryTable.setText(row, 4, String.valueOf(measurement.getCoords().getLat()));
				countryTable.setText(row, 5, String.valueOf(measurement.getCoords().getLon()));
				row++;
			}
		}
		container.add(countryTable);
	}

}
