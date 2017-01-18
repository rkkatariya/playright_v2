/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rahulkk
 */
public class DataSetBase {
    
    private String label;
    private List<BigDecimal> data = new ArrayList<>();

    public DataSetBase() {
    }

    public DataSetBase(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<BigDecimal> getData() {
        return data;
    }

    public void setData(List<BigDecimal> data) {
        this.data = data;
    }
    
    public void addData(BigDecimal data) {
        this.data.add(data);
    }
}
