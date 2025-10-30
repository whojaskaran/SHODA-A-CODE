package com.example.demo.model;

import java.util.List;

public class RunResult {
    private final String output;
    private final List<TestCaseResult> testCaseResults;

    public RunResult(String output, List<TestCaseResult> testCaseResults) {
        this.output = output;
        this.testCaseResults = testCaseResults;
    }

    public String getOutput() {
        return output;
    }

    public List<TestCaseResult> getTestCaseResults() {
        return testCaseResults;
    }
}
