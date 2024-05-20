package com.axreng.backend;

import com.google.gson.Gson;

import static spark.Spark.*;

public class AxurAPITest {
	private int port;
	private Gson gson;
	private WebCrawler webCrawler;
	
	public AxurAPITest(int port) {
		this.port = port;
		port(this.port);
		this.gson = new Gson();
		// Retrieve the base URL from the environment variable
        String baseURL = System.getenv("BASE_URL");
        if (baseURL == null || baseURL.isEmpty()) {
            throw new IllegalArgumentException("BASE_URL environment variable is not set or is empty");
        }
        this.webCrawler = new WebCrawler(baseURL);
	}
	
	public void startAPI() {
	     setupRoutes();
	}
	
	 private void setupRoutes() {
	        post("/crawl", (request, response) -> {
	            response.type("application/json");
	            SearchTask task = this.gson.fromJson(request.body(), SearchTask.class);
	            String taskId = this.webCrawler.startSearch(task);
	            return this.gson.toJson(new TaskResponse(taskId));
	        }, this.gson::toJson);

	        get("/tasks/:id", (request, response) -> {
	            response.type("application/json");
	            String taskId = request.params(":id");
	            SearchTask task = this.webCrawler.getTask(taskId);
	            if (task != null) {
	                return this.gson.toJson(task);
	            } else {
	                response.status(404);
	                return this.gson.toJson(new ErrorResponse("Task not found"));
	            }
	        }, this.gson::toJson);
	    }
}
