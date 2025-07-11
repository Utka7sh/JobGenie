package com.utkarsh.jobgenie.model;

public class JobSearchRequest {
    private String keyword;
    private String location;
    private String dateSincePosted;
    private String salary;
    private String experienceLevel;
    private int limit = 10;
    private String sortBy;
    private String minRating;
    private String fromAge;
    private String seniorityType;
    private String jobTypeGlassdoor;

    // --- Getters ---
    public String getKeyword() { return keyword; }
    public String getLocation() { return location; }
    public String getDateSincePosted() { return dateSincePosted; }
    public String getSalary() { return salary; }
    public String getExperienceLevel() { return experienceLevel; }
    public int getLimit() { return limit; }
    public String getSortBy() { return sortBy; }
    public String getMinRating() { return minRating; }
    public String getFromAge() { return fromAge; }
    public String getSeniorityType() { return seniorityType; }
    public String getJobTypeGlassdoor() { return jobTypeGlassdoor; }

    // --- Setters ---
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setLocation(String location) { this.location = location; }
    public void setDateSincePosted(String dateSincePosted) { this.dateSincePosted = dateSincePosted; }
    public void setSalary(String salary) { this.salary = salary; }
    public void setExperienceLevel(String experienceLevel) { this.experienceLevel = experienceLevel; }
    public void setLimit(int limit) { this.limit = limit; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public void setMinRating(String minRating) { this.minRating = minRating; }
    public void setFromAge(String fromAge) { this.fromAge = fromAge; }
    public void setSeniorityType(String seniorityType) { this.seniorityType = seniorityType; }
    public void setJobTypeGlassdoor(String jobTypeGlassdoor) { this.jobTypeGlassdoor = jobTypeGlassdoor; }
}
