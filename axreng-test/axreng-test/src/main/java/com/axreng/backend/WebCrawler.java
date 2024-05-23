package com.axreng.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axreng.backend.util.HttpHelper;
import com.axreng.backend.util.PageCache;

public class WebCrawler {
	private final String baseURL;
	private final PageCache pageCache;
	private ConcurrentLinkedQueue<String> urlsToVisit = new ConcurrentLinkedQueue<>();
	private static final Logger logger = LoggerFactory.getLogger(WebCrawler.class);

	public WebCrawler(String baseURL, PageCache pageCache) {
		super();
		this.baseURL = baseURL;
		this.pageCache = pageCache;
	}

	public void crawl(SearchTask task) {
		initializeURLsToVisit();
		long startTime = System.currentTimeMillis(); 
		WebCrawlerThreadManager threadManager = new WebCrawlerThreadManager();
		threadManager.executeCrawling(this, urlsToVisit, task);
		logger.info("Search completed for keyword: {} Results: {}", task.getKeyword(), task.getResults());
		long endTime = System.currentTimeMillis();
		int elapsedTimeSecs = (int) (endTime - startTime) / 1000;
		task.completeTask(elapsedTimeSecs);
		logger.info("Crawl method runtime: {} seconds:", elapsedTimeSecs);
	}

	public List<String> processNewUrls(String url, SearchTask task) {
		String pageContent = pageCache.get(url);
		if (pageContent == null) {
			pageContent = fetchPageContent(url);
			if (pageContent == null) {
				logger.error("Error fetching content from: {} ", url);
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

	public boolean isInvalidURL(String url, Set<String> visitedUrls) {
		return (url == null || visitedUrls.contains(url) || !url.startsWith(this.baseURL) || url.contains("mailto:")
				|| url.contains("ftp:"));
	}

	private void initializeURLsToVisit() {
		this.urlsToVisit.add(this.baseURL);
		String pageContent = pageCache.get(this.baseURL);
		if (pageContent == null) {
			pageContent = fetchPageContent(this.baseURL);
			if (pageContent == null) {
				logger.error("Error fetching content from: {} ", this.baseURL);
				return;
			}
			pageCache.put(this.baseURL, pageContent);
			List<String> links = extractLinks(pageContent);
			urlsToVisit.addAll(links);
		}
	}
}
