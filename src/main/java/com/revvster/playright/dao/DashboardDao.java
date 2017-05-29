/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Data;
import com.revvster.playright.model.User;
import com.revvster.playright.ui.charts.LineChartData;
import com.revvster.playright.ui.charts.BarChartData;
import com.revvster.playright.ui.charts.ChartsUtil;
import com.revvster.playright.ui.charts.PieChartData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 *
 * @author REVV-03
 */
public interface DashboardDao {

    public void close();

    public Integer getAvailableUnitsInProject(Integer project) throws SQLException;

    public Integer getVisitsInProject(Integer project) throws SQLException;

    public Integer getSoldUnitsInProject(Integer project) throws SQLException;

    public Integer getBlocksInProject(Integer project) throws SQLException;

    public String getCityInProject(Integer project) throws SQLException;

    public LineChartData getConversionRateBySalesRep(Integer project) throws SQLException;

    public List<PieChartData> getSalesQuotientBySalesRep(Integer project) throws SQLException;

    public Integer getSoldUnitsForProject(Integer project) throws SQLException;

    public String getChampionForProject(Integer project) throws SQLException;

    public LineChartData getSalesDataForProject(Integer project, ChartsUtil.DateRanges dateRange) throws SQLException;

    public LineChartData getVisitsDataForProject(Integer project, ChartsUtil.DateRanges dateRange) throws SQLException;

    public BarChartData getTopXSalesRepPerfForProject(Integer project, ChartsUtil.DateRanges dateRange, Integer x) throws SQLException;

    public LineChartData getVisitsDataForUser(Integer user, ChartsUtil.DateRanges dateRange) throws SQLException;

    public LineChartData getSalesDataForUser(Integer user, ChartsUtil.DateRanges dateRange) throws SQLException;

    public BarChartData getAnswerDistributionByCustomer(Integer selectedQuestion) throws SQLException;

    public BarChartData getInventoryStatesByAttribute(Integer selectedCatalogAttr, String attributes, String label) throws SQLException;

    public BarChartData getAwgTimeSpent(Integer project, ChartsUtil.DateRanges dateRange) throws SQLException;

    public String getHighestSellingForProject(Integer project) throws SQLException;

    public Integer createData(Data data) throws SQLException;

    public void updateData(Data data) throws SQLException;

    public List<Data> getDataByContext(User loggedInUser) throws SQLException;
    
    public List<Data> getDataByCustomer(User loggedInUser, String customer) throws SQLException;
    
    public List<String> getDistinctLanguage()
            throws SQLException;   
    public List<Data> getDistinctCustomers()
            throws SQLException; 
   
    public void deleteData(Integer id) throws SQLException;
    
    public BarChartData getLanguageVsArticles(Timestamp inptFrom,Timestamp inptTo) throws SQLException;
    
    public PieChartData getEditionVsArticles(Timestamp inptFrom,Timestamp inptTo) throws SQLException;
    
    public PieChartData getJournalistDistribution(Timestamp inptFrom,Timestamp inptTo) throws SQLException;
    
    public BarChartData getTopEnglishPrintDistribution(Timestamp inptFrom,Timestamp inptTo) throws SQLException;
    
    public BarChartData getTopVernacularPrintDistribution(Timestamp inptFrom,Timestamp inptTo) throws SQLException;
    
    public BarChartData getLanguageVsArticles(ChartsUtil.DateRanges dateRange) throws SQLException;
    
    public PieChartData getEditionVsArticles(ChartsUtil.DateRanges dateRange) throws SQLException;
    
    public BarChartData getTopEnglishPrintDistribution(ChartsUtil.DateRanges dateRange) throws SQLException;
    
    public BarChartData getTopVernacularPrintDistribution(ChartsUtil.DateRanges dateRange) throws SQLException;
    
    public PieChartData getJournalistDistribution(ChartsUtil.DateRanges dateRange) throws SQLException;
    
    public Data getPhoto(Integer imageId) throws SQLException;
    
    public Data getData(Integer id) throws SQLException;
    
    public List<Data> getDataForEmail(String inputFrom,
            String inputTo, String inputLanguage, String inputEdition,
            String inputSource, String inputNewsPaper, String inputCustomer, String inputImageExist)
            throws SQLException;

}
