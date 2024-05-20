package com.axreng.backend;

import java.util.ArrayList;
import java.util.List;

public class SearchTask {

	private String id;
	private String keyword;
	private List<String> results = new ArrayList<>();

	public SearchTask(String keyword) {
		super();
		this.keyword = keyword;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<String> getResults() {
		return results;
	}

	public void addResult(String result) {
		this.results.add(result);
	}
}