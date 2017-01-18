/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.ui.charts;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author rahulkk
 */
public class ChartsUtil {

    private static final String[] monthNames = {"January", "February", "March", "April", "May", "June", "July",
        "August", "September", "October", "November", "December"};

    private static final String[] monthShortNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct", "Nov", "Dec"};

    private static final String[] dayNames = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private static final String[] dayShortNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    public enum DateRanges {
        last7Days, lastMonth, lastQuarter, last6Months, lastYear, dateRange
    };

    public enum DataPointGroupBy {
        DAY, MONTH
    };

    //Ensure datapoints are same when merging 2 areaCharts    
    public static void mergeLineChartData(LineChartData d1, LineChartData d2) {
        LineDataSet d = new LineDataSet(
                "true",
                "rgba(255,99,132,0.4)",
                "rgba(255,99,132,1)",
                "rgba(255,99,132,1)",
                "#fff",
                "rgba(255,99,132,1)",
                "rgba(220,220,220,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,1)",
        //                "rgba(220,220,220,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(220,220,220,1)"
        );
        d.setLabel(d2.getDatasets().get(0).getLabel());
        d.setData(d2.getDatasets().get(0).getData());
        d1.addDataset(d);
    }

    public static String getSelectFieldsForDateAxis(DateRanges dateRange, String dateFieldName) {
        String selectFields = "";
        switch (dateRange) {
            case last7Days:
                selectFields = "DAY(any_value(q." + dateFieldName + ")) as `day_month`, \n"
                        + "DAYOFWEEK(any_value(q." + dateFieldName + ")) as `day_week`, \n"
                        + "DAYNAME(any_value(q." + dateFieldName + ")) as `day_name`, \n"
                        + "YEAR(any_value(q." + dateFieldName + ")) as `year`, \n "
                        + "MONTH(any_value(q." + dateFieldName + ")) as `month`";
                break;
            case lastMonth:
                selectFields = "DAY(any_value(q." + dateFieldName + ")) as `day_month`, \n"
                        + "DAYOFWEEK(any_value(q." + dateFieldName + ")) as `day_week`, \n"
                        + "DAYNAME(any_value(q." + dateFieldName + ")) as `day_name`, \n"
                        + "YEAR(any_value(q." + dateFieldName + ")) as `year`, \n "
                        + "MONTH(any_value(q." + dateFieldName + ")) as `month`";
                break;
            case lastQuarter:
                selectFields = "YEAR(any_value(q." + dateFieldName + ")) as `year`, \n"
                        + "MONTHNAME(any_value(q." + dateFieldName + ")) as `month_name`, \n"
                        + "MONTH(any_value(q." + dateFieldName + ")) as `month`";
                break;
            case last6Months:
                selectFields = "YEAR(any_value(q." + dateFieldName + ")) as `year`, \n"
                        + "MONTHNAME(any_value(q." + dateFieldName + ")) as `month_name`, \n"
                        + "MONTH(any_value(q." + dateFieldName + ")) as `month`";
                break;
            case lastYear:
                selectFields = "YEAR(any_value(q." + dateFieldName + ")) as `year`, \n"
                        + "MONTHNAME(any_value(q." + dateFieldName + ")) as `month_name`,\n"
                        + "MONTH(any_value(q." + dateFieldName + ")) as `month`";
                break;
        }
        return selectFields;
    }

    public static String getWhereClause(DateRanges dateRange, String dateFieldName) {
        String whereClause = "";
        switch (dateRange) {
            case last7Days:
                whereClause = "any_value(q." + dateFieldName + ") > DATE_SUB(NOW(), INTERVAL 7 day) \n";
                break;
            case lastMonth:
                whereClause = "MONTH(any_value(q." + dateFieldName + ")) = MONTH(DATE_SUB(NOW(), INTERVAL 1 MONTH))\n"
                        + "and YEAR(any_value(q." + dateFieldName + ")) = YEAR(DATE_SUB(NOW(), INTERVAL 1 MONTH))\n";
                break;
            case lastQuarter:
                whereClause = "QUARTER(any_value(q." + dateFieldName + ")) = QUARTER(DATE_SUB(NOW(), INTERVAL 3 MONTH))\n"
                        + "and YEAR(any_value(q." + dateFieldName + ")) = YEAR(DATE_SUB(NOW(), INTERVAL 3 MONTH))\n";
                break;
            case last6Months:
                whereClause = "any_value(q." + dateFieldName + ") > DATE_SUB(NOW(), INTERVAL 7 MONTH)\n";
                break;
            case lastYear:
                whereClause = "YEAR(any_value(q." + dateFieldName + ")) = YEAR(DATE_SUB(NOW(), INTERVAL 1 YEAR))\n";
                break;
        }
        return whereClause;
    }
    
