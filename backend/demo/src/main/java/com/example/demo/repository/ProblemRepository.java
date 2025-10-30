package com.example.demo.repository;

import com.example.demo.model.Problem;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ProblemRepository {
	private final Map<String, Problem> map = new ConcurrentHashMap<>();
	public Problem save(Problem p){ map.put(p.getId(), p); return p; }
	public Optional<Problem> findById(String id){ return Optional.ofNullable(map.get(id)); }
	public Collection<Problem> findAll(){ return map.values(); }
}


