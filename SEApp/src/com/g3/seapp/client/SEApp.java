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
 */
public class SEApp implements EntryPoint {
	
	private CountryServiceAsync countryService = GWT.create(CountryService.class);
	
	private Panel getEmptyRootPanel() {
		Panel panel = RootPanel.get("visualizationContainer");
		panel.clear();
		return panel;
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		final List<IVisualization> visualizations = new ArrayList<IVisualization>() {{
			add(new MapVisualization());
			add(new TableVisualization());
		}};
		
		ListBox dropDown = new ListBox();
		dropDown.getElement().setId("chooseVisDropDown");
		
		for(IVisualization vis : visualizations)
			dropDown.addItem(vis.getName());
		
		dropDown.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				ListBox listBoxFromEvent = (ListBox)event.getSource();
				int selected = listBoxFromEvent.getSelectedIndex();
				visualizations.get(selected).drawVisualization(getEmptyRootPanel());
			}
			
		});
		
		visualizations.get(0).drawVisualization(getEmptyRootPanel());
		
		RootPanel.get("mainContainer").insert(dropDown, 0);
	}
}
