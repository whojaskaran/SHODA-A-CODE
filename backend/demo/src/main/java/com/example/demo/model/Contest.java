package com.example.demo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Contest {
	private final String id = UUID.randomUUID().toString();
	private String name;
	private Instant startTime = Instant.now();
	private Instant endTime = Instant.now().plusSeconds(3600);
	private List<Problem> problems = new ArrayList<>();

	public Contest() {}

	public Contest(String name) { this.name = name; }

	public String getId() { return id; }
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	public Instant getStartTime() { return startTime; }
	public void setStartTime(Instant startTime) { this.startTime = startTime; }
	public Instant getEndTime() { return endTime; }
	public void setEndTime(Instant endTime) { this.endTime = endTime; }
	public List<Problem> getProblems() { return problems; }
	public void setProblems(List<Problem> problems) { this.problems = problems; }
}


