/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

/**
 *
 * @author Rahul
 */
public class Location extends Auditable implements ContextObj {
    
    private Integer id;
    private Integer project;
    private String projectName;
    private String name;
    private String description;
    private String url;
    private Integer businessOwner;

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getBusinessOwner() {
        return businessOwner;
    }

    public void setBusinessOwner(Integer businessOwner) {
        this.businessOwner = businessOwner;
    }

    
}
