/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.model;

import java.sql.Blob;
import java.sql.Timestamp;


/**
 *
 * @author REVV-03
 */
public class Data {

    private Integer id;
    private Timestamp newsDate;
    private String newsPaper;
    private String language;
    private String headline;
    private String edition;
    private String supplement;
    private String source;
    private Integer pageNo;
    private Integer height;
    private Integer width;
    private Integer totalArticleSize;
    private Integer circulationFigure;
    private Integer quantitativeAVE;
    private Integer journalistFactor;
    private Blob image;
    private String imageExists;      
    private String fileType;
    private Integer fileSize;
    private String imageFileName;
    private Timestamp lastUpdatedOn;
    private Integer active;
    private Integer createdBy;
    private Integer lastUpdatedBy;
    

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    public String getImageFileName() {
        return imageFileName;
    }

    public void setImageFileName(String imageFileName) {
        this.imageFileName = imageFileName;
    }

    /**
     * @return the newsDate
     */
    public Timestamp getNewsDate() {
        return newsDate;
    }

    /**
     * @param newsDate the newsDate to set
     */
    public void setNewsDate(Timestamp newsDate) {
        this.newsDate = newsDate;
    }

    /**
     * @return the newsPaper
     */
    public String getNewsPaper() {
        return newsPaper;
    }

    /**
     * @param newsPaper the newsPaper to set
     */
    public void setNewsPaper(String newsPaper) {
        this.newsPaper = newsPaper;
    }

    /**
     * @return the language
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the headline
     */
    public String getHeadline() {
        return headline;
    }

    /**
     * @param headline the headline to set
     */
    public void setHeadline(String headline) {
        this.headline = headline;
    }

    /**
     * @return the edition
     */
    public String getEdition() {
        return edition;
    }

    /**
     * @param edition the edition to set
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }

    /**
     * @return the supplement
     */
    public String getSupplement() {
        return supplement;
    }

    /**
     * @param supplement the supplement to set
     */
    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    /**
     * @return the source
     */
    public String getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * @return the pageNo
     */
    public Integer getPageNo() {
        return pageNo;
    }

    /**
     * @param pageNo the pageNo to set
     */
    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    /**
     * @return the height
     */
    public Integer getHeight() {
        return height;
    }

    /**
     * @param height the height to set
     */
    public void setHeight(Integer height) {
        this.height = height;
    }

    /**
     * @return the width
     */
    public Integer getWidth() {
        return width;
    }

    /**
     * @param width the width to set
     */
    public void setWidth(Integer width) {
        this.width = width;
    }

    /**
     * @return the totalArticleSize
     */
    public Integer getTotalArticleSize() {
        return totalArticleSize;
    }

    /**
     * @param totalArticleSize the totalArticleSize to set
     */
    public void setTotalArticleSize(Integer totalArticleSize) {
        this.totalArticleSize = totalArticleSize;
    }

    /**
     * @return the circulationFigure
     */
    public Integer getCirculationFigure() {
        return circulationFigure;
    }

    /**
     * @param circulationFigure the circulationFigure to set
     */
    public void setCirculationFigure(Integer circulationFigure) {
        this.circulationFigure = circulationFigure;
    }

    /**
     * @return the quantitativeAVE
     */
    public Integer getQuantitativeAVE() {
        return quantitativeAVE;
    }

    /**
     * @param quantitativeAVE the quantitativeAVE to set
     */
    public void setQuantitativeAVE(Integer quantitativeAVE) {
        this.quantitativeAVE = quantitativeAVE;
    }

    /**
     * @return the journalistFactor
     */
    public Integer getJournalistFactor() {
        return journalistFactor;
    }

    /**
     * @param journalistFactor the journalistFactor to set
     */
    public void setJournalistFactor(Integer journalistFactor) {
        this.journalistFactor = journalistFactor;
    }

   
    /**
     * @return the imageExists
     */
    public String getImageExists() {
        return imageExists;
    }

    /**
     * @param imageExists the imageExists to set
     */
    public void setImageExists(String imageExists) {
        this.imageExists = imageExists;
    }
  

    /**
     * @return the image
     */
    public Blob getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(Blob image) {
        this.image = image;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileSize
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize the fileSize to set
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the lastUpdatedOn
     */
    public Timestamp getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    /**
     * @param lastUpdatedOn the lastUpdatedOn to set
     */
    public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

    /**
     * @return the active
     */
    public Integer getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Integer active) {
        this.active = active;
    }

    /**
     * @return the createdBy
     */
    public Integer getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the lastUpdatedBy
     */
    public Integer getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    /**
     * @param lastUpdatedBy the lastUpdatedBy to set
     */
    public void setLastUpdatedBy(Integer lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

  

}
