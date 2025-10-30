package com.example.demo.model;

public class TestCaseResult {
    private String name;
    private boolean passed;
    private String output;
    private String expectedOutput;

    public TestCaseResult(String name, boolean passed, String output, String expectedOutput) {
        this.name = name;
        this.passed = passed;
        this.output = output;
        this.expectedOutput = expectedOutput;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPassed() {
        return passed;
    }

    public void setPassed(boolean passed) {
        this.passed = passed;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getExpectedOutput() {
        return expectedOutput;
    }

    public void setExpectedOutput(String expectedOutput) {
        this.expectedOutput = expectedOutput;
    }
}
