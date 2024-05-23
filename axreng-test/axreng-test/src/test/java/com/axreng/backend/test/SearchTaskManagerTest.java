package com.axreng.backend.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import com.axreng.backend.SearchTask;
import com.axreng.backend.SearchTaskManager;

public class SearchTaskManagerTest {
	private final String baseURL = "http://example.com";
	private final ExecutorService executorService = Executors.newFixedThreadPool(10); // Number of concurrent requests
	private final SearchTaskManager searchTaskManager = new SearchTaskManager(baseURL);

	@Test
	public void testStartSearch() {
		SearchTaskManager searchTaskManager = new SearchTaskManager(baseURL);
		String taskId = searchTaskManager.startSearch("test");
		// Assert
		assertNotNull(taskId);
		SearchTask taskResult = searchTaskManager.getTask(taskId);
		assertEquals(taskResult.getKeyword(), "test");
	}

	@RepeatedTest(10) // Number of repeated tests (concurrent requests)
	public void testConcurrentRequests() throws InterruptedException {
		// Submit the crawl task to the ExecutorService for concurrent execution
		executorService.submit(() -> {
			// Simulate some processing time
			try {
				String taskId = this.searchTaskManager.startSearch("test123");
				Thread.sleep(1000); // Simulate processing time
				assertNotNull(taskId);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	// Shutdown the ExecutorService after all tests are completed
	protected void afterAll() throws InterruptedException {
		executorService.shutdown();
		executorService.awaitTermination(5, TimeUnit.SECONDS);
	}
}
