package com.g3.seapp.client;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.widgetideas.client.SliderBar;
import com.google.gwt.dom.client.Element;
import com.googlecode.gwt.charts.client.*;
import com.googlecode.gwt.charts.client.event.ReadyEvent;
import com.googlecode.gwt.charts.client.event.ReadyHandler;
import com.googlecode.gwt.charts.client.geochart.GeoChart;
import com.googlecode.gwt.charts.client.geochart.GeoChartColorAxis;
import com.googlecode.gwt.charts.client.geochart.GeoChartOptions;

import java.util.Date;
import java.util.HashMap;

import com.google.gwt.core.shared.GWT;
import com.googlecode.gwt.charts.client.options.DisplayMode;

/**
 * This class represents a visualization of a Worldmap including
 * data displayed onto the different countries.
 * 
 * @author Thomas Huber
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities Shows a map visualization of the data.
 */
public class MapVisualization implements IVisualization {

	private enum MapType {
		COUNTRY,
		CITY
	}
	
	private GeoChart countryChart;
	private GeoChart cityChart;
	private CountryServiceAsync countryService = GWT.create(CountryService.class);

	private final int MIN_YEAR = 1743;
	private final int MAX_YEAR = 2013;

	private DataTable countryData;
	private DataTable cityData;
	private GeoChartOptions countryOptions;
	private GeoChartOptions cityOptions;

	private Button leftBtn;
	private Button rightBtn;

	private int currentYear;

	private final int updateDelay = 500;

	private Anchor countryExportLink;

	private MapType currentMapType = MapType.COUNTRY;

	/**
	 * Update the link to hold the new data to export
	 */
	private void refreshExportData() {
		GeoChart chart = currentMapType == MapType.COUNTRY ? countryChart : cityChart;
		Element elem = chart.getElement().getElementsByTagName("svg").getItem(0);
		elem.setAttribute("xmlns", "http://www.w3.org/2000/svg");
		String svgData = outerHTML(elem);
		svgData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + svgData;
		String base64 = "data:image/svg+xml;base64,\n" + btoa(svgData);
		countryExportLink.getElement().setAttribute("href", base64);
	}

	/**
	 * Create a button for exporting the data
	 *
	 * @return The newly created button
	 */
	private Anchor createExportButton() {
		Anchor link = new Anchor();
		link.getElement().setClassName("export-button");
		link.getElement().setAttribute("href-lang", "image/svg+xml");
		link.getElement().setAttribute("download", "mapvisualization.svg");
		link.setText("Export");
		countryExportLink = link;
		return link;
	}

	/**
	 * Gets the outer HTML of an element.
	 *
	 * @param elem The DOM element
	 * @return The outer HTML of the given DOM element
	 */
	native String outerHTML(Element elem) /*-{
        return elem.outerHTML;
    }-*/;

	/**
	 * Use the javascript method for converting a string to its base64 representation.
	 *
	 * @param b64 The string to convert to base64
	 * @return The base64 representation of the given string
	 */
	native String btoa(String b64) /*-{
        return btoa(b64);
    }-*/;

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
		final FlowPanel hpanel = new FlowPanel();

		hpanel.getElement().setId("geo-panel");

		ChartLoader chartLoader = new ChartLoader(ChartPackage.GEOCHART);
		chartLoader.loadApi(new Runnable() {

			public void run() {
				setupCommonOptions();

				// Create and attach the chart to the panel
				hpanel.add(createCountryVisualization());
				hpanel.add(createCityVisualization());

				currentYear = MIN_YEAR;

				updateVisualization(container);

				setupReadyHandler();
			}
		});

		//Panel  for the visualization of the copyright
		HorizontalPanel footer = new HorizontalPanel();
		Label lblFooter = new Label();
		lblFooter.setText("Copyright Data Source Berkeley Earth");
		footer.add(lblFooter);

		container.add(footer);
		container.add(hpanel);

		SetupSlider(container);

		ClickHandler clickHandler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				Button source = (Button)event.getSource();

