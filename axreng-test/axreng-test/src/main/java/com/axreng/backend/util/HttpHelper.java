package com.axreng.backend.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HttpHelper {

	public static String getRequest(String urlString) {
		try {
			 // Remove ".." segments from the URL
		    urlString = urlString.replaceAll("\\.\\./", "");
			URL url = new URL(urlString);
			URI uri = url.toURI().normalize();
			url = uri.toURL();

			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				StringBuilder content = new StringBuilder();
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
				return content.toString();
			}
		} catch (IOException | URISyntaxException e) {
			System.err.println("Error fetching content from " + urlString + ": " + e.getMessage());
			return null;
		}
	}
}
