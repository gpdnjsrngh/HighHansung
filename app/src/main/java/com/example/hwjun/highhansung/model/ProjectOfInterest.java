package com.example.hwjun.highhansung.model;

public class ProjectOfInterest {
    String projectId;
    String gender;
    String major;
    boolean wantNear;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public boolean isWantNear() {
        return wantNear;
    }

    public void setWantNear(boolean wantNear) {
        this.wantNear = wantNear;
    }
}
