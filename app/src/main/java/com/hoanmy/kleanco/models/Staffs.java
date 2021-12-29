package com.hoanmy.kleanco.models;

import java.util.List;

public class Staffs {
    int page_curr;
    int page_next;
    private List<Employee> staffs;

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

    public List<Employee> getStaffs() {
        return staffs;
    }

    public void setStaffs(List<Employee> staffs) {
        this.staffs = staffs;
    }
}
