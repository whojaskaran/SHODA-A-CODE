package com.example.demo.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Problem {
	private final String id = UUID.randomUUID().toString();
	private String title;
	private String description;
	private List<TestCase> testCases = new ArrayList<>();

	public Problem() {}

	public Problem(String title, String description) {
		this.title = title;
		this.description = description;
	}

	public String getId() { return id; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public List<TestCase> getTestCases() { return testCases; }
	public void setTestCases(List<TestCase> testCases) { this.testCases = testCases; }
}


