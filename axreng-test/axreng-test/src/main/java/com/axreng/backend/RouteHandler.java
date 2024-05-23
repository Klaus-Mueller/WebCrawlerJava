package com.axreng.backend;

import com.google.gson.Gson;

import spark.Route;

public interface RouteHandler {

	
	Route createSearcTaskPostRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	Route createSearchTaskGetRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetActiveTasksRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetCompletedTasksRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	void setupSearchTaskPostRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	void setupSearchTaskGetRoute(API api, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetActiveTasks(API api, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetCompletedTasks(API api, Gson gson, SearchTaskManager searchTaskManager);
	
	void setupIndex();
}