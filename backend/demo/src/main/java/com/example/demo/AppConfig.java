package com.example.demo;

import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class AppConfig {

	@Bean public ContestRepository contestRepository() { return new ContestRepository(); }
	@Bean public ProblemRepository problemRepository() { return new ProblemRepository(); }
	@Bean public SubmissionRepository submissionRepository() { return new SubmissionRepository(); }
	@Bean public UserRepository userRepository() { return new UserRepository(); }

	@Bean public JudgeService judgeService(SubmissionRepository s, ProblemRepository p) { return new JudgeService(s, p); }
    @Bean public ContestService contestService(ContestRepository c, ProblemRepository p, SubmissionRepository s) { return new ContestService(c, p, s); }
	@Bean public SubmissionService submissionService(SubmissionRepository s, JudgeService j) { return new SubmissionService(s, j); }

	@Bean
	public CommandLineRunner seed(ContestService contestService, ProblemRepository problemRepository) {
		return args -> {
			// Contest 1: Data Structures Fundamentals
			Contest contest1 = new Contest("Data Structures Fundamentals");
			Problem p1_1 = new Problem("Reverse a Linked List", "Given the head of a singly linked list, reverse the list, and return the reversed list.");
			p1_1.setTestCases(List.of(new TestCase("1 2 3 4 5", "5 4 3 2 1")));
			Problem p1_2 = new Problem("Implement a Queue using Stacks", "Implement a first in, first out (FIFO) queue using only two stacks.");
			p1_2.setTestCases(List.of(new TestCase("push 1\npush 2\npeek", "1")));
			Problem p1_3 = new Problem("Find the middle of a Linked List", "Given the head of a singly linked list, return the middle node of the linked list.");
			p1_3.setTestCases(List.of(new TestCase("1 2 3 4 5", "3")));
			Problem p1_4 = new Problem("Check for Balanced Parentheses", "Given a string containing just the characters '(', ')', '{', '}', '[' and ']', determine if the input string is valid.");
			p1_4.setTestCases(List.of(new TestCase("()[]{}", "true"), new TestCase("(]", "false")));
			Problem p1_5 = new Problem("Binary Tree Inorder Traversal", "Given the root of a binary tree, return the inorder traversal of its nodes' values.");
			p1_5.setTestCases(List.of(new TestCase("[1,null,2,3]", "1 3 2")));
			problemRepository.save(p1_1); problemRepository.save(p1_2); problemRepository.save(p1_3); problemRepository.save(p1_4); problemRepository.save(p1_5);
			contest1.setProblems(List.of(p1_1, p1_2, p1_3, p1_4, p1_5));
			contestService.save(contest1);
			System.out.println("Seeded contestId: " + contest1.getId());

			// Contest 2: Algorithmic Challenges
			Contest contest2 = new Contest("Algorithmic Challenges");
			Problem p2_1 = new Problem("FizzBuzz", "Given an integer n, return a string array answer (1-indexed) where answer[i] == \"FizzBuzz\" if i is divisible by 3 and 5, answer[i] == \"Fizz\" if i is divisible by 3, answer[i] == \"Buzz\" if i is divisible by 5, answer[i] == i (as a string) if none of the above conditions are true.");
			p2_1.setTestCases(List.of(new TestCase("3", "1\n2\nFizz")));
			Problem p2_2 = new Problem("Fibonacci Sequence", "The Fibonacci numbers, commonly denoted F(n) form a sequence, called the Fibonacci sequence, such that each number is the sum of the two preceding ones, starting from 0 and 1.");
			p2_2.setTestCases(List.of(new TestCase("5", "5")));
			Problem p2_3 = new Problem("Merge Two Sorted Arrays", "You are given two integer arrays nums1 and nums2, sorted in non-decreasing order, and two integers m and n, representing the number of elements in nums1 and nums2 respectively.");
			p2_3.setTestCases(List.of(new TestCase("[1,2,3,0,0,0]\n3\n[2,5,6]\n3", "[1,2,2,3,5,6]")));
			Problem p2_4 = new Problem("Valid Anagram", "Given two strings s and t, return true if t is an anagram of s, and false otherwise.");
			p2_4.setTestCases(List.of(new TestCase("anagram\nnagaram", "true")));
			Problem p2_5 = new Problem("Maximum Subarray", "Given an integer array nums, find the contiguous subarray (containing at least one number) which has the largest sum and return its sum.");
			p2_5.setTestCases(List.of(new TestCase("[-2,1,-3,4,-1,2,1,-5,4]", "6")));
			problemRepository.save(p2_1); problemRepository.save(p2_2); problemRepository.save(p2_3); problemRepository.save(p2_4); problemRepository.save(p2_5);
			contest2.setProblems(List.of(p2_1, p2_2, p2_3, p2_4, p2_5));
			contestService.save(contest2);
			System.out.println("Seeded contestId: " + contest2.getId());

			// Contest 3: Dynamic Programming
			Contest contest3 = new Contest("Dynamic Programming");
			Problem p3_1 = new Problem("Climbing Stairs", "You are climbing a staircase. It takes n steps to reach the top. Each time you can either climb 1 or 2 steps. In how many distinct ways can you climb to the top?");
			p3_1.setTestCases(List.of(new TestCase("3", "3")));
			Problem p3_2 = new Problem("Coin Change", "You are given an integer array coins representing coins of different denominations and an integer amount representing a total amount of money. Return the fewest number of coins that you need to make up that amount.");
			p3_2.setTestCases(List.of(new TestCase("[1,2,5]\n11", "3")));
			Problem p3_3 = new Problem("Longest Increasing Subsequence", "Given an integer array nums, return the length of the longest strictly increasing subsequence.");
			p3_3.setTestCases(List.of(new TestCase("[10,9,2,5,3,7,101,18]", "4")));
			Problem p3_4 = new Problem("Word Break", "Given a string s and a dictionary of strings wordDict, return true if s can be segmented into a space-separated sequence of one or more dictionary words.");
			p3_4.setTestCases(List.of(new TestCase("leetcode\n[\"leet\",\"code\"]", "true")));
			Problem p3_5 = new Problem("House Robber", "You are a professional robber planning to rob houses along a street. Each house has a certain amount of money stashed, the only constraint stopping you from robbing each of them is that adjacent houses have security systems connected and it will automatically contact the police if two adjacent houses were broken into on the same night.");
			p3_5.setTestCases(List.of(new TestCase("[2,7,9,3,1]", "12")));
			problemRepository.save(p3_1); problemRepository.save(p3_2); problemRepository.save(p3_3); problemRepository.save(p3_4); problemRepository.save(p3_5);
			contest3.setProblems(List.of(p3_1, p3_2, p3_3, p3_4, p3_5));
			contestService.save(contest3);
			System.out.println("Seeded contestId: " + contest3.getId());

			// Contest 4: Graphs and Trees
			Contest contest4 = new Contest("Graphs and Trees");
			Problem p4_1 = new Problem("Number of Islands", "Given an m x n 2D binary grid grid which represents a map of '1's (land) and '0's (water), return the number of islands.");
			p4_1.setTestCases(List.of(new TestCase("[[\"1\",\"1\",\"1\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"1\",\"0\"],[\"1\",\"1\",\"0\",\"0\",\"0\"],[\"0\",\"0\",\"0\",\"0\",\"0"]]", "1")));
			Problem p4_2 = new Problem("Validate Binary Search Tree", "Given the root of a binary tree, determine if it is a valid binary search tree (BST).");
			p4_2.setTestCases(List.of(new TestCase("[5,1,4,null,null,3,6]", "false")));
			Problem p4_3 = new Problem("Course Schedule", "There are a total of numCourses courses you have to take, labeled from 0 to numCourses - 1. You are given an array prerequisites where prerequisites[i] = [ai, bi] indicates that you must take course bi first if you want to take course ai.");
			p4_3.setTestCases(List.of(new TestCase("2\n[[1,0]]", "true")));
			Problem p4_4 = new Problem("Lowest Common Ancestor of a Binary Tree", "Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.");
			p4_4.setTestCases(List.of(new TestCase("[3,5,1,6,2,0,8,null,null,7,4]\n5\n1", "3")));
			Problem p4_5 = new Problem("Clone Graph", "Given a reference of a node in a connected undirected graph. Return a deep copy (clone) of the graph.");
			p4_5.setTestCases(List.of(new TestCase("[[2,4],[1,3],[2,4],[1,3]]", "[[2,4],[1,3],[2,4],[1,3]]")));
			problemRepository.save(p4_1); problemRepository.save(p4_2); problemRepository.save(p4_3); problemRepository.save(p4_4); problemRepository.save(p4_5);
			contest4.setProblems(List.of(p4_1, p4_2, p4_3, p4_4, p4_5));
			contestService.save(contest4);
			System.out.println("Seeded contestId: " + contest4.getId());

			// Contest 5: Advanced Algorithms
			Contest contest5 = new Contest("Advanced Algorithms");
			Problem p5_1 = new Problem("Trapping Rain Water", "Given n non-negative integers representing an elevation map where the width of each bar is 1, compute how much water it can trap after raining.");
			p5_1.setTestCases(List.of(new TestCase("[0,1,0,2,1,0,1,3,2,1,2,1]", "6")));
			Problem p5_2 = new Problem("Largest Rectangle in Histogram", "Given an array of integers heights representing the histogram's bar height where the width of each bar is 1, return the area of the largest rectangle in the histogram.");
			p5_2.setTestCases(List.of(new TestCase("[2,1,5,6,2,3]", "10")));
			Problem p5_3 = new Problem("Merge k Sorted Lists", "You are given an array of k linked-lists lists, each linked-list is sorted in ascending order. Merge all the linked-lists into one sorted linked-list and return it.");
			p5_3.setTestCases(List.of(new TestCase("[[1,4,5],[1,3,4],[2,6]]", "[1,1,2,3,4,4,5,6]")));
			Problem p5_4 = new Problem("Sliding Window Maximum", "You are given an array of integers nums, there is a sliding window of size k which is moving from the very left of the array to the very right. You can only see the k numbers in the window. Each time the sliding window moves right by one position. Return the max sliding window.");
			p5_4.setTestCases(List.of(new TestCase("[1,3,-1,-3,5,3,6,7]\n3", "[3,3,5,5,6,7]")));
			Problem p5_5 = new Problem("Word Ladder", "A transformation sequence from word beginWord to word endWord using a dictionary wordList is a sequence of words beginWord -> s1 -> s2 -> ... -> sk such that every adjacent pair of words differs by a single letter.");
			p5_5.setTestCases(List.of(new TestCase("hit\ncog\n[\"hot\",\"dot\",\"dog\",\"lot\",\"log\",\"cog\"]", "5")));
			problemRepository.save(p5_1); problemRepository.save(p5_2); problemRepository.save(p5_3); problemRepository.save(p5_4); problemRepository.save(p5_5);
			contest5.setProblems(List.of(p5_1, p5_2, p5_3, p5_4, p5_5));
			contestService.save(contest5);
			System.out.println("Seeded contestId: " + contest5.getId());
		};
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("http://localhost:5173").allowedMethods("GET","POST","PUT","DELETE","OPTIONS");
			}
		};
	}
}


