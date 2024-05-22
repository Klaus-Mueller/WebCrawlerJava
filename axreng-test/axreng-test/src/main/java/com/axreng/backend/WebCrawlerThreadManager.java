package com.axreng.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class WebCrawlerThreadManager {
	private static final int NUM_THREADS = 5;
	private final ExecutorService executorService;

	public WebCrawlerThreadManager () {
		this.executorService = Executors.newFixedThreadPool(NUM_THREADS);
	}
	
	public void executeCrawling(WebCrawler webCrawler, ConcurrentLinkedQueue<String> urlsToVisit, SearchTask task) {
		Set<String> visitedUrls = ConcurrentHashMap.newKeySet();
		try {
			while (!urlsToVisit.isEmpty()) {
				List<String> batch = new ArrayList<>();
				int batchSize = Math.min(NUM_THREADS, urlsToVisit.size());
				for (int i = 0; i < batchSize; i++) {
					String url = urlsToVisit.poll();
					url = normalizeURL(url);
					if (webCrawler.isInvalidURL(url, visitedUrls)) {
						continue;
					}
					batch.add(url);
					visitedUrls.add(url);
				}
				for (String url : batch) {
					this.executorService.submit(() -> {
						List<String> links = webCrawler.processNewUrls(url, task);
						for(String link : links) {
							if(!visitedUrls.contains(link)) {
								urlsToVisit.add(link);
							}
						}
					});
				}
				if(urlsToVisit.isEmpty()) {
					Thread.sleep(5000);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt(); // Reset interrupted status
			e.printStackTrace();
		} finally {
			shutdownExecutorService(this.executorService);
		}
	}

	private void shutdownExecutorService(ExecutorService executorService) {
		executorService.shutdown();
		try {
			if (!executorService.awaitTermination(30, TimeUnit.SECONDS)) {
				executorService.shutdownNow();
			}
		} catch (InterruptedException e) {
			executorService.shutdownNow();
			Thread.currentThread().interrupt();
		}
	}

	private String normalizeURL(String url) {
		return url.replaceAll("\\.\\./", "");
	}
}
