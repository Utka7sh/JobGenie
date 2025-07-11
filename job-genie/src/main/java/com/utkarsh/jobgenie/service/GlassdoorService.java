package com.utkarsh.jobgenie.service;

import com.utkarsh.jobgenie.model.Job;
import com.utkarsh.jobgenie.model.JobSearchRequest;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GlassdoorService {

    private static final Logger log = LoggerFactory.getLogger(GlassdoorService.class);

    @Value("${scraperapi.key}")
    private String apiKey;

    public List<Job> scrapeJobs(JobSearchRequest req) {
        List<Job> jobs = new ArrayList<>();

        try {
            String keyword = req.getKeyword() != null ? req.getKeyword().replace(" ", "-") : "java";
            String location = req.getLocation() != null ? req.getLocation().replace(" ", "-") : "india";
            int limit = req.getLimit() > 0 ? req.getLimit() : 10;

            String targetUrl = "https://www.glassdoor.co.in/Job/" + location.toLowerCase() + "-" + keyword.toLowerCase() + "-jobs-SRCH_IL.0,5_IN115_KO6,10.htm";
            String encodedUrl = URLEncoder.encode(targetUrl, StandardCharsets.UTF_8);
            String scraperUrl = "http://api.scraperapi.com?api_key=" + apiKey + "&url=" + encodedUrl;

            Document doc = Jsoup.connect(scraperUrl)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/138.0.0.0 Safari/537.36")
                    .timeout(10000)
                    .get();

            Elements jobCards = doc.select(".react-job-listing");

            for (Element job : jobCards) {
                String title = job.select(".jobInfoItem.jobTitle").text();
                String company = job.select(".jobEmpolyerName").text();
                String loc = job.select(".empLoc").text();
                String link = "https://www.glassdoor.co.in" + job.select("a").attr("href");
                String age = job.select(".d-flex.align-items-end.pl-std.css-17n8uzw").text(); // optional

                jobs.add(new Job(title, company, loc, age, link));

                if (jobs.size() >= limit) break;
            }

        } catch (Exception e) {
            log.error("Glassdoor ScraperAPI error", e);
        }

        return jobs;
    }
}
