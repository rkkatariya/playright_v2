/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

/**
 *
 * @author rahulkk
 */
public class BarDataSet extends DataSetBase implements DataSet {

    private String backgroundColor;
    private String borderColor;
    private String borderWidth;
    private String hoverBackgroundColor;
    private String hoverBorderColor;
//    private String fillColor;
//    private String strokeColor;
//    private String highlightFill;
//    private String highlightStroke;

    public BarDataSet() {
        super();
    }

    public BarDataSet(String backgroundColor, String borderColor,
            String borderWidth, String hoverBackgroundColor, String hoverBorderColor) {
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.borderWidth = borderWidth;
        this.hoverBackgroundColor = hoverBackgroundColor;
        this.hoverBorderColor = hoverBorderColor;
    }
//
    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(String borderWidth) {
        this.borderWidth = borderWidth;
    }

    public String getHoverBackgroundColor() {
        return hoverBackgroundColor;
    }

    public void setHoverBackgroundColor(String hoverBackgroundColor) {
        this.hoverBackgroundColor = hoverBackgroundColor;
    }

    public String getHoverBorderColor() {
        return hoverBorderColor;
    }

    public void setHoverBorderColor(String hoverBorderColor) {
        this.hoverBorderColor = hoverBorderColor;
    }
//    public BarDataSet(String fillColor, String strokeColor,
//            String highlightFill, String highlightStroke) {
//        super();
//        this.fillColor = fillColor;
//        this.strokeColor = strokeColor;
//        this.highlightFill = highlightFill;
//        this.highlightStroke = highlightStroke;
//    }

//    public String getFillColor() {
//        return fillColor;
//    }
//
//    public void setFillColor(String fillColor) {
//        this.fillColor = fillColor;
//    }
//
//    public String getStrokeColor() {
//        return strokeColor;
//    }
//
//    public void setStrokeColor(String strokeColor) {
//        this.strokeColor = strokeColor;
//    }
//
//    public String getHighlightFill() {
//        return highlightFill;
//    }
//
//    public void setHighlightFill(String highlightFill) {
//        this.highlightFill = highlightFill;
//    }
//
//    public String getHighlightStroke() {
//        return highlightStroke;
//    }
//
//    public void setHighlightStroke(String highlightStroke) {
//        this.highlightStroke = highlightStroke;
//    }

}
