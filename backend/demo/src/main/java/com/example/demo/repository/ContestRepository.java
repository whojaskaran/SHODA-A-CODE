package com.example.demo.repository;

import com.example.demo.model.Contest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ContestRepository {
	private final Map<String, Contest> map = new ConcurrentHashMap<>();
	public Contest save(Contest c){ map.put(c.getId(), c); return c; }
	public Optional<Contest> findById(String id){ return Optional.ofNullable(map.get(id)); }
	public Collection<Contest> findAll(){ return map.values(); }
}


