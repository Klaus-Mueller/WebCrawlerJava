package com.axreng.backend;

import com.google.gson.Gson;

public interface RouteHandler {
    void setupPostRoute(AxurAPI axurAPI, Gson gson, WebCrawler webCrawler);

    void setupGetRoute(AxurAPI axurAPI, Gson gson, WebCrawler webCrawler);
}