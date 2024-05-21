package com.axreng.backend;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchTaskManager {
	private Map<String, SearchTask> tasks = new ConcurrentHashMap<>();
	private ExecutorService executorService = Executors.newCachedThreadPool();
	private WebCrawler webCrawler;

	public SearchTaskManager (WebCrawler webCrawler) {
		this.webCrawler = webCrawler;
	}

	public String startSearch(String keyword) {
		SearchTask task = new SearchTask(keyword);
		tasks.put(task.getId(), task);
		executorService.submit(() -> webCrawler.crawl(task));
		return task.getId();
	}

	public SearchTask getActiveSearch(String taskId) {
		SearchTask task = tasks.get(taskId);
		if (task != null && task.getStatus() == SearchTask.Status.ACTIVE) {
			return task;
		}
		return null;
	}

	public SearchTask getCompletedSearch(String taskId) {
		SearchTask task = tasks.get(taskId);
		if (task != null && task.getStatus() == SearchTask.Status.DONE) {
			return task;
		}
		return null;
	}

	public SearchTask getTask(String taskId) {
		SearchTask task = tasks.get(taskId);
		if (task != null) {
			return task;
		}
		return null;
	}

}