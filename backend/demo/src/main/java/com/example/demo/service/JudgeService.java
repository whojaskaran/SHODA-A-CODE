
package com.example.demo.service;

import com.example.demo.model.*;
import com.example.demo.repository.ProblemRepository;
import com.example.demo.repository.SubmissionRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JudgeService {
	private final SubmissionRepository submissions;
	private final ProblemRepository problems;
	private final ExecutorService executor = Executors.newFixedThreadPool(2);

	public JudgeService(SubmissionRepository submissions, ProblemRepository problems) {
		this.submissions = submissions;
		this.problems = problems;
	}

	public void enqueue(Submission submission) {
		submission.setStatus(SubmissionStatus.PENDING);
		submissions.save(submission);
		executor.submit(() -> runSubmission(submission));
	}

	public RunResult runCode(Submission submission) throws IOException, InterruptedException {
		Problem problem = problems.findById(submission.getProblemId()).orElse(null);
		if (problem == null) {
			throw new IllegalArgumentException("Problem not found");
		}
		List<TestCase> testCases = problem.getTestCases().subList(0, Math.min(2, problem.getTestCases().size()));
		return judge(submission, testCases, false);
	}

	private void runSubmission(Submission submission) {
		submission.setStatus(SubmissionStatus.RUNNING);
		submissions.save(submission);
		Problem problem = problems.findById(submission.getProblemId()).orElse(null);
		if (problem == null) {
			submission.setStatus(SubmissionStatus.FAILED);
			submission.setResultMessage("Problem not found");
			submissions.save(submission);
			return;
		}
		try {
			judge(submission, problem.getTestCases(), true);
		} catch (Exception e) {
			submission.setStatus(SubmissionStatus.FAILED);
			submission.setResultMessage("Judge error: " + e.getMessage());
			submissions.save(submission);
		}
	}

	private RunResult judge(Submission submission, List<TestCase> testCases, boolean isSubmit) throws IOException, InterruptedException {
		return switch (submission.getLanguage()) {
			case "java" -> judgeJava(submission, testCases, isSubmit);
			case "cpp" -> judgeCpp(submission, testCases, isSubmit);
			case "python" -> judgePython(submission, testCases, isSubmit);
			case "javascript" -> judgeJavascript(submission, testCases, isSubmit);
			default -> throw new IllegalArgumentException("Unsupported language: " + submission.getLanguage());
		};
	}

	private RunResult judgeJava(Submission submission, List<TestCase> testCases, boolean isSubmit) throws IOException, InterruptedException {
		return judgeGeneric(submission, testCases, isSubmit, "Main.java", "javac Main.java", "java Main");
	}

	private RunResult judgeCpp(Submission submission, List<TestCase> testCases, boolean isSubmit) throws IOException, InterruptedException {
		return judgeGeneric(submission, testCases, isSubmit, "main.cpp", "g++ main.cpp -o main", "./main");
	}

	private RunResult judgePython(Submission submission, List<TestCase> testCases, boolean isSubmit) throws IOException, InterruptedException {
		return judgeGeneric(submission, testCases, isSubmit, "main.py", null, "python3 main.py");
	}

	private RunResult judgeJavascript(Submission submission, List<TestCase> testCases, boolean isSubmit) throws IOException, InterruptedException {
		return judgeGeneric(submission, testCases, isSubmit, "main.js", null, "node main.js");
	}

	private RunResult judgeGeneric(Submission submission, List<TestCase> testCases, boolean isSubmit, String sourceName, String compileCommand, String runCommand) throws IOException, InterruptedException {
		String base = Optional.ofNullable(System.getenv("JUDGE_WORKDIR"))
				.orElse(System.getProperty("user.home") + "/.shodh/judge");
		File baseDir = new File(base);
		if (!baseDir.exists()) {
			baseDir.mkdirs();
		}
		File dir = Files.createTempDirectory(baseDir.toPath(), "job_").toFile();
		File file = new File(dir, sourceName);
		try (FileWriter fw = new FileWriter(file, StandardCharsets.UTF_8)) {
			fw.write(submission.getSourceCode());
		}
		String mountPath = dir.getAbsolutePath();

		String judgeImage = Optional.ofNullable(System.getenv("JUDGE_IMAGE")).orElse("shodh-judge:latest");

		if (compileCommand != null) {
			Process compile = new ProcessBuilder("docker", "run", "--rm", "-v", mountPath + ":/work:z", "-w", "/work", judgeImage, "sh", "-c", compileCommand).redirectErrorStream(true).start();
			int compileExit = compile.waitFor();
			if (compileExit != 0) {
				String log = new String(compile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
				if (isSubmit) {
					submission.setStatus(SubmissionStatus.COMPILE_ERROR);
					submission.setResultMessage(log.isBlank() ? ("Compilation failed (exit=" + compileExit + ")") : truncate(log, 600));
					submissions.save(submission);
				}
				cleanup(dir);
				return new RunResult(log, new ArrayList<>());
			}
		}

		List<TestCaseResult> results = new ArrayList<>();
		boolean allPassed = true;
		StringBuilder consoleOutput = new StringBuilder();

		for (int i = 0; i < testCases.size(); i++) {
			TestCase tc = testCases.get(i);
			File inputFile = new File(dir, "input.txt");
			try (FileWriter fw = new FileWriter(inputFile, StandardCharsets.UTF_8)) {
				fw.write(tc.getInput());
			}
			Process run = new ProcessBuilder("docker", "run", "--rm", "-m", "256m", "--cpus", "0.5", "-v", mountPath + ":/work:z", "-w", "/work", judgeImage, "sh", "-c", "timeout 3s " + runCommand + " < /work/input.txt").redirectErrorStream(true).start();
			boolean finished = run.waitFor(Duration.ofSeconds(5).toMillis(), java.util.concurrent.TimeUnit.MILLISECONDS);
			String output = new String(run.getInputStream().readAllBytes(), StandardCharsets.UTF_8).trim();
			consoleOutput.append("Test Case ").append(i + 1).append(" output:\n").append(output).append("\n\n");

			if (!finished) {
				run.destroyForcibly();
				allPassed = false;
				if (isSubmit) {
					submission.setStatus(SubmissionStatus.TIME_LIMIT_EXCEEDED);
					submission.setResultMessage("Time Limit Exceeded on Test Case " + (i + 1));
				}
				results.add(new TestCaseResult("Test Case " + (i + 1), false, "TLE", tc.getExpectedOutput()));
				if (isSubmit) {
					break;
				}
			} else {
				String expected = tc.getExpectedOutput().trim();
				boolean passed = output.equals(expected);
				if (!passed) {
					allPassed = false;
				}
				results.add(new TestCaseResult("Test Case " + (i + 1), passed, output, expected));
			}
		}

		if (isSubmit) {
			submission.setTestCaseResults(results);
			if (submission.getStatus() != SubmissionStatus.TIME_LIMIT_EXCEEDED) {
				if (allPassed) {
					submission.setStatus(SubmissionStatus.ACCEPTED);
					submission.setResultMessage("All tests passed");
				}
				else {
					submission.setStatus(SubmissionStatus.WRONG_ANSWER);
					long failedCount = results.stream().filter(r -> !r.isPassed()).count();
					submission.setResultMessage(failedCount + " of " + testCases.size() + " tests failed.");
				}
			}
			submissions.save(submission);
		}

		cleanup(dir);
		return new RunResult(consoleOutput.toString(), results);
	}

	private void cleanup(File dir) {
		try {
			Files.walk(dir.toPath()).sorted((a, b) -> b.getNameCount() - a.getNameCount()).forEach(p -> {
				try {
					Files.deleteIfExists(p);
				} catch (IOException ignored) {
				}
			});
		} catch (IOException ignored) {
		}
	}

	private String truncate(String s, int max) {
		if (s == null) return "";
		if (s.length() <= max) return s;
		return s.substring(0, max) + "...";
	}
}

