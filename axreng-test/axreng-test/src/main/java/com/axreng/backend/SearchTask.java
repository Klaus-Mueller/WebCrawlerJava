package com.axreng.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SearchTask {
	
	public enum Status {
	    ACTIVE,
	    DONE
	}
	
	private String id;
	private String keyword;
	private List<String> results = new ArrayList<>();
	private Status status;
	private static final int MIN_KEYWORD_LENGTH = 4;
	private static final int MAX_KEYWORD_LENGTH = 32;

	public SearchTask(String keyword) {
		this.validateKeyword(keyword);
		this.generateTaskId();
		this.keyword = keyword;
		this.status = Status.ACTIVE;
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
		this.validateKeyword(keyword);
		this.keyword = keyword;
	}

	public List<String> getResults() {
		return results;
	}

	public void addResult(String result) {
		this.results.add(result);
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	private void generateTaskId() {
		// Generate a random UUID
		String uuid = UUID.randomUUID().toString().replace("-", "");
		// Take the first 8 characters
		this.setId(uuid.substring(0, 8)); 
	}
	
	private void validateKeyword(String keyword) {
		if (keyword == null || keyword.length() < MIN_KEYWORD_LENGTH || keyword.length() > MAX_KEYWORD_LENGTH) {
			throw new IllegalArgumentException("Keyword must have length between " + MIN_KEYWORD_LENGTH + " and " + MAX_KEYWORD_LENGTH + " characters.");
		}
	}
}
