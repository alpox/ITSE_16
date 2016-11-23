package com.g3.seapp.client;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MultiWordSuggestOracle;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a visualization of weatherdata
 * as a scrollable table.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities Shows a table of weatherdata 
 *
 */
public class TableVisualization implements IVisualization, IExportable {
	// Well... we can do that because our data amount never changes :-)
	private static final int MEASUREMENTCOUNT = 228175;
	
	private CellTable<Measurement> measurementTable = new CellTable<Measurement>();
	private AsyncDataProvider<Measurement> dataProvider;
	private List<String> columnNames = new ArrayList<String>();
	private TextColumn<Measurement> countryColumn;
	private TextColumn<Measurement> cityColumn;
	private TextColumn<Measurement> dateColumn;
	private TextColumn<Measurement> avgColumn;
	private TextColumn<Measurement> errorColumn;
	private TextColumn<Measurement> latColumn;
	private TextColumn<Measurement> lonColumn;
	private CountryServiceAsync countryService = GWT.create(CountryService.class);
	
	private final DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	
	private ArrayList<Measurement> measurements;
	
	private HashMap<Measurement.MeasurementType, String> filters = new HashMap<>();
	
	/**
	 * Constructor of TableVisualization
	 * We setup the shape of the table in here
	 */
	public TableVisualization() {
		setupColumns();
		measurementTable.setWidth("100%", true);

		measurementTable.setPageSize(25);
	}
	
