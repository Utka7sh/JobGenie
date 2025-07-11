package com.utkarsh.jobgenie.model;

public class Job {
    public String title;
    public String company;
    public String location;
    public String datePosted;
    public String url;

    public Job(String title, String company, String location, String datePosted, String url) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.datePosted = datePosted;
        this.url = url;
    }

    @Override
    public String toString() {
        return title + " at " + company + " (" + location + ") - Posted: " + datePosted + "\n" + url;
    }
}