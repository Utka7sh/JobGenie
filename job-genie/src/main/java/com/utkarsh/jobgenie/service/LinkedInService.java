package com.utkarsh.jobgenie.service;

import com.utkarsh.jobgenie.model.Job;
import com.utkarsh.jobgenie.model.JobSearchRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class LinkedInService {

    private static final int TIMEOUT = 15000; // 15 seconds
    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36";
    private static final int MAX_RETRIES = 3;

    public List<Job> scrapeJobs(JobSearchRequest request) {
        List<Job> jobs = new ArrayList<>();
        String searchUrl = buildSearchUrl(request);

        if (searchUrl == null || searchUrl.isEmpty()) {
            System.err.println("Error: Search URL is empty.");
            return jobs;
        }

        Document doc = fetchDocumentWithRetry(searchUrl, MAX_RETRIES);
        if (doc == null) {
            System.err.println("Failed to fetch job listings from LinkedIn.");
            return jobs;
        }

        Elements jobElements = doc.select("li");

        for (Element job : jobElements) {
            String title = job.select(".base-search-card__title").text();
            String company = job.select(".base-search-card__subtitle").text();
            String location = job.select(".job-search-card__location").text();
            String date = job.select("time").attr("datetime");
            String jobUrl = job.select(".base-card__full-link").attr("href");

            if (!title.isEmpty() && !company.isEmpty()) {
                jobs.add(new Job(title, company, location, date, jobUrl));
            }

            if (jobs.size() >= request.getLimit()) break;
        }

        return jobs;
    }

    private Document fetchDocumentWithRetry(String url, int maxRetries) {
        IOException lastException = null;

        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                Document doc = Jsoup.connect(url)
                        .timeout(TIMEOUT)
                        .userAgent(USER_AGENT)
                        .referrer("https://www.google.com/")
                        .header("Accept-Language", "en-US,en;q=0.9")
                        .get();

                return doc; // ✅ Return on success
            } catch (IOException e) {
                lastException = e;
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                try {
                    Thread.sleep(1000L * attempt); // Exponential backoff
                } catch (InterruptedException ignored) {
                }
            }
        }

        if (lastException != null) {
            lastException.printStackTrace(); // ✅ Safe check before using
        } else {
            System.err.println("All attempts failed, but no exception was captured.");
        }

        return null;
    }


    private String buildSearchUrl(JobSearchRequest req) {
        try {
            String keyword = encode(req.getKeyword());
            String location = encode(req.getLocation());
            String dateSincePosted = getDateSincePostedCode(req.getDateSincePosted());
            String experience = getExperienceCode(req.getExperienceLevel());
            String sortBy = getSortByCode(req.getSortBy());

            StringBuilder url = new StringBuilder("https://www.linkedin.com/jobs-guest/jobs/api/seeMoreJobPostings/search?");
            url.append("keywords=").append(keyword);
            url.append("&location=").append(location);
            if (!dateSincePosted.isEmpty()) url.append("&f_TPR=").append(dateSincePosted);
            if (!experience.isEmpty()) url.append("&f_E=").append(experience);
            if (!sortBy.isEmpty()) url.append("&sortBy=").append(sortBy);
            url.append("&start=0");

            return url.toString();
        } catch (Exception e) {
            System.err.println("Error building search URL: " + e.getMessage());
            return "";
        }
    }

    private String encode(String input) {
        return input == null ? "" : URLEncoder.encode(input.trim(), StandardCharsets.UTF_8);
    }

    private String getDateSincePostedCode(String filter) {
        if (filter == null) return "";
        return switch (filter.toLowerCase()) {
            case "24hr" -> "r86400";
            case "past week" -> "r604800";
            case "past month" -> "r2592000";
            default -> "";
        };
    }

    private String getSortByCode(String sortBy) {
        if (sortBy == null) return "";
        return switch (sortBy.toLowerCase()) {
            case "recent" -> "DD";
            case "relevant" -> "R";
            default -> "";
        };
    }

    private String getExperienceCode(String level) {
        if (level == null) return "2"; // Default to entry level
        return switch (level.toLowerCase()) {
            case "internship" -> "1";
            case "entry" -> "2";
            case "associate", "mid" -> "3";
            case "mid-senior", "senior" -> "4";
            case "director" -> "5";
            case "executive" -> "6";
            default -> "2";
        };
    }
}
