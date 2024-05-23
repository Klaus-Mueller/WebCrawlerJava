package com.axreng.backend;

import com.google.gson.Gson;

import spark.Route;

public interface RouteHandler {

	
	Route createSearcTaskPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createSearchTaskGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetActiveTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetCompletedTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupSearchTaskPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupSearchTaskGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetActiveTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetCompletedTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);
	
	void setupIndex();
}