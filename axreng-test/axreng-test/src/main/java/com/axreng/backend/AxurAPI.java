package com.axreng.backend;

import static spark.Spark.port;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

public class AxurAPI {

	private static final Logger logger = LoggerFactory.getLogger(AxurAPI.class);

	private int port;
	private Gson gson;
	private WebCrawler webCrawler;
	private RouteHandler routeHandler;
	private SearchTaskManager searchTaskManager;

	public AxurAPI(int port, RouteHandler routeHandler, String baseURL) {
		if (baseURL == null || baseURL.isEmpty()) {
			throw new IllegalArgumentException("BASE_URL environment variable is not set or is empty");
		}
		this.port = port;
		this.routeHandler = routeHandler;
		port(this.port);
		this.gson = new Gson();
		this.webCrawler = new WebCrawler(baseURL);
		this.searchTaskManager = new SearchTaskManager(this.webCrawler);
		logger.info("AxurAPI initialized with port: {} and baseURL: {}", port, baseURL);
	}

	public AxurAPI(int port, RouteHandler routeHandler) {
		this(port, routeHandler, System.getenv("BASE_URL"));
	}

	public void startAPI() {
		setupRoutes();
		logger.info("API started on port {}", port);
	}

	/**
	 * Create Get and Post Routes
	 */
	private void setupRoutes() {
		routeHandler.setupPostRoute(this, gson, searchTaskManager);
		routeHandler.setupGetRoute(this, gson, searchTaskManager);
		routeHandler.setupGetActiveTasks(this, gson, searchTaskManager);
		routeHandler.setupGetCompletedTasks(this, gson, searchTaskManager);
		logger.info("Routes have been set up");
	}

	// Getters for testing
	public int getPort() {
		return port;
	}

	public Gson getGson() {
		return gson;
	}

	public WebCrawler getWebCrawler() {
		return webCrawler;
	}
}
