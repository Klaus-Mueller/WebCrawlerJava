package com.axreng.backend;

import com.google.gson.Gson;

import static spark.Spark.get;
import static spark.Spark.post;

public class DefaultRouteHandler implements RouteHandler {
	
    @Override
    public void setupPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
        post("/crawl", (request, response) -> {
            response.type("application/json");
            String keyword = gson.fromJson(request.body(), SearchTask.class).getKeyword();
            String taskId = searchTaskManager.startSearch(keyword);
            return gson.toJson(new TaskResponse(taskId));
        }, gson::toJson);
    }

    @Override
    public void setupGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
        get("/tasks/:id", (request, response) -> {
            response.type("application/json");
            String taskId = request.params(":id");
            SearchTask task = searchTaskManager.getTask(taskId);
            if (task != null) {
                return gson.toJson(task);
            } else {
                response.status(404);
                return gson.toJson(new ErrorResponse("Task not found"));
            }
        }, gson::toJson);
    }
}