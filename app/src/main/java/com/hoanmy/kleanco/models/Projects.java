package com.hoanmy.kleanco.models;

import java.util.List;

public class Projects {
    int page_curr;
    int page_next;
    private List<ProjectDetail> projects;

    public int getPage_curr() {
        return page_curr;
    }

    public void setPage_curr(int page_curr) {
        this.page_curr = page_curr;
    }

    public int getPage_next() {
        return page_next;
    }

    public void setPage_next(int page_next) {
        this.page_next = page_next;
    }

    public List<ProjectDetail> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectDetail> projects) {
        this.projects = projects;
    }
}