				for (int i = 0; i < hpanel.getWidgetCount(); i++) {
					Widget w = hpanel.getWidget(i);
					if (source == leftBtn
							&& w.getElement().hasClassName("right")) {
						w.getElement().toggleClassName("left");
						w.getElement().toggleClassName("right");
						leftBtn.setVisible(false);
						rightBtn.setVisible(true);
					} else if (source == rightBtn
							&& w.getElement().hasClassName("left")) {
						w.getElement().toggleClassName("right");
						w.getElement().toggleClassName("left");
						leftBtn.setVisible(true);
						rightBtn.setVisible(false);
					}
				}
				currentMapType = currentMapType == MapType.COUNTRY ? MapType.CITY : MapType.COUNTRY;
				refreshExportData();
			}
		};

		leftBtn = createNextButton("<", "left", clickHandler);
		rightBtn = createNextButton(">", "right", clickHandler);

		leftBtn.setHTML(leftBtn.getText() + "<span class='next-btn-desc'>Countries</span>");
		rightBtn.setHTML(rightBtn.getText() + "<span class='next-btn-desc'>Cities</span>");

		leftBtn.setVisible(false);

		container.add(leftBtn);
		container.add(rightBtn);
		container.add(createExportButton());
	}

	/**
	 * Sets up a ready listener to update the export button's data on
	 * every change of the underlying svg element.
	 */
	private void setupReadyHandler() {
		countryChart.addReadyHandler(new ReadyHandler() {
			@Override
			public void onReady(ReadyEvent readyEvent) {
				refreshExportData();
			}
		});
	}

	/**
	 * Creates a slider button
	 *
	 * @param text Sets the text on the button
	 * @param positionClass Sets the class which tells the button how to position itself
	 * @param handler A clickhandler, telling the button what to do on click
	 * @return The created button
	 */
	private Button createNextButton(String text, String positionClass, ClickHandler handler) {
		Button nextBtn = new Button();
		nextBtn.getElement().addClassName("next-button");
		nextBtn.getElement().addClassName(positionClass);
		nextBtn.getElement().removeClassName("gwt-Button");
		nextBtn.getElement().setId("switch-button-" + positionClass);
		nextBtn.setText(text);

		nextBtn.addClickHandler(handler);

		return nextBtn;
	}

	/**
	 * Draws the slider to the page
	 *
	 * @post The slider is drawn to page
	 * @param container The container panel to draw the slider into
	 */
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

		final Timer t = new Timer() {
			public void run() {
				updateVisualization(null);
			}
		};

		slider.addChangeListener(new ChangeListener() {
			@Override
			public void onChange(Widget sender) {
				int year = (int)slider.getCurrentValue();
				yearTxt.setText(String.valueOf(year));
				currentYear = year;

				t.cancel();

				t.schedule(updateDelay);
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

	native void consoleLog(String str) /*-{
        return console.log(str);
    }-*/;

	/**
	 * Updates the visualization and displays the map and the requested data
	 *
	 * @pre countryChart != null && cityChart != null
	 * @param  container A Panel which contains the whole visualization
	 */
	public void updateVisualization(Panel container) {
		AsyncCallback<HashMap<String, Float>> countryCallback = new AsyncCallback<HashMap<String, Float>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<String, Float> result) {
				countryData.removeRows(0, countryData.getNumberOfRows());

				for(String country : result.keySet()){
					countryData.addRow();
					countryData.setValue(countryData.getNumberOfRows()-1, 0, country);
					countryData.setValue(countryData.getNumberOfRows()-1, 1, result.get(country));
				}

				// Draw the chart
				countryChart.redraw();
			}
		};

		AsyncCallback<HashMap<String, Measurement>> cityCallback = new AsyncCallback<HashMap<String, Measurement>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<String, Measurement> result) {
				cityData.removeRows(0, cityData.getNumberOfRows());

				for(String city : result.keySet()){
					Measurement meas = result.get(city);
					float avg = Math.round(meas.getAvg() * 1000f) / 1000f;
					String tooltip = "City: " + city + "\nAverage Temperature (\u2103): " + avg;
					cityData.addRow();
					cityData.setValue(cityData.getNumberOfRows()-1, 0, meas.getCoords().getLat());
					cityData.setValue(cityData.getNumberOfRows()-1, 1, meas.getCoords().getLon());
					cityData.setValue(cityData.getNumberOfRows()-1, 2, meas.getAvg());
					cityData.setValue(cityData.getNumberOfRows()-1, 3, tooltip);
				}

				// Draw the chart
				cityChart.redraw();
			}
		};

		countryService.getAverageTempOfYear(currentYear, countryCallback);
		countryService.getAverageTempOfYearPerCity(currentYear, cityCallback);
	}

	/**
	 * Creates the map visualization chart for the countries.
	 *
	 * @pre Google Charts resources are loaded
	 * @post this.countryChart != null
	 * @param container The container to draw the visualization into
	 * @return The newly created chart
	 */
	private GeoChart createCountryVisualization() {
		countryChart = createVisualization(countryData, countryOptions, "country-chart");
		return countryChart;
	}

	/**
	 * Creates the map visualization chart for the cities.
	 *
	 * @pre Google Charts resources are loaded
	 * @post this.cityChart != null
	 * @return The newly created chart
	 */
	private GeoChart createCityVisualization() {
		cityChart = createVisualization(cityData, cityOptions, "city-chart");
		return cityChart;
	}

	/**
	 * Creates a geochart with the specified id
	 *
	 * @pre Google Charts resources are loaded
	 * @param id The html id for the geo-chart
	 * @return The newly created GeoChart
	 */
	private GeoChart createVisualization(DataTable data, GeoChartOptions options, String id) {
		GeoChart chart = new GeoChart();

		// Draw the chart
		chart.draw(data, options);
		chart.getElement().setId(id);
		chart.getElement().setClassName("left");
		chart.setHeight("80vh");

		return chart;
	}

	/**
	 * Creates the options for the map visualizations
	 */
	private void setupCommonOptions() {
		countryData = DataTable.create();
		cityData = DataTable.create();
		countryOptions = GeoChartOptions.create();
		cityOptions = GeoChartOptions.create();
		GeoChartColorAxis geoChartColorAxis = GeoChartColorAxis.create();

		// Set options for country
		geoChartColorAxis.setColors("Gold", "red");

		countryOptions.setColorAxis(geoChartColorAxis);
		countryOptions.setDatalessRegionColor("Lightgrey");

		cityOptions.setColorAxis(geoChartColorAxis);
		cityOptions.setDatalessRegionColor("Lightgrey");
		cityOptions.setDisplayMode(DisplayMode.MARKERS);

		// Set options for city

		countryData.addColumn(ColumnType.STRING, "Country");
		countryData.addColumn(ColumnType.NUMBER, "Average Temperature (\u2103)");
		cityData.addColumn(ColumnType.NUMBER, "Latitude");
		cityData.addColumn(ColumnType.NUMBER, "Longitude");
		cityData.addColumn(ColumnType.NUMBER, "Temperature");
		DataColumn col = DataColumn.create(ColumnType.STRING, RoleType.TOOLTIP, "City");
		cityData.addColumn(col);
	}
}
