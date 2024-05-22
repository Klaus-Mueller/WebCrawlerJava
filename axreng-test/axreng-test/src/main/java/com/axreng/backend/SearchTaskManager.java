package com.axreng.backend;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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

	public List<SearchTask> getActiveSearchTaks() {
		return tasks.values()
				.stream()
				.filter(task -> task.getStatus().equals(SearchTask.Status.ACTIVE))
				.collect(Collectors.toList());
	}

	public List<SearchTask> getCompletedSearchTaks() {
		return tasks.values()
				.stream()
				.filter(task -> task.getStatus().equals(SearchTask.Status.DONE))
				.collect(Collectors.toList());
	}

	public SearchTask getTask(String taskId) {
		SearchTask task = tasks.get(taskId);
		if (task != null) {
			return task;
		}
		return null;
	}

}