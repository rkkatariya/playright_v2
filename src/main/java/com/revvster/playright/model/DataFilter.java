/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

import java.sql.Timestamp;

/**
 *
 * @author REVV-03
 */
public class DataFilter {
    private Integer customer;
    private Integer salesRep;
    private Integer project;
    private String customerName;
    private String emailAddress;
    private String mobileNo;
    private String salesRepName;
    private Timestamp visitedOn;

    /**
     * @return the customer
     */
    public Integer getCustomer() {
        return customer;
    }

    /**
     * @param customer the customer to set
     */
    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    /**
     * @return the salesRep
     */
    public Integer getSalesRep() {
        return salesRep;
    }

    /**
     * @param salesRep the salesRep to set
     */
    public void setSalesRep(Integer salesRep) {
        this.salesRep = salesRep;
    }

    /**
     * @return the project
     */
    public Integer getProject() {
        return project;
    }

    /**
     * @param project the project to set
     */
    public void setProject(Integer project) {
        this.project = project;
    }

    /**
     * @return the emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @param emailAddress the emailAddress to set
     */
    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    /**
     * @return the mobileNo
     */
    public String getMobileNo() {
        return mobileNo;
    }

    /**
     * @param mobileNo the mobileNo to set
     */
    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    /**
     * @return the salesRepName
     */
    public String getSalesRepName() {
        return salesRepName;
    }

    /**
     * @param salesRepName the salesRepName to set
     */
    public void setSalesRepName(String salesRepName) {
        this.salesRepName = salesRepName;
    }

    /**
     * @return the visitedOn
     */
    public Timestamp getVisitedOn() {
        return visitedOn;
    }

    /**
     * @param visitedOn the visitedOn to set
     */
    public void setVisitedOn(Timestamp visitedOn) {
        this.visitedOn = visitedOn;
    }

    /**
     * @return the customerName
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName the customerName to set
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    
}
