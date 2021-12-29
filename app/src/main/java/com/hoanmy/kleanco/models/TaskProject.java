package com.hoanmy.kleanco.models;

public class TaskProject {
    String _id;
    String name;
    String project_id;
    String user_id;
    String date_str;
    int date;
    int time_period;
    int time_rest;
    String time_start;
    String time_end;
    long time_start_timestamp;
    long time_end_timestamp;
    String area;
    String floor;
    String note;
    int status;
    long created_at;
    long updated_at;
    long time_end_real_ts;
    long time_period_diff;
    long time_period_diff_ts;
    long time_period_real;
    long time_period_real_ts;
    long time_start_real_ts;
    private User user;
    private boolean isDone = false;
    int feedback_status;
    int feedback_score;

    public int getFeedback_score() {
        return feedback_score;
    }

    public void setFeedback_score(int feedback_score) {
        this.feedback_score = feedback_score;
    }

    public int getFeedback_status() {
        return feedback_status;
    }

    public void setFeedback_status(int feedback_status) {
        this.feedback_status = feedback_status;
    }

    public long getTime_end_real_ts() {
        return time_end_real_ts;
    }

    public void setTime_end_real_ts(long time_end_real_ts) {
        this.time_end_real_ts = time_end_real_ts;
    }

    public long getTime_period_diff() {
        return time_period_diff;
    }

    public void setTime_period_diff(long time_period_diff) {
        this.time_period_diff = time_period_diff;
    }

    public long getTime_period_diff_ts() {
        return time_period_diff_ts;
    }

    public void setTime_period_diff_ts(long time_period_diff_ts) {
        this.time_period_diff_ts = time_period_diff_ts;
    }

    public long getTime_period_real() {
        return time_period_real;
    }

    public void setTime_period_real(long time_period_real) {
        this.time_period_real = time_period_real;
    }

    public long getTime_period_real_ts() {
        return time_period_real_ts;
    }

    public void setTime_period_real_ts(long time_period_real_ts) {
        this.time_period_real_ts = time_period_real_ts;
    }

    public long getTime_start_real_ts() {
        return time_start_real_ts;
    }

    public void setTime_start_real_ts(long time_start_real_ts) {
        this.time_start_real_ts = time_start_real_ts;
    }

    public boolean isShowFeedback() {
        return isShowFeedback;
    }

    public void setShowFeedback(boolean showFeedback) {
        isShowFeedback = showFeedback;
    }

    private boolean isShowFeedback = false;
    private long timeStartFeedback = 0;

    public boolean isDone() {
        return isDone;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    public long getTimeStartFeedback() {
        return timeStartFeedback;
    }

    public void setTimeStartFeedback(long timeStartFeedback) {
        this.timeStartFeedback = timeStartFeedback;
    }

    public long getTime_start_timestamp() {
        return time_start_timestamp;
    }

    public void setTime_start_timestamp(long time_start_timestamp) {
        this.time_start_timestamp = time_start_timestamp;
    }

    public long getTime_end_timestamp() {
        return time_end_timestamp;
    }

    public void setTime_end_timestamp(long time_end_timestamp) {
        this.time_end_timestamp = time_end_timestamp;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getDate_str() {
        return date_str;
    }

    public void setDate_str(String date_str) {
        this.date_str = date_str;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getTime_period() {
        return time_period;
    }

    public void setTime_period(int time_period) {
        this.time_period = time_period;
    }

    public int getTime_rest() {
        return time_rest;
    }

    public void setTime_rest(int time_rest) {
        this.time_rest = time_rest;
    }

    public String getTime_start() {
        return time_start;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public String getTime_end() {
        return time_end;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreated_at() {
        return created_at;
    }

    public void setCreated_at(long created_at) {
        this.created_at = created_at;
    }

    public long getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(long updated_at) {
        this.updated_at = updated_at;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
