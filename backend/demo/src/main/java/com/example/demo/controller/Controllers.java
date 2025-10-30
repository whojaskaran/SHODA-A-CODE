package com.example.demo.controller;

import com.example.demo.model.Contest;
import com.example.demo.model.Submission;
import com.example.demo.service.ContestService;
import com.example.demo.service.SubmissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/contests")
class ContestController {
	private final ContestService service;
	ContestController(ContestService service){ this.service=service; }
	@GetMapping public Collection<Contest> getAll(){ return service.list(); }
	@GetMapping("/{contestId}") public ResponseEntity<Contest> getById(@PathVariable String contestId){ return service.getContest(contestId).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
    @GetMapping("/{contestId}/leaderboard") public ResponseEntity<List<Map<String,Object>>> leaderboard(@PathVariable String contestId){ return ResponseEntity.ok(service.getLeaderboard(contestId)); }
}

@RestController
@RequestMapping("/api/submissions")
class SubmissionController {
	private final SubmissionService service;
	SubmissionController(SubmissionService service){ this.service=service; }
	@PostMapping public ResponseEntity<Submission> submit(@RequestBody Submission submission){ Submission saved=service.create(submission); return ResponseEntity.created(URI.create("/api/submissions/"+saved.getId())).body(saved); }
	@GetMapping("/{id}") public ResponseEntity<Submission> getById(@PathVariable String id){ return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }
}


