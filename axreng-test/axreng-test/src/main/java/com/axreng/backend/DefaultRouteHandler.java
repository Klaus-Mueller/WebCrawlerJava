package com.axreng.backend;

import static spark.Spark.get;
import static spark.Spark.post;

import java.util.List;

import com.google.gson.Gson;

import spark.Route;

public class DefaultRouteHandler implements RouteHandler {


	@Override
	public Route createPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		return (request, response) -> {
			response.type("application/json");
			String keyword = gson.fromJson(request.body(), SearchTask.class).getKeyword();
			String taskId = searchTaskManager.startSearch(keyword);
			String body = gson.toJson(new TaskResponse(taskId));
			response.status(200);
			response.body(body);
			return body;
		};
	}

	@Override
	public Route createGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		return (request, response) -> {
			response.type("application/json");
			String taskId = request.params(":id");
			SearchTask task = searchTaskManager.getTask(taskId);
			if (task != null) {
				String body = gson.toJson(task);
				response.status(200);
				response.body(body);
				return body;
			} else {
				response.status(404);
				return gson.toJson(new ErrorResponse("Task not found"));
			}
		};
	}

	@Override
	public Route createGetActiveTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		return (request, response) -> {
			response.type("application/json");
			List<SearchTask> tasks = searchTaskManager.getActiveSearchTaks();
			if (tasks != null) {
				String body = gson.toJson(tasks);
				response.status(200);
				response.body(body);
				return body;
			} else {
				response.status(404);
				return gson.toJson(new ErrorResponse("Task not found"));
			}
		};
	}

	@Override
	public Route createGetCompletedTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		return (request, response) -> {
			response.type("application/json");
			List<SearchTask> tasks = searchTaskManager.getCompletedSearchTaks();
			if (tasks != null) {
				String body = gson.toJson(tasks);
				response.status(200);
				response.body(body);
				return body;
			} else {
				response.status(404);
				return gson.toJson(new ErrorResponse("Task not found"));
			}
		};
	}

	@Override
	public void setupPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		Route postRoute = createPostRoute(axurAPI, gson, searchTaskManager);
		post("/crawl", postRoute);
	}

	@Override
	public void setupGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		Route getRoute = createGetRoute(axurAPI, gson, searchTaskManager);
		get("/tasks/:id", getRoute);
	}

	@Override
	public void setupGetActiveTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		Route getRoute = createGetActiveTasksRoute(axurAPI, gson, searchTaskManager);
		get("/tasks/status/active", getRoute);
	}

	@Override
	public void setupGetCompletedTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager) {
		Route getRoute = createGetCompletedTasksRoute(axurAPI, gson, searchTaskManager);
		get("/tasks/status/done", getRoute);
	}
}