/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

/**
 *
 * @author REVV-03
 */
public class PieDataSet extends DataSetBase implements DataSet {

    private String backgroundColor;
    private String borderColor;
    private String borderWidth;
    private String hoverBackgroundColor;
    private String hoverBorderColor;
     
    
     public PieDataSet() {
        super();
    }

    public PieDataSet(String backgroundColor, String borderColor,
            String borderWidth, String hoverBackgroundColor, String hoverBorderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.hoverBorderColor = hoverBorderColor;
    }
    /**
     * @return the backgroundColor
     */
    public String getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * @param backgroundColor the backgroundColor to set
     */
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    /**
     * @return the borderColor
     */
    public String getBorderColor() {
        return borderColor;
    }

    /**
     * @param borderColor the borderColor to set
     */
    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * @return the borderWidth
     */
    public String getBorderWidth() {
        return borderWidth;
    }

    /**
     * @param borderWidth the borderWidth to set
     */
    public void setBorderWidth(String borderWidth) {
        this.borderWidth = borderWidth;
    }

    /**
     * @return the hoverBackgroundColor
     */
    public String getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    /**
     * @param hoverBackgroundColor the hoverBackgroundColor to set
     */
    public void setHoverBackgroundColor(String hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    /**
     * @return the hoverBorderColor
     */
    public String getHoverBorderColor() {
        return hoverBorderColor;
    }

    /**
     * @param hoverBorderColor the hoverBorderColor to set
     */
    public void setHoverBorderColor(String hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
    }
}
