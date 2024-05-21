package com.axreng.backend;

import com.google.gson.Gson;

import static spark.Spark.*;

public class AxurAPI {
	
    private int port;
    private Gson gson;
    private WebCrawler webCrawler;
    private RouteHandler routeHandler;
    private SearchTaskManager searchTaskManager;
    
    public AxurAPI(int port, RouteHandler routeHandler, String baseURL) {
        this.port = port;
        this.routeHandler = routeHandler;
        port(this.port);
        this.gson = new Gson();
        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("BASE_URL environment variable is not set or is empty");
        }
        this.webCrawler = new WebCrawler(baseURL);
        this.searchTaskManager = new SearchTaskManager(this.webCrawler);
    }

    public AxurAPI(int port, RouteHandler routeHandler) {
        this(port, routeHandler, System.getenv("BASE_URL"));
    }

    public void startAPI() {
        setupRoutes();
    }

    /**
     * Create Get and Post Routes
     */
    private void setupRoutes() {
        routeHandler.setupPostRoute(this, gson, searchTaskManager);
        routeHandler.setupGetRoute(this, gson, searchTaskManager);
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
