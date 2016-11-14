package com.g3.seapp.client;

import java.util.ArrayList;
import java.util.List;

import com.g3.seapp.server.CountryServiceImpl;
import com.g3.seapp.shared.CountryCollection;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities Shows the whole app.
 */
public class SEApp implements EntryPoint {
	
	private CountryServiceAsync countryService = GWT.create(CountryService.class);
	
	/**
	 * Gets the empty root panel - all visualizations are drawn in this panel
	 * @pre A DOM element with id "visualizationContainer" exists
	 * @post the DOM element with id "visualizationContainer" is empty
	 * @return the empty root panel.
	 */
	private Panel getEmptyRootPanel() {
		Panel panel = RootPanel.get("visualizationContainer");
		panel.clear();
		return panel;
	}

	/**
	 * This is the entry point method.
	 * @pre nothing
	 * @post app is loaded
	 * @return nothing
	 */
	public void onModuleLoad() {
		// Create a list with all possible visualizations
		final List<IVisualization> visualizations = new ArrayList<IVisualization>() {{
			add(new MapVisualization());
			add(new TableVisualization());
		}};
		
		// Create a dropdown list
		ListBox dropDown = new ListBox();
		// Set the html id of the dropdown for styling in the css
		dropDown.getElement().setId("chooseVisDropDown");
		
		// Add all visualizations to the dropdown
		for(IVisualization vis : visualizations)
			dropDown.addItem(vis.getName());
		
		// On change of the visualization do:
		dropDown.addChangeHandler(new ChangeHandler() {

			/**
			 * Draws the specific visualization into the empty root panel
			 * on change of the selected dropdown entry.
			 */
			@Override
			public void onChange(ChangeEvent event) {
				// Get dropdown
				ListBox listBoxFromEvent = (ListBox)event.getSource();
				// Get selected index of dropdown
				int selected = listBoxFromEvent.getSelectedIndex();
				// Draw selected visualization
				visualizations.get(selected).drawVisualization(getEmptyRootPanel());
			}
			
		});
		
		// Draw map visualization on initialization
		visualizations.get(0).drawVisualization(getEmptyRootPanel());
		
		// Add the dropdown to the main container
		RootPanel.get("mainContainer").insert(dropDown, 0);
	}
}
