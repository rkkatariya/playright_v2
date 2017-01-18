/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

/**
 * @author rahulkk
 */
public class LineDataSet extends DataSetBase implements DataSet {

    private String fill;     
    private String backgroundColor;
    private String borderColor;
    private String pointBorderColor;
    private String pointBackgroundColor;
//    private String pointBorderWidth;
//    private String pointHoverRadius;
    private String pointHoverBackgroundColor;
    private String pointHoverBorderColor;
//    private String pointHoverBorderWidth;  
//    private String fillColor;
//    private String strokeColor;
//    private String pointColor;
//    private String pointStrokeColor;
//    private String pointHighlightFill;
//    private String pointHighlightStroke;

    public LineDataSet() {
        super();
    }

    
    

//    public LineDataSet(String fillColor, String strokeColor, String pointColor,
//            String pointStrokeColor, String pointHighlightFill,
//            String pointHighlightStroke) {
//        super();
//        this.fillColor = fillColor;
//        this.strokeColor = strokeColor;
//        this.pointColor = pointColor;
//        this.pointStrokeColor = pointStrokeColor;
//        this.pointHighlightFill = pointHighlightFill;
//        this.pointHighlightStroke = pointHighlightStroke;
//    }
//
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
//    public String getPointColor() {
//        return pointColor;
//    }
//
//    public void setPointColor(String pointColor) {
//        this.pointColor = pointColor;
//    }
//
//    public String getPointStrokeColor() {
//        return pointStrokeColor;
//    }
//
//    public void setPointStrokeColor(String pointStrokeColor) {
//        this.pointStrokeColor = pointStrokeColor;
//    }
//
//    public String getPointHighlightFill() {
//        return pointHighlightFill;
//    }
//
//    public void setPointHighlightFill(String pointHighlightFill) {
//        this.pointHighlightFill = pointHighlightFill;
//    }
//
//    public String getPointHighlightStroke() {
//        return pointHighlightStroke;
//    }
//
//    public void setPointHighlightStroke(String pointHighlightStroke) {
//        this.pointHighlightStroke = pointHighlightStroke;
//    }

    public LineDataSet(String fill, String backgroundColor, String borderColor, 
            String pointBorderColor, String pointBackgroundColor, 
            String pointHoverBackgroundColor, String pointHoverBorderColor) {
        this.fill = fill;
        this.backgroundColor = backgroundColor;
        this.borderColor = borderColor;
        this.pointBorderColor = pointBorderColor;
        this.pointBackgroundColor = pointBackgroundColor;
        this.pointHoverBackgroundColor = pointHoverBackgroundColor;
        this.pointHoverBorderColor = pointHoverBorderColor;
    }

    public String getFill() {
        return fill;
    }

    public void setFill(String fill) {
        this.fill = fill;
    }

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

    public String getPointBorderColor() {
        return pointBorderColor;
    }

    public void setPointBorderColor(String pointBorderColor) {
        this.pointBorderColor = pointBorderColor;
    }

    public String getPointBackgroundColor() {
        return pointBackgroundColor;
    }

    public void setPointBackgroundColor(String pointBackgroundColor) {
        this.pointBackgroundColor = pointBackgroundColor;
    }

    public String getPointHoverBackgroundColor() {
        return pointHoverBackgroundColor;
    }

    public void setPointHoverBackgroundColor(String pointHoverBackgroundColor) {
        this.pointHoverBackgroundColor = pointHoverBackgroundColor;
    }

    public String getPointHoverBorderColor() {
        return pointHoverBorderColor;
    }

    public void setPointHoverBorderColor(String pointHoverBorderColor) {
        this.pointHoverBorderColor = pointHoverBorderColor;
    }

    
}
