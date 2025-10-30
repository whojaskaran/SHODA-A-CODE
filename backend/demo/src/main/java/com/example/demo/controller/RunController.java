package com.example.demo.controller;

import com.example.demo.model.RunResult;
import com.example.demo.model.Submission;
import com.example.demo.service.JudgeService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RunController {

    private final JudgeService judgeService;

    public RunController(JudgeService judgeService) {
        this.judgeService = judgeService;
    }

    @PostMapping("/api/run")
    public RunResult runCode(@RequestBody Submission submission) throws Exception {
        return judgeService.runCode(submission);
    }
}
