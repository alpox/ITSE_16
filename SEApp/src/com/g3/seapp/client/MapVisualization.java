package com.g3.seapp.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;


import com.google.gwt.dom.client.Style.Unit;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;

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
	}

	/**
	Updates the visualization and displays the map and the requested data
	@pre nothing
	@post nothing
	@param  container A Panel which contains the whole visualization
	 **/
	public void updateVisualization(Panel container) {

		// Prepare the datatable
		DataTable dataTable = DataTable.create();
		
		dataTable.addColumn(ColumnType.STRING, "Test");
		dataTable.addColumn(ColumnType.NUMBER, "Test");

		// Set options
		GeoChartOptions options = GeoChartOptions.create();
		options.setDatalessRegionColor("OliveDrab");

		// Draw the chart
		geoChart.draw(dataTable, options);
		
	}

}
