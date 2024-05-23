package com.axreng.backend;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import com.axreng.backend.util.PageCache;

public class SearchTaskManager {
	private Map<String, SearchTask> tasks = new ConcurrentHashMap<>();
	private static final int NUM_THREADS = 5;
	private ExecutorService executorService;
	private final PageCache pageCache;
	private String baseURL;

	public SearchTaskManager (String baseURL) {
		this.executorService = Executors.newFixedThreadPool(NUM_THREADS);
		this.baseURL = baseURL;
		this.pageCache = new PageCache();
	}

	public String startSearch(String keyword) {
		SearchTask task = new SearchTask(keyword);
		tasks.put(task.getId(), task);
		executorService.submit(() -> new WebCrawler(this.baseURL, this.pageCache).crawl(task));
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