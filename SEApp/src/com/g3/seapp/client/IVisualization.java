package com.g3.seapp.client;

import com.google.gwt.user.client.ui.Panel;

public interface IVisualization {
	public String getName();
	public void drawVisualization(Panel container);
	public void updateVisualization(Panel container);
}
