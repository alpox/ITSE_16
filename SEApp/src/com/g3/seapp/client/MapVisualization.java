package com.g3.seapp.client;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class MapVisualization implements IVisualization, IExportable {

	@Override
	public void export() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Map Visualization";
	}

	@Override
	public void drawVisualization(Panel container) {
		// TODO Auto-generated method stub
		container.add(new HTML("<span>This is the world map</span>"));
	}

	@Override
	public void updateVisualization(Panel container) {
		// TODO Auto-generated method stub
		
	}

}
