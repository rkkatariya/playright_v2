/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author rahulkk
 */
public interface DataSet {
    
    public String getLabel();
    public List<BigDecimal> getData();
}
