package com.example.hwjun.highhansung.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

@IgnoreExtraProperties
public class UserModel {
    private String UID;
    private String userEmail;
    private String userName;
    private String userGender;
    private String userMajor;
    private List<String> projectIdsOfInterest;
    private String introMsg;
    private List<ProjectOfInterest> projects;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public UserModel () {
        projectIdsOfInterest=new ArrayList<>();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserMajor() {
        return userMajor;
    }

    public void setUserMajor(String userMajor) {
        this.userMajor = userMajor;
    }

    public List<String> getProjectIdsOfInterest() {
        return projectIdsOfInterest;
    }

    public void addProjectIdsOfInterest(String projectId) {
        projectIdsOfInterest.add(projectId);
    }

    public String getIntroMsg() {
        return introMsg;
    }

    public void setIntroMsg(String introMsg) {
        this.introMsg = introMsg;
    }
}
