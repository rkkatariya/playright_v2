/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rahulkk
 */
public class ChartData {
    
    private List<String> labels = new ArrayList<>();
    private List<DataSet> datasets = new ArrayList<>();

    public ChartData() {
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }
    
    public void addLabel(String label) {
        this.labels.add(label);
    }

    public List<DataSet> getDatasets() {
        return datasets;
    }

    public void setDatasets(List<DataSet> datasets) {
        this.datasets = datasets;
    }
    
    public void addDataset(DataSet ds) {
        this.datasets.add(ds);
    }
    
}
