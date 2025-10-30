package com.example.demo.repository;

import com.example.demo.model.User;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserRepository {
	private final Map<String, User> map = new ConcurrentHashMap<>();
	public User save(User u){ map.put(u.getId(), u); return u; }
	public Optional<User> findById(String id){ return Optional.ofNullable(map.get(id)); }
	public Optional<User> findByUsername(String username){ return map.values().stream().filter(u -> username.equals(u.getUsername())).findFirst(); }
	public Collection<User> findAll(){ return map.values(); }
}


