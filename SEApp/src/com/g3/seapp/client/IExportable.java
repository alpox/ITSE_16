package com.g3.seapp.client;

/**
 * An interface for exportables.
 * 
 * @author Elias Bernhaut
 * @version 0.0.1
 * @history 14.11.2016 Version 1
 * @responsibilities 
 * 	Defines an interface for classes which
 *  can export data/visualizations.
 */
public interface IExportable {
	/**
	 * Exports data from an exportable class
	 * 
	 * @return nothing
	 */
	public void export();
}
