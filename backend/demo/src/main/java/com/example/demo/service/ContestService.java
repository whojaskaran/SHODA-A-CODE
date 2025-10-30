package com.example.demo.service;

import com.example.demo.model.Contest;
import com.example.demo.model.Problem;
import com.example.demo.repository.ContestRepository;
import com.example.demo.repository.ProblemRepository;
import com.example.demo.repository.SubmissionRepository;
import com.example.demo.model.Submission;
import com.example.demo.model.SubmissionStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ContestService {
    private final ContestRepository contests;
    private final ProblemRepository problems;
    private final SubmissionRepository submissions;

    public ContestService(ContestRepository contests, ProblemRepository problems, SubmissionRepository submissions) {
        this.contests = contests;
        this.problems = problems;
        this.submissions = submissions;
    }

	public Optional<Contest> getContest(String id) { return contests.findById(id); }
	public Contest save(Contest contest) { return contests.save(contest); }
	public Problem saveProblem(Problem problem) { return problems.save(problem); }
    public List<Map<String, Object>> getLeaderboard(String contestId) {
        java.util.Map<String, java.util.Set<String>> userScores = new java.util.HashMap<>();
        for (Submission s : submissions.findAll()) {
            if (!contestId.equals(s.getContestId())) continue;
            if (s.getStatus() != SubmissionStatus.ACCEPTED) continue;
            userScores.computeIfAbsent(s.getUserId(), k -> new java.util.HashSet<>()).add(s.getProblemId());
        }
        java.util.List<Map<String, Object>> rows = new java.util.ArrayList<>();
        for (var e : userScores.entrySet()) {
            java.util.Map<String, Object> row = new java.util.HashMap<>();
            row.put("userId", e.getKey());
            row.put("username", e.getKey());
            row.put("score", e.getValue().size());
            rows.add(row);
        }
        rows.sort((a,b) -> Integer.compare((int)b.getOrDefault("score",0), (int)a.getOrDefault("score",0)));
        return rows;
    }
	public java.util.Collection<Contest> list() { return contests.findAll(); }
}


