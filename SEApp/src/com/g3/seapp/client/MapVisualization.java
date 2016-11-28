package com.g3.seapp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;

import java.util.ArrayList;
import java.util.HashMap;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.core.shared.GWT;

import com.kiouri.sliderbar.client.solution.simplehorizontal.SliderBarSimpleHorizontal;

/**
 * This class represents a visualization of a Worldmap including
 * data displayed onto the different countries.
 * 
 * @author Thomas Huber
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities Shows a map visualization of the data.
 */
public class MapVisualization implements IVisualization, IExportable {
	
	private GeoChart geoChart;
	private CountryServiceAsync countryService = GWT.create(CountryService.class);
	
	@Override
	public void export() {
		// TODO Auto-generated method stub
		
	}

	/**
	Returns the name respectively type of the visualization as String
	@pre nothing
	@post nothing
	@return Returns the name of the visualization
	 **/
	public String getName() {
		return "Map Visualization";
	}

	/**
	Draws the initial visualization of the map
	@pre nothing
	@post nothing
	@param  container A Panel which contains the whole visualization
	 **/
	public void drawVisualization(final Panel container) {
		
		ChartLoader chartLoader = new ChartLoader(ChartPackage.GEOCHART);
		chartLoader.loadApi(new Runnable() {


			public void run() {
				// Create and attach the chart to the panel
				geoChart = new GeoChart();
				container.add(geoChart);
				updateVisualization(container);
			}
		});
		//Set size constraints
		container.setHeight("70vh");
		container.setWidth("70vw");

		SliderBarSimpleHorizontal slider = new SliderBarSimpleHorizontal(20, "200px", true);
		container.add(slider);
	}

	/**
	Updates the visualization and displays the map and the requested data
	@pre nothing
	@post nothing
	@param  container A Panel which contains the whole visualization
	 **/
	public void updateVisualization(Panel container) {
		int year = 1833;
		// Prepare the datatable
		final DataTable dataTable = DataTable.create();
		// Set options
		final GeoChartOptions options = GeoChartOptions.create();
		GeoChartColorAxis geoChartColorAxis = GeoChartColorAxis.create();
		geoChartColorAxis.setColors("green", "yellow", "red");
		options.setColorAxis(geoChartColorAxis);
		options.setDatalessRegionColor("Grey");
		
		AsyncCallback<HashMap<String, Float>> callback = new AsyncCallback<HashMap<String, Float>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<String, Float> result) {
				for(String country : result.keySet()){
					dataTable.addRow();
					dataTable.setValue(dataTable.getNumberOfRows()-1, 0, country);
					dataTable.setValue(dataTable.getNumberOfRows()-1, 1, result.get(country));
				}
				
				// Draw the chart
				geoChart.draw(dataTable, options);
			}
		};
		
		countryService.getAverageTempOfYear(year, callback);
		
		
		dataTable.addColumn(ColumnType.STRING, "Country");
		dataTable.addColumn(ColumnType.NUMBER, "Average Temperature");
		

		// Draw the chart
		geoChart.draw(dataTable, options);
		
		//Panel  for the visualization of the copyright
		HorizontalPanel footer = new HorizontalPanel();
		Label lblFooter = new Label();
		lblFooter.setText("Copyright Data source K. Meier");
		footer.add(lblFooter);
		container.add(footer);
	}

}