	/**
	 * setup of the columns for the table. Called on initialization.
	 * @pre nothing
	 * @post measurementTable.getColumnCount() > 0
	 * @return nothing
	 */
	private void setupColumns() {
    	
		countryColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return measurement.getCountry();
		      }
	    };
	    
	    cityColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return measurement.getCity();
		      }
	    };
	    
	    dateColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return dateFormat.format(measurement.getDate());
		      }
	    };
	    
	    avgColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		      	float avg = Math.round(measurement.getAvg() * 1000f) / 1000f;
		        return Float.toString(avg);
		      }
	    };
	    
	    errorColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		      	float err = Math.round(measurement.getError() * 1000f) / 1000f;
		        return Float.toString(err);
		      }
	    };
	    
	    latColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		      	float lat = Math.round(measurement.getCoords().getLat() * 1000f) / 1000f;
		        return Float.toString(lat);
		      }
	    };
	    
	    lonColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		      	float lon = Math.round(measurement.getCoords().getLon() * 1000f) / 1000f;
		        return Float.toString(lon);
		      }
	    };

	    // Set the width of each column.
	    measurementTable.setColumnWidth(countryColumn, 15.0, Unit.PCT);
	    measurementTable.setColumnWidth(cityColumn, 15.0, Unit.PCT);
	    measurementTable.setColumnWidth(dateColumn, 15.0, Unit.PCT);
	    measurementTable.setColumnWidth(avgColumn, 20.0, Unit.PCT);
	    measurementTable.setColumnWidth(errorColumn, 20.0, Unit.PCT);
	    measurementTable.setColumnWidth(latColumn, 20.0, Unit.PCT);
	    measurementTable.setColumnWidth(lonColumn, 20.0, Unit.PCT);
	    
	    countryColumn.setSortable(true);
	    cityColumn.setSortable(true);
	    dateColumn.setSortable(true);
	    avgColumn.setSortable(true);
	    errorColumn.setSortable(true);
	    latColumn.setSortable(true);
	    lonColumn.setSortable(true);
	   
	    measurementTable.addColumn(countryColumn, "Country");
	    measurementTable.addColumn(cityColumn, "City");
	    measurementTable.addColumn(dateColumn, "Date");
	    measurementTable.addColumn(avgColumn, "Average");
	    measurementTable.addColumn(errorColumn, "Error");
	    measurementTable.addColumn(latColumn, "Latitude");
	    measurementTable.addColumn(lonColumn, "Longitude");
	    
	    columnNames.add("country");
	    columnNames.add("city");
	    columnNames.add("Date");
	    columnNames.add("avg");
	    columnNames.add("error");
	    columnNames.add("lat");
	    columnNames.add("lon");
	}
	
	/**
	 * Sets up the data provider for the measurementTable
	 * @pre nothing
	 * @post measurementTable has a dataprovider
	 * @return nothing
	 */
	private void setupDataProvider() {
		dataProvider = new AsyncDataProvider<Measurement>() {
		      @Override
		      protected void onRangeChanged(HasData<Measurement> display) {
		        final Range range = display.getVisibleRange();

		        // Get the ColumnSortInfo from the table.
		        final ColumnSortList sortList = measurementTable.getColumnSortList();

				final int start = range.getStart();
	            final int end = start + range.getLength();

				AsyncCallback<ArrayList<Measurement>> callback = new AsyncCallback<ArrayList<Measurement>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ArrayList<Measurement> result) {
						measurements = result;
						measurementTable.setRowData(start, measurements);
						//if(measurements.size() == 0)
						//	dataProvider.updateRowCount(0, true);

						updateTableRowCount();
					}
				};

				Measurement.MeasurementType sortCol = Measurement.MeasurementType.COUNTRY;
				boolean asc = true;
				
				if(sortList.size() > 0) {
					int colIndex = measurementTable.getColumnIndex((Column<Measurement, ?>)sortList.get(0).getColumn());
					sortCol = Measurement.MeasurementType.valueOf(columnNames.get(colIndex).toUpperCase());
					asc = sortList.get(0).isAscending();
				}
	            
				countryService.getMeasurements(start, end, sortCol, !asc, filters, callback);
			}
		};
		
		dataProvider.addDataDisplay(measurementTable);
		
		AsyncHandler columnSortHandler = new AsyncHandler(measurementTable);
		measurementTable.addColumnSortHandler(columnSortHandler);
		
		measurementTable.getColumnSortList().push(countryColumn);
	}

	private void setupFilter(final Panel container, final Measurement.MeasurementType key) {
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		final SuggestBox countryFilterBox = new SuggestBox(oracle);

		countryFilterBox.addValueChangeHandler(new ValueChangeHandler<String>() {

			@Override
			public void onValueChange(final ValueChangeEvent<String> event) {
				applyFilter(key, event.getValue());
			}
		});

		countryFilterBox.addSelectionHandler(new SelectionHandler<SuggestOracle.Suggestion>() {
			@Override
			public void onSelection(SelectionEvent<SuggestOracle.Suggestion> event) {
				applyFilter(key, event.getSelectedItem().getReplacementString());
			}
		});


		AsyncCallback<ArrayList<String>> callback = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				((MultiWordSuggestOracle)countryFilterBox.getSuggestOracle()).addAll(result);
			}
		};

		countryService.getNames(key, callback);

		container.add(countryFilterBox);
	}

	private void applyFilter(Measurement.MeasurementType key, String filterString) {
		if(filterString.isEmpty())
			filters.remove(key);
		else
			filters.put(key, filterString);

		measurementTable.setVisibleRangeAndClearData(measurementTable.getVisibleRange(), true);
	}

	private void updateTableRowCount() {
		AsyncCallback<Integer> callback = new AsyncCallback<Integer>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(Integer result) {
				dataProvider.updateRowCount(result, true);
			}
		};

		countryService.getMeasurementEntrySize(filters, callback);
	}

	/**
	 * Exports the table data as csv.
	 * @pre nothing
	 * @post nothing
	 * @return nothing
	 */
	@Override
	public void export() {
		// TODO Implement
	}

	/**
	 * Gets the name of the visualization
	 * @pre nothing
	 * @post nothing
	 * @return The name of the visualization
	 */
	@Override
	public String getName() {
		return "Table Visualization";
	}

	/**
	 * Draws the visualization
	 * @pre nothing
	 * @post container holds table and pager
	 * @return nothing
	 */
	@Override
	public void drawVisualization(final Panel container) {
		
		//countryService.getMeasurements(0, 20, callback);
		setupDataProvider();
		
	    measurementTable.setRowCount(MEASUREMENTCOUNT, true);
	    
	    SimplePager.Resources resources = GWT.create(SimplePager.Resources.class);
	    SimplePager pager = new SimplePager(TextLocation.CENTER, resources,
				true, 500, true);

	    pager.setPageSize(25);
	    // Set the cellList as the display.
	    pager.setDisplay(measurementTable);

		setupFilter(container, Measurement.MeasurementType.COUNTRY);
		setupFilter(container, Measurement.MeasurementType.CITY);
		setupFilter(container, Measurement.MeasurementType.DATE);
		setupFilter(container, Measurement.MeasurementType.AVG);
		setupFilter(container, Measurement.MeasurementType.ERROR);
		setupFilter(container, Measurement.MeasurementType.LAT);
		setupFilter(container, Measurement.MeasurementType.LON);

		container.add(measurementTable);
		container.add(pager);
	}

	/**
	 * Updates the visualization
	 * @pre nothing
	 * @post nothing
	 * @return nothing
	 */
	@Override
	public void updateVisualization(Panel container) {

	}

}
