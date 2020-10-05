package com.example.hwjun.highhansung.model;

public class Project {
    private String name;
    private String host;
    private String registerDate;
    private String link;
    private String img;
    private String project_id;

    public Project() {

    }

    public Project(String name, String host, String registerDate) {
        this.name = name;
        this.host = host;
        this.registerDate = registerDate;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public void setProject_id(String project_id) {
        this.project_id = project_id;
    }

    public String getName() {
        return name;
    }

    public String getHost() {
        return host;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public String getLink() {
        return link;
    }

    public String getImg() {
        return img;
    }

    public String getProject_id() {
        return project_id;
    }
}
