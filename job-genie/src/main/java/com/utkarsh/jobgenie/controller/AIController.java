package com.utkarsh.jobgenie.controller;

import com.utkarsh.jobgenie.model.AIRequest;
import com.utkarsh.jobgenie.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
@CrossOrigin(origins = "*")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/generate-referral")
    public String generateReferral(@RequestBody AIRequest req) {
        return aiService.generateReferralMessage(req);
    }

    @PostMapping("/generate-cover-letter")
    public String generateCoverLetter(@RequestBody AIRequest req) {
        return aiService.generateCoverLetter(req);
    }
}
