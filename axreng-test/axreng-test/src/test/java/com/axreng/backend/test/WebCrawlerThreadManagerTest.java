package com.axreng.backend.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axreng.backend.SearchTask;
import com.axreng.backend.WebCrawler;
import com.axreng.backend.WebCrawlerThreadManager;

public class WebCrawlerThreadManagerTest {

	private ConcurrentLinkedQueue<String> urlsToVisitMock;
	private SearchTask taskMock;

	@BeforeEach
	public void setUp() {
		// Initialize mocks
		urlsToVisitMock = new ConcurrentLinkedQueue<String>();
		taskMock = mock(SearchTask.class);

		// Add some URLs to the queue
		urlsToVisitMock.add("http://example.com/page1");
		urlsToVisitMock.add("http://example.com/page2");
	}

	@Test
	public void testExecuteCrawling() {
		// Create mocks
		WebCrawler webCrawlerMock = mock(WebCrawler.class);

		// Initialize the thread manager
		WebCrawlerThreadManager threadManager = new WebCrawlerThreadManager();

		// Execute the crawling
		threadManager.executeCrawling(webCrawlerMock, urlsToVisitMock, taskMock);

		// Verify that the webCrawlerMock's method is called
		verify(webCrawlerMock, atLeastOnce()).isInvalidURL(anyString(), any());
		verify(webCrawlerMock, atLeastOnce()).processNewUrls(anyString(), eq(taskMock));
	}
}

