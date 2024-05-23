package com.axreng.backend;

public class Main {
	public static void main(String[] args) {
		API aPI = new API(4567, new DefaultRouteHandler());
		aPI.startAPI();
	}
}