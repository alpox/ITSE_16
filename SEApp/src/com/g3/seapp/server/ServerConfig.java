package com.g3.seapp.server;

import java.util.ArrayList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ServerConfig implements ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		DataManager dm = new DataManager();
		ArrayList<String[]> countries = dm.readCSV();
	}
}