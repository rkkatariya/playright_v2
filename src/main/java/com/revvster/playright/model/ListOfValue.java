/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

import java.util.List;

/**
 *
 * @author Rahul
 */
public class ListOfValue extends Auditable {
    
    private Integer id;
    private Integer company;
    private Integer project;
    private Integer location;
    private LOVType type;
    private String name;
    private String value;
    private List<LOVAttribute> attributes;

    public ListOfValue() {
    }

    public ListOfValue(Integer id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCompany() {
        return company;
    }

    public void setCompany(Integer company) {
        this.company = company;
    }

    public Integer getProject() {
        return project;
    }

    public void setProject(Integer project) {
        this.project = project;
    }

    public Integer getLocation() {
        return location;
    }

    public void setLocation(Integer location) {
        this.location = location;
    }

    public LOVType getType() {
        return type;
    }

    public void setType(LOVType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<LOVAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<LOVAttribute> attributes) {
        this.attributes = attributes;
    }
    
    public void addAttibute(LOVAttribute attribute) {
        this.attributes.add(attribute);
    }
    
}
