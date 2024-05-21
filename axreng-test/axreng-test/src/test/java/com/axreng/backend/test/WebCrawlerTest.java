package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.axreng.backend.WebCrawler;

public class WebCrawlerTest {
	
    @Test
    public void testExtractLinks() {
        // Test case 1: Valid page content with one link
        String pageContent1 = "<a href=\"http://example.com/page1\"></a>";
        WebCrawler webCrawler1 = new WebCrawler("http://example.com");
        List<String> links1 = webCrawler1.extractLinks(pageContent1);
        assertEquals(1, links1.size());
        assertEquals("http://example.com/page1", links1.get(0));

        // Test case 2: Valid page content with multiple links
        String pageContent2 = "<a href=\"http://example.com/page1\"></a>" +
                             "<a href=\"http://example.com/page2\"></a>";
        WebCrawler webCrawler2 = new WebCrawler("http://example.com");
        List<String> links2 = webCrawler2.extractLinks(pageContent2);
        assertEquals(2, links2.size());
        assertEquals("http://example.com/page1", links2.get(0));
        assertEquals("http://example.com/page2", links2.get(1));

        // Test case 3: Page content without any links
        String pageContent3 = "This is a sample page content without any links.";
        WebCrawler webCrawler3 = new WebCrawler("http://example.com");
        List<String> links3 = webCrawler3.extractLinks(pageContent3);
        assertEquals(0, links3.size());

        // Test case 4: Page content with malformed link
        String pageContent4 = "<a href=\"http://anotherExample.com/page1\"></a>";
        WebCrawler webCrawler4 = new WebCrawler("http://example.com");
        List<String> links4 = webCrawler4.extractLinks(pageContent4);
        assertEquals(0, links4.size()); // Malformed link should be ignored
    }
}