    public static String getWhereClause(Date fromDate, Date toDate, String dateFieldName) {
        String whereClause = "";        
            
        if((fromDate!= null)&&(toDate!=null))

        {           
          //whereClause=whereClause+' '+'and (closeDate>=:fromDate AND closeDate<= :toDate+1)';        

        }

        return whereClause;
    }

    public static String getGroupByForDateAxis(DateRanges dateRange, String dateFieldName) {
        String groupBy = "";
        switch (dateRange) {
            case last7Days:
                groupBy = "DAYOFWEEK(q." + dateFieldName + ")\n";
                break;
            case lastMonth:
                groupBy = "DATE(q." + dateFieldName + ")\n";
                break;
            case lastQuarter:
                groupBy = "YEAR(q." + dateFieldName + "), MONTH(q." + dateFieldName + ")\n";
                break;
            case last6Months:
                groupBy = "YEAR(q." + dateFieldName + "), MONTH(q." + dateFieldName + ")\n";
                break;
            case lastYear:
                groupBy = "YEAR(q." + dateFieldName + "), MONTH(q." + dateFieldName + ")\n";
                break;
        }
        return groupBy;
    }

    public static String buildQueryForDateAxis(DateRanges dateRange, String dateFieldName,
            String groupingFunction, String fromQuery) {
        String query = "select " + getSelectFieldsForDateAxis(dateRange, dateFieldName) + ", "
                + groupingFunction + " as `value` \n"
                + "from (" + fromQuery + ") q \n"
                + "where " + getWhereClause(dateRange, dateFieldName)
                + "group by " + getGroupByForDateAxis(dateRange, dateFieldName)
                + "order by any_value(q." + dateFieldName + ")";
        return query;
    }

    public static String buildQueryForCustomAxis(DateRanges dateRange, String dateFieldName,
            String groupingFunction, String fromQuery, String groupByForAxis) {
        String query = "";
        if (dateRange != null) {
            query = "select " + groupByForAxis + ", "
                    + groupingFunction + " as `value` \n"
                    + "from (" + fromQuery + ") q \n"
                    + "where " + getWhereClause(dateRange, dateFieldName)
                    + "group by q." + groupByForAxis + "\n"
                    + "order by `value` desc";
        } else {
            query = "select " + groupByForAxis + ", "
                    + groupingFunction + " as `value` \n"
                    + "from (" + fromQuery + ") q \n"
                    + "group by q." + groupByForAxis + "\n"
                    + "order by `value` desc";
        }
        return query;
    }

    public static DataPointGroupBy getDataPointsGroupByForDateAxis(DateRanges dateRange) {
        DataPointGroupBy dpgroupby = DataPointGroupBy.DAY;
        switch (dateRange) {
            case last7Days:
                dpgroupby = DataPointGroupBy.DAY;
                break;
            case lastMonth:
                dpgroupby = DataPointGroupBy.DAY;
                break;
            case lastQuarter:
                dpgroupby = DataPointGroupBy.MONTH;
                break;
            case last6Months:
                dpgroupby = DataPointGroupBy.MONTH;
                break;
            case lastYear:
                dpgroupby = DataPointGroupBy.MONTH;
                break;
        }
        return dpgroupby;
    }

