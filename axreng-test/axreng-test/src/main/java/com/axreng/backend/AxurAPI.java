package com.axreng.backend;

import com.google.gson.Gson;

import static spark.Spark.*;

public class AxurAPI {
    private int port;
    private Gson gson;
    private WebCrawler webCrawler;
    private RouteHandler routeHandler;

    
 // This is for UnitTest Only.
    public AxurAPI(int port, RouteHandler routeHandler, String baseURL) {
        this.port = port;
        this.routeHandler = routeHandler;
        port(this.port);
        this.gson = new Gson();
        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("BASE_URL environment variable is not set or is empty");
        }
        this.webCrawler = new WebCrawler(baseURL);
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
        routeHandler.setupPostRoute(this, gson, webCrawler);
        routeHandler.setupGetRoute(this, gson, webCrawler);
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
