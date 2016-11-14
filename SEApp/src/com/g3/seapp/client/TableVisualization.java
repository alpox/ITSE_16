package com.g3.seapp.client;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.mortbay.log.Log;

import com.g3.seapp.shared.Country;
import com.g3.seapp.shared.CountryCollection;
import com.g3.seapp.shared.Measurement;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.AsyncHandler;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.user.cellview.client.ColumnSortList.ColumnSortInfo;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;

/**
 * Represents a visualization of weatherdata
 * as a scrollable table.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @responsibilities Shows a table of weatherdata 
 *
 */
public class TableVisualization implements IVisualization, IExportable {
	// Well... we can do that because our data amount never changes :-)
	private static final int MEASUREMENTCOUNT = 228175;
	
	private CellTable<Measurement> measurementTable = new CellTable<Measurement>();
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
	
	private List<Measurement> measurements;
	
	/**
	 * Constructor of TableVisualization
	 * We setup the shape of the table in here
	 */
	public TableVisualization() {
		SetupColumns();
		measurementTable.setWidth("100%", true);
	}
	
	/**
	 * Setup of the columns for the table. Called on initialization.
	 * @pre nothing
	 * @post measurementTable.getColumnCount() > 0
	 * @return nothing
	 */
	private void SetupColumns() {
    	
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
		        return Float.toString(measurement.getAvg());
		      }
	    };
	    
	    errorColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return Float.toString(measurement.getError());
		      }
	    };
	    
	    latColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return Float.toString(measurement.getCoords().getLat());
		      }
	    };
	    
	    lonColumn = new TextColumn<Measurement>() {
		      @Override
		      public String getValue(Measurement measurement) {
		        return Float.toString(measurement.getCoords().getLon());
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
	private void SetupDataProvider() {
		AsyncDataProvider<Measurement> dataProvider = new AsyncDataProvider<Measurement>() {
		      @Override
		      protected void onRangeChanged(HasData<Measurement> display) {
		        final Range range = display.getVisibleRange();

		        // Get the ColumnSortInfo from the table.
		        final ColumnSortList sortList = measurementTable.getColumnSortList();

				final int start = range.getStart();
	            final int end = start + range.getLength();

				// TODO Auto-generated method stub
				AsyncCallback<ArrayList<Measurement>> callback = new AsyncCallback<ArrayList<Measurement>>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onSuccess(ArrayList<Measurement> result) {
						// TODO Auto-generated method stub
						measurements = result;
						measurementTable.setRowData(start, measurements);
					}
				};

				String sortCol = "country";
				boolean asc = true;
				
				if(sortList.size() > 0) {
					int colIndex = measurementTable.getColumnIndex((Column<Measurement, ?>)sortList.get(0).getColumn());
					sortCol = columnNames.get(colIndex);
					asc = sortList.get(0).isAscending();
				}
	            
				countryService.getMeasurements(start, end, sortCol, !asc, callback);
			}
		};
		
		dataProvider.addDataDisplay(measurementTable);
		
		AsyncHandler columnSortHandler = new AsyncHandler(measurementTable);
		measurementTable.addColumnSortHandler(columnSortHandler);
		
		measurementTable.getColumnSortList().push(countryColumn);
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
		SetupDataProvider();
		
	    measurementTable.setRowCount(MEASUREMENTCOUNT, true);
	    
	    //SimplePager.Resources resources = GWT.create(SimplePager.Resources.class);
	    SimplePager pager = new SimplePager();

	    // Set the cellList as the display.
	    pager.setDisplay(measurementTable);
	    
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
