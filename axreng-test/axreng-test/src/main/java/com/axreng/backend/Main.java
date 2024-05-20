package com.axreng.backend;

public class Main {
    public static void main(String[] args) {
       AxurAPI axurAPI = new AxurAPI(4567, new DefaultRouteHandler());
       axurAPI.startAPI();
    }
}