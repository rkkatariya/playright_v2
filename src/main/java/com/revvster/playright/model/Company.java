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
public class Company extends Auditable implements ContextObj {
    
    private Integer id;
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
