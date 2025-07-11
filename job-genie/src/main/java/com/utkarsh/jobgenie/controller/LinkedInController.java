package com.utkarsh.jobgenie.controller;

import com.utkarsh.jobgenie.model.Job;
import com.utkarsh.jobgenie.model.JobSearchRequest;
import com.utkarsh.jobgenie.service.LinkedInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/linkedin")
public class LinkedInController {

    @Autowired
    private LinkedInService service;

    @PostMapping("/jobs")
    public List<Job> getLinkedInJobs(@RequestBody JobSearchRequest request) {
        return service.scrapeJobs(request);
    }
}
