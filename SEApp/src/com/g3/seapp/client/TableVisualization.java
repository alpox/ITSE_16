package com.g3.seapp.client;

import com.g3.seapp.shared.Measurement;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.builder.shared.FieldSetBuilder;
import com.google.gwt.dom.client.FieldSetElement;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.*;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
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
	    measurementTable.setColumnWidth(countryColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(cityColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(dateColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(avgColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(errorColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(latColumn, 150, Unit.PX);
	    measurementTable.setColumnWidth(lonColumn, 150, Unit.PX);
	    
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
	 *
	 * @post measurementTable has a dataprovider
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

	/**
	 * Sets up a filter SuggestBox. Used for filtering
	 * the measurementTable for a specific column.
	 *
	 * @param container The panel in which the TableVisualization gets drawn
	 * @param key The type of the measurement property to setup the filter for
	 */
	private void setupFilter(final Panel container, final Measurement.MeasurementType key) {
		final MultiWordSuggestOracle oracle = new MultiWordSuggestOracle();
		final SuggestBox countryFilterBox = new SuggestBox(oracle);

		countryFilterBox.setWidth("140px");

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

		String labelText = key.toString().toLowerCase();
		labelText = String.valueOf(labelText.charAt(0)).toUpperCase() + labelText.substring(1, labelText.length());

		VerticalPanel panel = new VerticalPanel();

		Label label = new Label();
		label.setText(labelText);
		panel.add(label);

		panel.add(countryFilterBox);

		container.add(panel);
	}

	/**
	 * Applies a specific value to filter for
	 *
	 * @param key The type of the measurement property to filter for
	 * @param filterString The specific filter string
	 */
	private void applyFilter(Measurement.MeasurementType key, String filterString) {
		if(filterString.isEmpty())
			filters.remove(key);
		else
			filters.put(key, filterString);

		measurementTable.setVisibleRangeAndClearData(measurementTable.getVisibleRange(), true);
	}

	/**
	 * Updates the number of rows in the table
	 */
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
	 */
	@Override
	public void export() {
		// TODO Implement
	}
	
	/**
	 * Gets the name of the visualization
	 *
	 * @return The name of the visualization
	 */
	@Override
	public String getName() {
		return "Table Visualization";
	}
	
	public void createDropdown()
	{
		// Make a new dropdown for selecting the datatyp, adding a few items to it.
				ListBox lb1 = new ListBox();
				// Set the html id of the dropdown for styling in the css
				lb1.getElement().setId("dropdownForDataType");
				lb1.addItem("Date");
				lb1.addItem("Average");
				lb1.addItem("Error");
				lb1.addItem("Latitude");
				lb1.addItem("Longitude");
				
				//New Dropdown for aggregation methods
				ListBox lb2 = new ListBox();
				lb2.getElement().setId("aggregationMethods");
				lb2.addItem("Median");
				lb2.addItem("Minimum");
				lb2.addItem("Maximum");
				lb2.addItem("Standard deviation");
				
				// Add the dropdown to the main container
				RootPanel.get("mainContainer").insert(lb1, 0);
				RootPanel.get("mainContainer").insert(lb2, 0);
	}

	/**
	 * Draws the visualization
	 *
	 * @param container The panel in which the TableVisualization gets drawn
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

	    HorizontalPanel horizPanel = new HorizontalPanel();

		setupFilter(horizPanel, Measurement.MeasurementType.COUNTRY);
		setupFilter(horizPanel, Measurement.MeasurementType.CITY);
		setupFilter(horizPanel, Measurement.MeasurementType.DATE);
		setupFilter(horizPanel, Measurement.MeasurementType.AVG);
		setupFilter(horizPanel, Measurement.MeasurementType.ERROR);
		setupFilter(horizPanel, Measurement.MeasurementType.LAT);
		setupFilter(horizPanel, Measurement.MeasurementType.LON);

		container.add(horizPanel);

		container.add(measurementTable);
		container.add(pager);
			
		//Panel for the visualization of the copyright
		HorizontalPanel footer = new HorizontalPanel();
		Label lblFooter = new Label();
		lblFooter.setText("Copyright Data source K. Meier");
		footer.add(lblFooter);
		container.add(footer);
		
		//creats two dropdowns for choosing datatype and aggreagtion method
		createDropdown();
		
		// Create a Label and an HTML widget.
	    Label lbl = new Label();
	    //set id
	    lbl.getElement().setId("resultLable");
	    //set text
	    lbl.setText("result"); 
	    //Add  label
		VerticalPanel panel = new VerticalPanel();
	    panel.add(lbl);
	    
	    container.add(panel);
	}

	/**
	 * Updates the visualization
	 *
	 * @param container The panel in which the TableVisualization gets drawn
	 * @post container holds table and pager
	 */
	@Override
	public void updateVisualization(Panel container) {

	}

}
