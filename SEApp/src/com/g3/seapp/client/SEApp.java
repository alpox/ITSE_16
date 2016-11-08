package com.g3.seapp.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class SEApp implements EntryPoint {
	
	private void setVisualization(Widget wdg) {
		RootPanel visContainer = RootPanel.get("visualizationContainer");
		visContainer.clear();
		visContainer.add(wdg);
	}
	
	private void showTable() {
		Widget wdg = new HTML("<span>Table</span>");
		setVisualization(wdg);
	}
	
	private void showMap() {
		Widget wdg = new HTML("<span>Map</span>");
		setVisualization(wdg);
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		
		ListBox dropDown = new ListBox();
		dropDown.getElement().setId("chooseVisDropDown");
		dropDown.addItem("World map");
		dropDown.addItem("Table");
		
		dropDown.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				ListBox listBoxFromEvent = (ListBox)event.getSource();
				int selected = listBoxFromEvent.getSelectedIndex();
				switch(selected) {
				case 1: showTable(); break;
				default: showMap(); break;
				}
			}
			
		});
		
		showMap();
		
		RootPanel.get("mainContainer").insert(dropDown, 0);
	}
}
