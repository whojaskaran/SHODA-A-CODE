package com.example.demo.repository;

import com.example.demo.model.Submission;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SubmissionRepository {
	private final Map<String, Submission> map = new ConcurrentHashMap<>();
	public Submission save(Submission s){ map.put(s.getId(), s); return s; }
	public Optional<Submission> findById(String id){ return Optional.ofNullable(map.get(id)); }
	public Collection<Submission> findAll(){ return map.values(); }
}


