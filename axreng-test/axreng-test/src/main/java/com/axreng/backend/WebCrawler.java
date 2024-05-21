package com.axreng.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.axreng.backend.SearchTask.Status;
import com.axreng.backend.util.HttpHelper;
import com.axreng.backend.util.PageCache;

public class WebCrawler {
	private final String baseURL;
	private final PageCache pageCache;
	private final int numThreads = 3;
	private ConcurrentLinkedQueue<String> urlsToVisit = new ConcurrentLinkedQueue<>();

	public WebCrawler(String baseURL) {
		super();
		this.baseURL = baseURL;
		this.pageCache = new PageCache();
	}

	public void crawl(SearchTask task) {
		initializeURLsToVisit();
		long startTime = System.currentTimeMillis(); 
		Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
		ExecutorService executorService = Executors.newFixedThreadPool(this.numThreads);

		try {
			while (!urlsToVisit.isEmpty()) {
				List<String> batch = new ArrayList<>();
				int batchSize = Math.min(numThreads, urlsToVisit.size());
				for (int i = 0; i < batchSize; i++) {
					String url = urlsToVisit.poll();
					url = url.replaceAll("\\.\\./", "");
					if (isInvalidURL(url, visitedUrls)) {
						continue;
					}
					batch.add(url);
					visitedUrls.add(url);
				}
				for (String url : batch) {
					executorService.submit(() -> {
						List<String> links = processNewUrls(url, task);
						for(String link : links) {
							if(!visitedUrls.contains(link)) {
								this.urlsToVisit.add(link);
							}
						}
					});
				}
				if(urlsToVisit.isEmpty()) {
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executorService.shutdown();
			try {
				executorService.awaitTermination((long) 30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
		System.out.println("Search completed for keyword: " + task.getKeyword());
		System.out.println("Results: " + task.getResults());
		task.setStatus(Status.DONE);
		long endTime = System.currentTimeMillis();
		long elapsedTimeSecs = (endTime - startTime) / 1000;
		System.out.println("Crawl method runtime: " + elapsedTimeSecs + " seconds");
	}

	public List<String> processNewUrls(String url, SearchTask task) {
		String pageContent = pageCache.get(url);

		if (pageContent == null) {
			pageContent = fetchPageContent(url);
			if (pageContent == null) {
				System.err.println("Error fetching content from " + url);
				return null;
			}
			pageCache.put(url, pageContent);
		}

		if (pageContent.toLowerCase().contains(task.getKeyword().toLowerCase())) {
			task.addResult(url);
		}

		List<String> links = extractLinks(pageContent);
		return links;
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
					link = urlBase + link;
				} else {
					link = urlBase + "/" + link;		
				}
			}
			if (link.startsWith(this.baseURL)) {
				links.add(link);
			}
		}
		return links;
	}

	private boolean isInvalidURL(String url, Set<String> visitedUrls) {
		return (url == null || visitedUrls.contains(url) || !url.startsWith(this.baseURL) || url.contains("mailto:")
				|| url.contains("ftp:"));
	}

	private void initializeURLsToVisit() {
		this.urlsToVisit.add(this.baseURL);
		String pageContent = pageCache.get(this.baseURL);

		if (pageContent == null) {
			pageContent = fetchPageContent(this.baseURL);
			if (pageContent == null) {
				System.err.println("Error fetching content from " + this.baseURL);
				return;
			}
			pageCache.put(this.baseURL, pageContent);
			List<String> links = extractLinks(pageContent);
			urlsToVisit.addAll(links);
		}
	}
}