    public static Integer getNoOfDataPointsForDateAxis(DateRanges dateRange) {
        Integer dataPoints = 1;
        switch (dateRange) {
            case last7Days:
                dataPoints = 8;
                break;
            case lastMonth:
                Date d = new Date();
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.MONTH, -1);
                dataPoints = c.getActualMaximum(Calendar.DAY_OF_MONTH);
                break;
            case lastQuarter:
                dataPoints = 3;
                break;
            case last6Months:
                dataPoints = 6;
                break;
            case lastYear:
                dataPoints = 12;
                break;
        }
        return dataPoints;
    }

    public static Calendar getStartDataPointForDateAxis(DateRanges dateRange) {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        switch (dateRange) {
            case last7Days:
                c.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case lastMonth:
                c.add(Calendar.MONTH, -1);
                c.set(Calendar.DATE, 1);
                break;
            case lastQuarter:
                int quarter = (month / 3) + 1;
                switch (quarter) {
                    case 1:
                        c.set(Calendar.YEAR, year - 1);
                        c.set(Calendar.MONTH, Calendar.OCTOBER);
                        break;
                    case 2:
                        c.set(Calendar.MONTH, Calendar.JANUARY);
                        break;
                    case 3:
                        c.set(Calendar.MONTH, Calendar.APRIL);
                        break;
                    case 4:
                        c.set(Calendar.MONTH, Calendar.JULY);
                        break;
                }
                c.set(Calendar.DATE, 1);
                break;
            case last6Months:
                c.add(Calendar.MONTH, -6);
                c.set(Calendar.DATE, 1);
                break;
            case lastYear:
                c.set(Calendar.YEAR, year - 1);
                c.set(Calendar.MONTH, Calendar.JANUARY);
                c.set(Calendar.DATE, 1);
                break;
        }
        return c;
    }

    public static void getAreaChartDataObjForMonth(ResultSet rs,
            LineChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer months = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < months; i++) {
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(monthNames[m - 1] + " " + y);
            if (dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.MONTH, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void getAreaChartDataObjForDay(ResultSet rs,
            LineChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer days = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int ddw = dow, ddm = dom, dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < days; i++) {
            dow = c.get(Calendar.DAY_OF_WEEK);
            dom = c.get(Calendar.DAY_OF_MONTH);
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    ddw = rs.getInt("day_week");
                    ddm = rs.getInt("day_month");
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(dayShortNames[dow - 1] + " " + dom + " " + monthShortNames[m - 1]);
            if (ddm == dom && dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.DAY_OF_WEEK, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void getBarChartDataObj(ResultSet rs, BarChartData chartData, String label,
            String groupByForAxis, Integer x)
            throws SQLException {
        BarDataSet ds = new BarDataSet(
                "rgba(54, 162, 235,0.2)",
                "rgba(54, 162, 235,1)",
                "1",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)"
        //                "rgba(151,187,205,0.5)",
        //                "rgba(151,187,205,0.8)",
        //                "rgba(151,187,205,0.75)",
        //                "rgba(151,187,205,1)"
        );
        int i = 0;
        x = (x == null ? i + 1 : x);
        while (rs.next() && i < x) {
            chartData.addLabel(rs.getString(groupByForAxis));
            ds.addData(rs.getBigDecimal("value"));
        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void getBarChartDataObj(ResultSet rs, BarChartData chartData, String label)
            throws SQLException {
        BarDataSet ds = new BarDataSet(
                "rgba(54, 162, 235,0.2)",
                "rgba(54, 162, 235,1)",
                "1",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)"
        //                "rgba(151,187,205,0.5)",
        //                "rgba(151,187,205,0.8)",
        //                "rgba(151,187,205,0.75)",
        //                "rgba(151,187,205,1)"
        );
        while (rs.next()) {

            ds.addData(rs.getBigDecimal("value"));
        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    //Ensure datapoints are same when merging 2 areaCharts    
    public static void mergeBarChartData(BarChartData d1, BarChartData d2) {
        BarDataSet d = new BarDataSet(
                "rgba(255,99,132,0.2)",
                "rgba(255,99,132,1)",
                "1",
                "rgba(255,99,132,0.4)",
                "rgba(255,99,132,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,0.8)",
        //                "rgba(220,220,220,0.75)",
        //                "rgba(220,220,220,1)"
        );
        d.setLabel(d2.getDatasets().get(0).getLabel());
        d.setData(d2.getDatasets().get(0).getData());
        d1.addDataset(d);
    }

    public static void mergeBarChartData(BarChartData d1, BarChartData d2, BarChartData d3) {
        BarDataSet d4 = new BarDataSet(
                "rgba(255,99,132,0.2)",
                "rgba(255,99,132,1)",
                "1",
                "rgba(255,99,132,0.4)",
                "rgba(255,99,132,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,0.8)",
        //                "rgba(220,220,220,0.75)",
        //                "rgba(220,220,220,1)"
        );
        d4.setLabel(d2.getDatasets().get(0).getLabel());
        d4.setData(d2.getDatasets().get(0).getData());
        d1.addDataset(d4);
        BarDataSet d5 = new BarDataSet(
                "rgba(0,128,0,0.3)",
                "rgba(0,128,0,1)",
                "1",
                "rgba(0,128,0,0.4)",
                "rgba(0,128,0,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,0.8)",
        //                "rgba(220,220,220,0.75)",
        //                "rgba(220,220,220,1)"
        );
        d5.setLabel(d3.getDatasets().get(0).getLabel());
        d5.setData(d3.getDatasets().get(0).getData());
        d1.addDataset(d5);
    }
    
    public static void getBarChartDataObjForMonth(ResultSet rs,
            BarChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer months = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < months; i++) {
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(monthNames[m - 1] + " " + y);
            if (dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.MONTH, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void getBarChartDataObjForDay(ResultSet rs,
            BarChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer days = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int ddw = dow, ddm = dom, dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < days; i++) {
            dow = c.get(Calendar.DAY_OF_WEEK);
            dom = c.get(Calendar.DAY_OF_MONTH);
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    ddw = rs.getInt("day_week");
                    ddm = rs.getInt("day_month");
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(dayShortNames[dow - 1] + " " + dom + " " + monthShortNames[m - 1]);
            if (ddm == dom && dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.DAY_OF_WEEK, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void getPieChartDataObj(ResultSet rs, PieChartData chartData, String label,
            String groupByForAxis, Integer x)
            throws SQLException {
        PieDataSet ps = new PieDataSet(
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "2",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)"
        //                "rgba(151,187,205,0.5)",
        //                "rgba(151,187,205,0.8)",
        //                "rgba(151,187,205,0.75)",
        //                "rgba(151,187,205,1)"
        );
        int i = 0;
        x = (x == null ? i + 1 : x);
       // double z = 0.1;
        while (rs.next() && i < x) {
           // String y = "rgba(50, 162, 200," + z + ")";
         //  ps.setBackgroundColor("rgba(54, 162, 235," + z + ")");
            chartData.addLabel(rs.getString(groupByForAxis));
            ps.addData(rs.getBigDecimal("value"));
         //   chartData.setColor(y);
          //  z++;
        }
        ps.setLabel(label);
        chartData.addDataset(ps);
    }
    
    public static void getPieChartDataObjForMonth(ResultSet rs,
            PieChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer months = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < months; i++) {
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(monthNames[m - 1] + " " + y);
            if (dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.MONTH, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }
    
     public static void getPieChartDataObjForDay(ResultSet rs,
            PieChartData chartData, String label, DateRanges dateRange)
            throws SQLException {
        LineDataSet ds = new LineDataSet(
                "true",
                "rgba(54, 162, 235,0.4)",
                "rgba(54, 162, 235,1)",
                "rgba(54, 162, 235,1)",
                "#fff",
                "rgba(54, 162, 235,1)",
                "rgba(220,220,220,1)"
        //                "rgba(151,187,205,0.2)",
        //                "rgba(151,187,205,1)",
        //                "rgba(151,187,205,1)",
        //                "#fff",
        //                "#fff",
        //                "rgba(151,187,205,1)"
        );
        Integer days = getNoOfDataPointsForDateAxis(dateRange);
        Calendar c = getStartDataPointForDateAxis(dateRange);
        int dow = c.get(Calendar.DAY_OF_WEEK);
        int dom = c.get(Calendar.DAY_OF_MONTH);
        int m = c.get(Calendar.MONTH) + 1;
        int y = c.get(Calendar.YEAR);
        int ddw = dow, ddm = dom, dm = m, dy = y;
        BigDecimal value = new BigDecimal(0);
        boolean getNext = true;
        for (int i = 0; i < days; i++) {
            dow = c.get(Calendar.DAY_OF_WEEK);
            dom = c.get(Calendar.DAY_OF_MONTH);
            m = c.get(Calendar.MONTH) + 1;
            y = c.get(Calendar.YEAR);
            if (getNext) {
                if (rs.next()) {
                    ddw = rs.getInt("day_week");
                    ddm = rs.getInt("day_month");
                    dm = rs.getInt("month");
                    dy = rs.getInt("year");
                    value = rs.getBigDecimal("value");
                }
            }
            chartData.addLabel(dayShortNames[dow - 1] + " " + dom + " " + monthShortNames[m - 1]);
            if (ddm == dom && dm == m && dy == y) {
                getNext = true;
                ds.addData(value);
            } else {
                getNext = false;
                ds.addData(new BigDecimal(0));
            }
            c.add(Calendar.DAY_OF_WEEK, 1);

        }
        ds.setLabel(label);
        chartData.addDataset(ds);
    }

    public static void mergePieChartData(PieChartData d1, PieChartData d2, PieChartData d3) {
        PieDataSet d4 = new PieDataSet(
                "rgba(255,99,132,0.2)",
                "rgba(255,99,132,1)",
                "1",
                "rgba(255,99,132,0.4)",
                "rgba(255,99,132,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,0.8)",
        //                "rgba(220,220,220,0.75)",
        //                "rgba(220,220,220,1)"
        );
        d4.setLabel(d2.getDatasets().get(0).getLabel());
        d4.setData(d2.getDatasets().get(0).getData());
        d1.addDataset(d4);
        PieDataSet d5 = new PieDataSet(
                "rgba(0,128,0,0.3)",
                "rgba(0,128,0,1)",
                "1",
                "rgba(0,128,0,0.4)",
                "rgba(0,128,0,1)"
        //                "rgba(220,220,220,0.2)",
        //                "rgba(220,220,220,0.8)",
        //                "rgba(220,220,220,0.75)",
        //                "rgba(220,220,220,1)"
        );
        d5.setLabel(d3.getDatasets().get(0).getLabel());
        d5.setData(d3.getDatasets().get(0).getData());
        d1.addDataset(d5);
    }

}
