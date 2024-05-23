package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axreng.backend.SearchTask;
import com.axreng.backend.WebCrawler;

public class WebCrawlerTest {
	
	private WebCrawler webCrawler;
    private String baseURL;
    private Set<String> visitedUrls;

    @BeforeEach
    public void setUp() {
        baseURL = "http://example.com";
        webCrawler = new WebCrawler(baseURL);
        visitedUrls = new HashSet<>();
    }

	@Test
	public void testExtractLinks() {
		// Test case 1: Valid page content with one link
		String pageContent1 = "<a href=\"http://example.com/page1\"></a>";
		List<String> links1 = webCrawler.extractLinks(pageContent1);
		assertEquals(1, links1.size());
		assertEquals("http://example.com/page1", links1.get(0));

		// Test case 2: Valid page content with multiple links
		String pageContent2 = "<a href=\"http://example.com/page1\"></a>" +
				"<a href=\"http://example.com/page2\"></a>";
		List<String> links2 = webCrawler.extractLinks(pageContent2);
		assertEquals(2, links2.size());
		assertEquals("http://example.com/page1", links2.get(0));
		assertEquals("http://example.com/page2", links2.get(1));

		// Test case 3: Page content without any links
		String pageContent3 = "This is a sample page content without any links.";
		List<String> links3 = webCrawler.extractLinks(pageContent3);
		assertEquals(0, links3.size());

		// Test case 4: Page content with malformed link
		String pageContent4 = "<a href=\"http://anotherExample.com/page1\"></a>";
		List<String> links4 = webCrawler.extractLinks(pageContent4);
		assertEquals(0, links4.size()); // Malformed link should be ignored
	}

	@Test
	public void testExtractLinkThread() {
		// Test case 5: Large page content with multiple links (to test threading)
		StringBuilder largePageContent = new StringBuilder();
		largePageContent.append("<html><body>");
		for (int i = 0; i < 10000; i++) {
			largePageContent.append("<a href=\"http://example.com/page").append(i).append("\"></a>");
		}
		largePageContent.append("</body></html>");

		List<String> links = webCrawler.extractLinks(largePageContent.toString());

		// Assert that the number of links extracted matches the expected count
		assertEquals(10000, links.size());
	}

	@Test
	public void testCrawl() {
		// Create a mock SearchTask
		SearchTask taskMock = mock(SearchTask.class);

		long startTime = System.currentTimeMillis(); 
		// Call the crawl method
		webCrawler.crawl(taskMock);
		long endTime = System.currentTimeMillis();
		int elapsedTimeSecs = (int) (endTime - startTime) / 1000;

		verify(taskMock).completeTask(elapsedTimeSecs);
	}

	@Test
	public void testProcessNewUrls() {
		// Set up the test
		String baseURL = "http://example.com";
		SearchTask searchTask = new SearchTask("documents");

		// Call the processNewUrls method
		List<String> links = webCrawler.processNewUrls(baseURL, searchTask);

		// Assert the results
		assertNotNull(links);
	}

	@Test
	public void testInvalidURL() {
		String visitedURL = "http://example.com/page1";
		String invalidURL = "http://anotherexample.com/page1";
		String mailToURL = "mailto:test@example.com";
		String ftpURL = "ftp://example.com";
		visitedUrls.add(visitedURL);

		assertTrue(webCrawler.isInvalidURL(null, visitedUrls));
		assertTrue(webCrawler.isInvalidURL(visitedURL, visitedUrls));
		assertTrue(webCrawler.isInvalidURL(invalidURL, visitedUrls));
		assertTrue(webCrawler.isInvalidURL(mailToURL, visitedUrls));
		assertTrue(webCrawler.isInvalidURL(ftpURL, visitedUrls));

		String validURL = "http://example.com/page2";
		assertFalse(webCrawler.isInvalidURL(validURL, visitedUrls));
	}
}
