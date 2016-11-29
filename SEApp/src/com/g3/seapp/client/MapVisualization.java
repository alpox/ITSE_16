package com.g3.seapp.client;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.widgetideas.client.SliderBar;
import com.googlecode.gwt.charts.client.ChartLoader;
import com.googlecode.gwt.charts.client.ChartPackage;
import com.googlecode.gwt.charts.client.ColumnType;
import com.googlecode.gwt.charts.client.DataTable;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;

import java.util.HashMap;

import com.google.gwt.core.shared.GWT;

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

	private final int MIN_YEAR = 1743;
	private final int MAX_YEAR = 2013;

	private DataTable dataTable;
	private GeoChartOptions options;
	
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

		//Panel  for the visualization of the copyright
		HorizontalPanel footer = new HorizontalPanel();
		Label lblFooter = new Label();
		lblFooter.setText("Copyright Data source K. Meier");
		footer.add(lblFooter);
		container.add(footer);
	}

	public void SetupSlider(Panel container) {
		int numYears = MAX_YEAR - MIN_YEAR;

		FlowPanel hpanel = new FlowPanel();
		hpanel.getElement().setId("slider-panel");

		final TextBox yearTxt = new TextBox();
		yearTxt.getElement().setId("slider-box");
		yearTxt.setMaxLength(4);
		yearTxt.setText(String.valueOf(MIN_YEAR));

		final SliderBar slider = new SliderBar(MIN_YEAR, MAX_YEAR);

		slider.setNumLabels(numYears / 15);
		slider.setNumTicks(numYears / 15);
		slider.setStepSize(1);
		slider.setCurrentValue(MIN_YEAR);

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				int year = (int)slider.getCurrentValue();
				yearTxt.setText(String.valueOf(year));
				updateWithYear(year);
			}
		});

		yearTxt.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				String val = yearTxt.getValue();
				char last = val.charAt(val.length()-1);
				if(last < '0' || last > '9')
					val = val.substring(0, val.length()-1);

				if(val.length() == 4) {
					double yearVal = Double.valueOf(val);

					if(yearVal > MAX_YEAR) yearVal = MAX_YEAR;
					else if(yearVal < MIN_YEAR) yearVal = MIN_YEAR;

					val = String.valueOf(yearVal);
					slider.setCurrentValue(yearVal);
				}

				yearTxt.setText(val);
			}
		});

		hpanel.add(slider);
		hpanel.add(yearTxt);
		container.add(hpanel);
	}

	public void updateWithYear(int year) {
		AsyncCallback<HashMap<String, Float>> callback = new AsyncCallback<HashMap<String, Float>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<String, Float> result) {
				dataTable.removeRows(0, dataTable.getNumberOfRows());
				for(String country : result.keySet()){
					dataTable.addRow();
					dataTable.setValue(dataTable.getNumberOfRows()-1, 0, country);
					dataTable.setValue(dataTable.getNumberOfRows()-1, 1, result.get(country));
				}

				// Draw the chart
				geoChart.redraw();
			}
		};

		countryService.getAverageTempOfYear(year, callback);
	}

	/**
	Updates the visualization and displays the map and the requested data
	@pre nothing
	@post nothing
	@param  container A Panel which contains the whole visualization
	 **/
	public void updateVisualization(Panel container) {
		dataTable = DataTable.create();
		options = GeoChartOptions.create();

		// Set options
		GeoChartColorAxis geoChartColorAxis = GeoChartColorAxis.create();
		geoChartColorAxis.setColors("Gold", "red");
		options.setColorAxis(geoChartColorAxis);
		options.setDatalessRegionColor("Lightgrey");

		dataTable.addColumn(ColumnType.STRING, "Country");
		dataTable.addColumn(ColumnType.NUMBER, "Average Temperature");

		// Draw the chart
		geoChart.draw(dataTable, options);
		geoChart.getElement().setId("geo-chart");
		geoChart.setHeight("85vh");
		geoChart.setWidth("80%");

		updateWithYear(MIN_YEAR);

		SetupSlider(container);
	}

}
