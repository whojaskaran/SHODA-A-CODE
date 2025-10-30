package com.example.demo.service;

import com.example.demo.model.Submission;
import com.example.demo.repository.SubmissionRepository;

import java.util.Optional;

public class SubmissionService {
	private final SubmissionRepository submissions;
	private final JudgeService judges;

	public SubmissionService(SubmissionRepository submissions, JudgeService judges) {
		this.submissions = submissions;
		this.judges = judges;
	}

	public Submission create(Submission submission) {
		submissions.save(submission);
		judges.enqueue(submission);
		return submission;
	}

	public Optional<Submission> findById(String id) {
		return submissions.findById(id);
	}
}


