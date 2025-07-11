package com.utkarsh.jobgenie.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.utkarsh.jobgenie.model.AIRequest;
import okhttp3.*;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    private static final String GEMINI_API_KEY = "";
    private static final String GEMINI_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String generateReferralMessage(AIRequest req) {
        String prompt = buildReferralPrompt(req);
        return callGemini(prompt);
    }

    public String generateCoverLetter(AIRequest req) {
        String prompt = buildCoverLetterPrompt(req);
        return callGemini(prompt);
    }

    private String buildReferralPrompt(AIRequest req) {
        if (req.getTemplate() != null && !req.getTemplate().isEmpty()) {
            return "Using this template:\n" + req.getTemplate() +
                    "\n\nCreate a final referral message only, avoid explanation or tips. Don't include any instructions or example formatting. Just return the final referral message.\n"+
                    "Company Name: " + req.getCompanyName() + "\n" +
                    "Job Title: " + req.getJobTitle() + "\n" +
                    "Job Description: " + req.getJobDescription();
        } else {
            return "Generate a human-written and confident referral message,  avoid explanation or tips. Don't include any instructions or example formatting. Just return the final referral message using the following details:\n" +
                    "Company Name: " + req.getCompanyName() + "\n" +
                    "Job Title: " + req.getJobTitle() + "\n" +
                    "Job Description: " + req.getJobDescription();
        }
    }

    private String buildCoverLetterPrompt(AIRequest req) {
        if (req.getTemplate() != null && !req.getTemplate().isEmpty()) {
            return "Using this template:\n" + req.getTemplate() +
                    "\n\nCreate a professional and human-written cover letter using the following details, avoid explanation or tips. Don't include any instructions or example formatting. Just return the final cover letter.\n" +
                    "Job Title: " + req.getJobTitle() + "\n" +
                    "Company Name: " + req.getCompanyName() + "\n" +
                    "Job Description: " + req.getJobDescription();
        } else {
            return "Generate a confident and human-written cover letter for the following job, , avoid explanation or tips. Don't include any instructions or example formatting. Just return the final cover letter.\n" +
                    "Job Title: " + req.getJobTitle() + "\n" +
                    "Company Name: " + req.getCompanyName() + "\n" +
                    "Job Description: " + req.getJobDescription();
        }
    }

    private String callGemini(String prompt) {
        try {
            MediaType mediaType = MediaType.parse("application/json");

            String json = mapper.writeValueAsString(new Object() {
                public final Object[] contents = new Object[] {
                        new Object() {
                            public final Object[] parts = new Object[] {
                                    new Object() {
                                        public final String text = prompt;
                                    }
                            };
                            public final String role = "user";
                        }
                };
            });

            Request request = new Request.Builder()
                    .url(GEMINI_URL)
                    .post(RequestBody.create(json, mediaType))
                    .addHeader("Content-Type", "application/json")
                    .build();

            Response response = client.newCall(request).execute();
            String responseBody = response.body().string();

            JsonNode root = mapper.readTree(responseBody);
            JsonNode textNode = root
                    .path("candidates")
                    .path(0)
                    .path("content")
                    .path("parts")
                    .path(0)
                    .path("text");

            return (textNode != null && !textNode.isMissingNode()) ? textNode.asText() : "No content returned.";

        } catch (Exception e) {
            e.printStackTrace();
            return "AI generation failed.";
        }
    }


}
