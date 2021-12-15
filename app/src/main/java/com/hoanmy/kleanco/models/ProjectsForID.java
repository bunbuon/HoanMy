package com.hoanmy.kleanco.models;

import java.util.List;

public class ProjectsForID {
    int page_curr;
    int page_next;
    List<TaskProject> tasks;

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

    public List<TaskProject> getTaskProjects() {
        return tasks;
    }

    public void setTaskProjects(List<TaskProject> taskProjects) {
        this.tasks = taskProjects;
    }
}
