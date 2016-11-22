package com.g3.seapp.server;

import javax.servlet.http.HttpServlet;

public class DataLoaderServlet extends HttpServlet {
	public void init() {
		DataManager.loadData();
	}
}
