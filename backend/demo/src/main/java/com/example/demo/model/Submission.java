package com.example.demo.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Submission {
	private final String id = UUID.randomUUID().toString();
	private String contestId;
	private String problemId;
	private String userId;
	private String language = "java";
	private String sourceCode;
	private SubmissionStatus status = SubmissionStatus.PENDING;
	private String resultMessage;
	private Instant createdAt = Instant.now();
	private List<TestCaseResult> testCaseResults = new ArrayList<>();

	public Submission() {}

	public Submission(String contestId, String problemId, String userId, String sourceCode) {
		this.contestId = contestId;
		this.problemId = problemId;
		this.userId = userId;
		this.sourceCode = sourceCode;
	}

	public String getId() { return id; }
	public String getContestId() { return contestId; }
	public void setContestId(String contestId) { this.contestId = contestId; }
	public String getProblemId() { return problemId; }
	public void setProblemId(String problemId) { this.problemId = problemId; }
	public String getUserId() { return userId; }
	public void setUserId(String userId) { this.userId = userId; }
	public String getLanguage() { return language; }
	public void setLanguage(String language) { this.language = language; }
	public String getSourceCode() { return sourceCode; }
	public void setSourceCode(String sourceCode) { this.sourceCode = sourceCode; }
	public SubmissionStatus getStatus() { return status; }
	public void setStatus(SubmissionStatus status) { this.status = status; }
	public String getResultMessage() { return resultMessage; }
	public void setResultMessage(String resultMessage) { this.resultMessage = resultMessage; }
	public Instant getCreatedAt() { return createdAt; }

	public List<TestCaseResult> getTestCaseResults() {
		return testCaseResults;
	}

	public void setTestCaseResults(List<TestCaseResult> testCaseResults) {
		this.testCaseResults = testCaseResults;
	}
}


