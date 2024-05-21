package com.axreng.backend;

import com.google.gson.Gson;

public interface RouteHandler {
    void setupPostRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);

    void setupGetRoute(AxurAPI axurAPI, Gson gson, SearchTaskManager searchTaskManager);
}