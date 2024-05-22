package com.axreng.backend;

import com.google.gson.Gson;

import spark.Route;

public interface RouteHandler {

	Route createPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetActiveTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	Route createGetCompletedTasksRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetActiveTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

	void setupGetCompletedTasks(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);
}