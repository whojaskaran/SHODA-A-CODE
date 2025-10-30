package com.example.demo.model;

import java.util.UUID;

public class User {
	private final String id = UUID.randomUUID().toString();
	private String username;

	public User() {}

	public User(String username) { this.username = username; }

	public String getId() { return id; }

	public String getUsername() { return username; }

	public void setUsername(String username) { this.username = username; }
}



