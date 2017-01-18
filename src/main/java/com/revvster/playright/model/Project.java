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
public class Project extends Auditable implements ContextObj {
    
    private Integer id;
    private Integer company;
    private String companyName;
    private String name;
    private String description;
    private String url;
    private Integer businessOwner;
    private String coordinates;
    //These need to be removed from the model. Bad design.
    private Integer totalUnits;
    private Integer unitsAvailable;
    private Integer unitsSold;
    private Integer totalVisits;
    private String salesRep;
    
    @Override
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
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

    public String getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String coordinates) {
        this.coordinates = coordinates;
    }

    public Integer getTotalUnits() {
        return totalUnits;
    }

    public void setTotalUnits(Integer totalUnits) {
        this.totalUnits = totalUnits;
    }

    public Integer getUnitsAvailable() {
        return unitsAvailable;
    }

    public void setUnitsAvailable(Integer unitsAvailable) {
        this.unitsAvailable = unitsAvailable;
    }

    public Integer getUnitsSold() {
        return unitsSold;
    }

    public void setUnitsSold(Integer unitsSold) {
        this.unitsSold = unitsSold;
    }

    public Integer getTotalVisits() {
        return totalVisits;
    }

    public void setTotalVisits(Integer totalVisits) {
        this.totalVisits = totalVisits;
    }

    /**
     * @return the salesRep
     */
    public String getSalesRep() {
        return salesRep;
    }

    /**
     * @param salesRep the salesRep to set
     */
    public void setSalesRep(String salesRep) {
        this.salesRep = salesRep;
    }

    
}
