package com.g3.seapp.client;

import com.google.gwt.user.client.ui.Panel;

/**
 * An interface declaring all methods
 * for drawing an arbitrary visualization
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities
 * 	Representing a visualization
 *
 */
public interface IVisualization {
	/**
	 * Gets the name of the visualization
	 * @return the name of the visualization
	 */
	public String getName();
	/**
	 * Draws the visualization
	 * @param container The panel in which the visualization should be drawn
	 */
	public void drawVisualization(Panel container);
	/**
	 * Updates the visualization
	 * @param container The panel in which the visualization should be drawn
	 */
	public void updateVisualization(Panel container);
}
