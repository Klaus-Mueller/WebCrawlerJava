package com.axreng.backend;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
	private Map<String, SearchTask> tasks = new ConcurrentHashMap<>();
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private String baseURL;

	public WebCrawler(String baseURL) {
		super();
		this.baseURL = baseURL;
	}

	public String startSearch(SearchTask task) {
		String taskId = UUID.randomUUID().toString();
		task.setId(taskId);
		tasks.put(taskId, task);
		executorService.submit(() -> crawl(task));
		return taskId;
	}

	public SearchTask getTask(String taskId) {
		return tasks.get(taskId);
	}

	private void crawl(SearchTask task) {
		Set<String> visitedUrls = new HashSet<>();
		Queue<String> urlsToVisit = new LinkedList<>();
		urlsToVisit.add(baseURL);

		while (!urlsToVisit.isEmpty()) {
			String url = urlsToVisit.poll();

			if (visitedUrls.contains(url) || !url.startsWith(baseURL)) {
				continue;
			}

			visitedUrls.add(url);

			// Fetch page content
			String pageContent = fetchPageContent(url);

			if (pageContent == null) {
				System.err.println("Error fetching content from " + url);
				continue;
			}

			// Check for keyword in page content
			if (pageContent.contains(task.getKeyword())) {
				task.addResult(url);
			}

			// Extract links and add to the queue
			List<String> links = extractLinks(pageContent);
			urlsToVisit.addAll(links);
		}

		System.out.println("Search completed for keyword: " + task.getKeyword());
		System.out.println("Results: " + task.getResults());
	}

	private String fetchPageContent(String urlString) {
		StringBuilder content = new StringBuilder();

		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");

			try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String inputLine;
				while ((inputLine = in.readLine()) != null) {
					content.append(inputLine);
				}
			}
		} catch (Exception e) {
			System.err.println("Error fetching content from " + urlString + ": " + e.getMessage());
			return null;
		}

		return content.toString();
	}

	public List<String> extractLinks(String pageContent) {
		List<String> links = new ArrayList<>();
		String regex = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pageContent);

		while (matcher.find()) {
			String link = matcher.group(1);
			if (!link.startsWith("http")) {
				// Remove trailing slash from base URL if present
				String urlBase = this.baseURL.endsWith("/") ? this.baseURL.substring(0, this.baseURL.length() - 1) : this.baseURL;
				if (link.startsWith("/")) {
					// Concatenate base URL with link
					link = urlBase + link;
				} else {
					// This is a relative link, concatenate it with the current URL
					link = urlBase + "/" + link;		
				}
			}
			if (link.startsWith(this.baseURL)) {
				links.add(link);
			}
		}
		return links;
	}


}
