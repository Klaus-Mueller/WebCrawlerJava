package com.axreng.backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axreng.backend.SearchTask.Status;
import com.axreng.backend.util.HttpHelper;
import com.axreng.backend.util.PageCache;

public class WebCrawler {
	private String baseURL;
	private PageCache pageCache;

	public WebCrawler(String baseURL) {
		super();
		this.baseURL = baseURL;
		this.pageCache = new PageCache();
	}

	public void crawl(SearchTask task) {
		long startTime = System.nanoTime(); // Start tracking time
		Set<String> visitedUrls = new HashSet<>();
		Queue<String> urlsToVisit = new LinkedList<>();
		urlsToVisit.add(baseURL);

		while (!urlsToVisit.isEmpty()) {
			String url = urlsToVisit.poll();

			if (visitedUrls.contains(url) || !url.startsWith(baseURL)) {
				continue;
			}

			visitedUrls.add(url);

			String pageContent = pageCache.get(url);

			if (pageContent == null) {
				pageContent = fetchPageContent(url);
				if (pageContent == null) {
					System.err.println("Error fetching content from " + url);
					continue;
				}
				pageCache.put(url, pageContent);
			}

			// Check for keyword in page content
			if (pageContent.toLowerCase().contains(task.getKeyword().toLowerCase())) {
				task.addResult(url);
			}

			// Extract links and add to the queue
			List<String> links = extractLinks(pageContent);
			urlsToVisit.addAll(links);
		}

		System.out.println("Search completed for keyword: " + task.getKeyword());
		System.out.println("Results: " + task.getResults());
		task.setStatus(Status.DONE);
		long endTime = System.nanoTime();
		long elapsedTimeNano = endTime - startTime;
		double elapsedTimeMillis = (double) elapsedTimeNano / 1_000_000; // Convert nanoseconds to milliseconds
		System.out.println("Crawl method runtime: " + elapsedTimeMillis + " milliseconds");
	}

	public String fetchPageContent(String urlString) {
		return HttpHelper.getRequest(urlString);
	}

	public List<String> extractLinks(String pageContent) {
		List<String> links = new ArrayList<>();
		String regex = "<a\\s+(?:[^>]*?\\s+)?href=\"([^\"]*)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(pageContent);

		while (matcher.find()) {
			String link = matcher.group(1);
			if (!link.startsWith("http") &&  !link.isEmpty()) {
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
