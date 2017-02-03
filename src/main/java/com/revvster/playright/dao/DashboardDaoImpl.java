/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.dao;

import com.revvster.playright.model.Data;
import com.revvster.playright.model.User;
import com.revvster.playright.ui.charts.LineChartData;
import com.revvster.playright.ui.charts.ChartsUtil;
import com.revvster.playright.ui.charts.LineDataSet;
import com.revvster.playright.ui.charts.BarChartData;
import com.revvster.playright.ui.charts.PieChartData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author REVV-03
 */
public class DashboardDaoImpl extends DaoHelper implements DashboardDao {

    private static final Logger logger = LogManager.getLogger(DashboardDaoImpl.class.getName());

    private static final Map<Integer, String> colors;

    static {
        Map<Integer, String> aMap = new HashMap<>();
        aMap.put(1, "rgba(26, 188, 156,1.0)");
        aMap.put(2, "rgba(39, 174, 96,1.0)");
        aMap.put(3, "rgba(231, 76, 60,1.0)");
        aMap.put(4, "rgba(189, 195, 199,1.0)");
        aMap.put(5, "rgba(155, 89, 182,1.0)");
        aMap.put(6, "rgba(41, 128, 185,1.0)");
        aMap.put(7, "rgba(230, 126, 34,1.0)");
        aMap.put(8, "rgba(243, 156, 18,1.0)");
        colors = Collections.unmodifiableMap(aMap);
    }

    public DashboardDaoImpl() {
        super();
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public Integer getAvailableUnitsInProject(Integer project) throws SQLException {
        logger.debug("getAvailableUnitsInProject::START");
        Integer noOfUnits = getUnitsInProjectByState(project, "available");
        logger.debug("getAvailableUnitsInProject::END");
        return noOfUnits;
    }

    private Integer getUnitsInProjectByState(Integer project, String state) throws SQLException {
        logger.debug("getUnitsInProjectByState::START");
        Integer noOfUnits = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) as units from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "where ci.project = ? \n"
                + "and ci.active = 1 \n"
                + "and lov.value = ?");
        ps.setInt(1, project);
        ps.setString(2, state);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            noOfUnits = rs.getInt("units");
        }
        close(rs);
        close(ps);
        logger.debug("getUnitsInProjectByState::END");
        return noOfUnits;
    }

    @Override
    public Integer getVisitsInProject(Integer project) throws SQLException {
        logger.debug("getVisitsInProject::START");
        Integer noOfVisits = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) as visits from sb_customer_visits\n"
                + "where project = ?\n");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            noOfVisits = rs.getInt("visits");
        }
        close(rs);
        close(ps);
        logger.debug("getVisitsInProject::END");
        return noOfVisits;
    }

    @Override
    public Integer getSoldUnitsInProject(Integer project) throws SQLException {
        logger.debug("getSoldUnitsInProject::START");
        Integer noOfUnits = 0;
        noOfUnits += getUnitsInProjectByState(project, "sold");
        noOfUnits += getUnitsInProjectByState(project, "registered");
        logger.debug("getSoldUnitsInProject::END");
        return noOfUnits;
    }

    @Override
    public Integer getSoldUnitsForProject(Integer project) throws SQLException {
        logger.debug("getSoldUnitsForProject::START");
        Integer noOfUnits = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select count(*) as sold_units from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "where ci.project = ?\n"
                + "and lov.value in ('sold')");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            noOfUnits = rs.getInt("sold_units");
        }
        close(rs);
        close(ps);
        logger.debug("getSoldUnitsForProject::END");
        return noOfUnits;
    }

    @Override
    public String getChampionForProject(Integer project) throws SQLException {
        logger.debug("getChampionForProject::START");
        String champion = "Nobody";
        PreparedStatement ps;
        ps = conn.prepareStatement("select ci.sales_rep, count(ci.sales_rep) as maxsale from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "where lov.value = 'sold'\n"
                + "and ci.project = ? \n"
                + "group by ci.sales_rep\n"
                + "order by count(ci.sales_rep) desc");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            champion = getFullName(rs.getInt("sales_rep"));
        }
        close(rs);
        close(ps);
        logger.debug("getChampionForProject::END");
        return champion;
    }

    @Override
    public String getHighestSellingForProject(Integer project) throws SQLException {
        logger.debug("getHighestSellingForProject::START");
        String sellingType = "";
        PreparedStatement ps;
        ps = conn.prepareStatement("select cao.name highest_selling_type, count(ci.id) as count\n"
                + "from sb_catalog_items ci \n"
                + "join sb_lov ls on ci.state = ls.id\n"
                + "join sb_catalog_attr_val cav on ci.id = cav.catalog_item\n"
                + "join sb_catalog_attribute_options cao on cav.option = cao.id\n"
                + "join sb_lov_attr lta on cao.option = lta.id\n"
                + "join sb_lov lt on lta.lov = lt.id\n"
                + "where ls.value in ('sold', 'registered')\n"
                + "and lt.value = 'Type'\n"
                + "and ci.project = ? \n"
                + "group by cao.name\n"
                + "order by count(ci.id) desc\n"
                + "limit 1");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            sellingType = rs.getString("highest_selling_type");
        }
        close(rs);
        close(ps);
        logger.debug("getHighestSellingForProject::END");
        return sellingType.equals("") ? "Type Not Defined On Project" : sellingType;
    }

    @Override
    public LineChartData getConversionRateBySalesRep(Integer project) throws SQLException {
        logger.debug("getConversionRateBySalesRep::START");
        LineChartData a = new LineChartData();
        a.addLabel("Conversion Rate");
        PreparedStatement ps;
        ps = conn.prepareStatement("select @row_number:=@row_number+1 AS row_number,\n"
                + "a.name, truncate(a.sold_units/a.customers, 2) as data from (\n"
                + "select ci.sales_rep, concat(concat(u.first_name, ' '), u.last_name) as name,\n"
                + "(select count(*) from sb_customer_sales_rep crp \n"
                + "	where crp.sales_rep = ci.sales_rep \n"
                + "    and crp.project = ci.project\n"
                + "    and crp.active = 1) as customers, \n"
                + "count(*) as sold_units\n"
                + "from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "left join sb_users u on ci.sales_rep = u.id\n"
                + "where ci.project = ?\n"
                + "and lov.value in ('sold', 'registered')\n"
                + "and ci.active = 1\n"
                + "group by sales_rep) a,\n"
                + "(SELECT @row_number:=0) AS t");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            LineDataSet ds = new LineDataSet();
//            populateDataSetSingleData(ds, rs);
            a.addDataset(ds);
        }
        close(rs);
        close(ps);
        logger.debug("getConversionRateBySalesRep::END");
        return a;
    }

    @Override
    public List<PieChartData> getSalesQuotientBySalesRep(Integer project) throws SQLException {
        logger.debug("getSalesQuotientBySalesRep::START");
        List<PieChartData> pieChartData = new ArrayList<>();
//        PieChartData p = new PieChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement("select @row_number:=@row_number+1 AS row_number,\n"
                + "a.name, a.sold_units as data from (\n"
                + "select ci.sales_rep, concat(concat(u.first_name, ' '), u.last_name) as name, \n"
                + "count(*) as sold_units\n"
                + "from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "left join sb_users u on ci.sales_rep = u.id\n"
                + "where ci.project = 1\n"
                + "and lov.value in ('sold', 'registered')\n"
                + "and ci.active = 1\n"
                + "group by sales_rep\n"
                + "union\n"
                + "select 0, \"Available Units\" as name, \n"
                + "count(*) as sold_units\n"
                + "from sb_catalog_items ci\n"
                + "left join sb_lov lov on ci.state = lov.id\n"
                + "where ci.project = 1\n"
                + "and lov.value not in ('sold', 'registered', 'unavailable')\n"
                + "and ci.active = 1) a,\n"
                + "(SELECT @row_number:=0) AS t");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            PieChartData pc = new PieChartData();
            populatePieData(pc, rs);
            pieChartData.add(pc);
        }
        close(rs);
        close(ps);
        logger.debug("getSalesQuotientBySalesRep::END");
        return pieChartData;
    }

    private void populatePieData(PieChartData pc, ResultSet rs) throws SQLException {

        pc.setData(rs.getInt("data"));
        pc.setLabel(rs.getString("name"));
        String color = colors.get(rs.getInt("row_number"));
        pc.setColor(color);
        pc.setHighlight(color);
    }

    @Override
    public Integer getBlocksInProject(Integer project) throws SQLException {
        logger.debug("getBlocksInProject::START");
        Integer noOfBlocks = 0;
        PreparedStatement ps;
        ps = conn.prepareStatement("select blocks from sb_projects p\n"
                + "where p.id=?");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            noOfBlocks = rs.getInt("blocks");
        }
        close(rs);
        close(ps);
        logger.debug("getBlocksInProject::END");
        return noOfBlocks;
    }

    @Override
    public String getCityInProject(Integer project) throws SQLException {
        logger.debug("getCityInProject::START");
        String cityName = "NULL";
        PreparedStatement ps;
        ps = conn.prepareStatement("select city from sb_projects p\n"
                + "where p.id=?");
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            cityName = rs.getString("city");
        }
        close(rs);
        close(ps);
        logger.debug("getCityInProject::END");
        return cityName;
    }

    @Override
    public LineChartData getSalesDataForProject(Integer project, ChartsUtil.DateRanges dateRange)
            throws SQLException {
        logger.debug("getSalesDataForProject::START");
        LineChartData chartData = new LineChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForDateAxis(dateRange,
                "actioned_on", "count(*)", "select ci.id, ca.actioned_on, lovb.`value` \n "
                + "from sb_catalog_items ci\n"
                + "left join sb_lov lova on ci.state = lova.id\n"
                + "left join sb_customer_action ca \n "
                + "on ci.customer = ca.customer and ci.id = ca.catalog_item\n"
                + "left join sb_lov lovb on ca.action = lovb.id\n"
                + "where lova.`value` in ('sold', 'registered')\n"
                + "and lovb.`value` = 'sold'\n"
                + "and ci.project = ? "));
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        if (ChartsUtil.DataPointGroupBy.MONTH == ChartsUtil.getDataPointsGroupByForDateAxis(dateRange)) {
            ChartsUtil.getAreaChartDataObjForMonth(rs, chartData, "Sales", dateRange);
        } else {
            ChartsUtil.getAreaChartDataObjForDay(rs, chartData, "Sales", dateRange);
        }
        close(rs);
        close(ps);
        logger.debug("getSalesDataForProject::END");
        return chartData;
    }

    @Override
    public LineChartData getVisitsDataForProject(Integer project, ChartsUtil.DateRanges dateRange)
            throws SQLException {
        logger.debug("getVisitsDataForProject::START");
        LineChartData chartData = new LineChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForDateAxis(dateRange,
                "visited_on", "count(*)", "select id, visited_on from sb_customer_visits\n"
                + "where project = ?"));
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        if (ChartsUtil.DataPointGroupBy.MONTH == ChartsUtil.getDataPointsGroupByForDateAxis(dateRange)) {
            ChartsUtil.getAreaChartDataObjForMonth(rs, chartData, "Visits", dateRange);
        } else {
            ChartsUtil.getAreaChartDataObjForDay(rs, chartData, "Visits", dateRange);
        }
        close(rs);
        close(ps);
        logger.debug("getVisitsDataForProject::END");
        return chartData;
    }

    @Override
    public LineChartData getVisitsDataForUser(Integer user, ChartsUtil.DateRanges dateRange)
            throws SQLException {
        logger.debug("getVisitsDataForUser::START");
        LineChartData chartData = new LineChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForDateAxis(dateRange,
                "visited_on", "count(*)", "select id, visited_on from sb_customer_visits\n"
                + "where sales_rep = ?"));
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        if (ChartsUtil.DataPointGroupBy.MONTH == ChartsUtil.getDataPointsGroupByForDateAxis(dateRange)) {
            ChartsUtil.getAreaChartDataObjForMonth(rs, chartData, "Visits", dateRange);
        } else {
            ChartsUtil.getAreaChartDataObjForDay(rs, chartData, "Visits", dateRange);
        }
        close(rs);
        close(ps);
        logger.debug("getVisitsDataForUser::END");
        return chartData;
    }

    @Override
    public BarChartData getTopXSalesRepPerfForProject(Integer project, ChartsUtil.DateRanges dateRange, Integer x)
            throws SQLException {
        logger.debug("getTop5SalesRepPerfForProject::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "actioned_on", "count(*)", "select concat(concat(u.first_name, ' '), u.last_name) as sales_rep, \n"
                + "ca.actioned_on, lovb.`value` from sb_catalog_items ci\n"
                + "left join sb_lov lova on ci.state = lova.id\n"
                + "left join sb_customer_action ca \n"
                + " on ci.customer = ca.customer and ci.id = ca.catalog_item\n"
                + "left join sb_lov lovb on ca.action = lovb.id\n"
                + "left join sb_users u on ci.sales_rep = u.id\n"
                + "where lova.`value` in ('sold', 'registered')\n"
                + "and lovb.`value` = 'sold'\n"
                + "and ci.project = ? \n", "sales_rep"));
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Sold", "sales_rep", x);
        close(rs);
        close(ps);
        logger.debug("getTop5SalesRepPerfForProject::END");
        return chartData;
    }

    @Override
    public LineChartData getSalesDataForUser(Integer user, ChartsUtil.DateRanges dateRange)
            throws SQLException {
        logger.debug("getSalesDataForUser::START");
        LineChartData chartData = new LineChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForDateAxis(dateRange,
                "actioned_on", "count(*)", "select concat(concat(u.first_name, ' '), u.last_name) as sales_rep, \n"
                + "ca.actioned_on, lovb.`value` from sb_catalog_items ci\n"
                + "left join sb_lov lova on ci.state = lova.id\n"
                + "left join sb_customer_action ca \n"
                + " on ci.customer = ca.customer and ci.id = ca.catalog_item\n"
                + "left join sb_lov lovb on ca.action = lovb.id\n"
                + "left join sb_users u on ci.sales_rep = u.id\n"
                + "where lova.`value` in ('sold', 'registered')\n"
                + "and lovb.`value` = 'sold'\n"
                + "and ci.sales_rep = ? \n"));
        ps.setInt(1, user);
        ResultSet rs = ps.executeQuery();
        if (ChartsUtil.DataPointGroupBy.MONTH == ChartsUtil.getDataPointsGroupByForDateAxis(dateRange)) {
            ChartsUtil.getAreaChartDataObjForMonth(rs, chartData, "Sold", dateRange);
        } else {
            ChartsUtil.getAreaChartDataObjForDay(rs, chartData, "Sold", dateRange);
        }
        //ChartsUtil.getBarChartDataObj(rs, chartData, "Sold", "sales_rep", 1);
        close(rs);
        close(ps);
        logger.debug("getSalesDataForUser::END");
        return chartData;
    }

    @Override
    public BarChartData getAnswerDistributionByCustomer(Integer selectedQuestion)
            throws SQLException {
        logger.debug("getAnswerDistributionByCustomer::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(customer)", "select distinct qo.`option`, a.customer from sb_question_options qo\n"
                + "left outer join sb_answer_options ao on qo.id = ao.`option`\n"
                + "left outer join sb_answers a on ao.answer = a.id\n"
                + "where qo.question = ?", "`option`"));
        ps.setInt(1, selectedQuestion);
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Customers", "option", null);
        close(rs);
        close(ps);
        logger.debug("getAnswerDistributionByCustomer::END");
        return chartData;
    }

    @Override
    public BarChartData getAwgTimeSpent(Integer project, ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getAwgTimeSpent::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "visited_on", "round(avg(time_spent))", "select cv.visited_on,s.name as section_name,st.time_spent\n"
                + "FROM sb_customer_visits cv \n"
                + "left join sb_customers c on cv.customer = c.id\n"
                + "left join sb_projects p on cv.project = p.id\n"
                + "left join sb_section_time st on cv.id = st.customer_visit\n"
                + "join sb_sections s on st.section = s.id\n"
                + "where cv.project = ?", "`section_name`"));
        ps.setInt(1, project);
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "TimeSpent", "section_name", null);
        close(rs);
        close(ps);
        logger.debug("getAwgTimeSpent::END");
        return chartData;
    }

    @Override
    public BarChartData getLanguageVsArticles(Timestamp inptFrom, Timestamp inptTo) throws SQLException {
        logger.debug("getLanguageVsArticles::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(*)", "select ad.last_updated_on,ad.language from analytics_data ad\n"
                + "where ad.last_updated_on between '" + inptFrom + "' and '" + inptTo + "'", "`language`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "LanguageVsArticles", "language", null);
        close(rs);
        close(ps);
        logger.debug("getLanguageVsArticles::END");
        return chartData;
    }

    @Override
    public PieChartData getEditionVsArticles(Timestamp inptFrom, Timestamp inptTo) throws SQLException {
        logger.debug("getEditionVsArticles::START");
        PieChartData chartData = new PieChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(*)", "SELECT ad.last_updated_on,ad.edition FROM analytics_data ad\n"
                + "where ad.last_updated_on between '" + inptFrom + "' and '" + inptTo + "'", "`edition`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getPieChartDataObj(rs, chartData, "edition Vs articles", "edition", null);
        close(rs);
        close(ps);
        logger.debug("getEditionVsArticles::END");
        return chartData;
    }

    @Override
    public PieChartData getJournalistDistribution(Timestamp inptFrom, Timestamp inptTo) throws SQLException {
        logger.debug("getJournalistDistribution::START");
        PieChartData chartData = new PieChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(*)", "SELECT ad.last_updated_on,ad.journalist_factor FROM analytics_data ad\n"
                + "where ad.last_updated_on between '" + inptFrom + "' and '" + inptTo + "'", "`journalist_factor`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getPieChartDataObj(rs, chartData, "JournalistDistribution", "journalist_factor", null);
        close(rs);
        close(ps);
        logger.debug("getJournalistDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getTopEnglishPrintDistribution(Timestamp inptFrom, Timestamp inptTo) throws SQLException {
        logger.debug("getTopEnglishPrintDistribution::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(*)", "SELECT ad.last_updated_on,ad.news_paper FROM analytics_data ad where ad.language = 'english'\n"
                + "and ad.last_updated_on between '" + inptFrom + "' and '" + inptTo + "'", "`news_paper`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Articles", "news_paper", null);
        close(rs);
        close(ps);
        logger.debug("getTopEnglishPrintDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getTopVernacularPrintDistribution(Timestamp inptFrom, Timestamp inptTo) throws SQLException {
        logger.debug("getTopVernacularPrintDistribution::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(*)", "SELECT ad.last_updated_on,ad.news_paper FROM analytics_data ad where ad.language <> 'english'\n"
                + "and ad.last_updated_on between '" + inptFrom + "' and '" + inptTo + "'", "`news_paper`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Articles", "news_paper", null);
        close(rs);
        close(ps);
        logger.debug("getTopVernacularPrintDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getLanguageVsArticles(ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getLanguageVsArticles::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "last_updated_on", "count(*)", "select ad.last_updated_on,ad.language from analytics_data ad", "`language`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "LanguageVsArticles", "language", null);
        close(rs);
        close(ps);
        logger.debug("getLanguageVsArticles::END");
        return chartData;
    }

    @Override
    public PieChartData getEditionVsArticles(ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getEditionVsArticles::START");
        PieChartData chartData = new PieChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "last_updated_on", "count(edition)", "SELECT ad.last_updated_on,ad.edition FROM analytics_data ad", "`edition`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getPieChartDataObj(rs, chartData, "edition Vs articles", "edition", null);
        close(rs);
        close(ps);
        logger.debug("getEditionVsArticles::END");
        return chartData;
    }

    @Override
    public PieChartData getJournalistDistribution(ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getJournalistDistribution::START");
        PieChartData chartData = new PieChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "last_updated_on", "count(journalist_factor)", "SELECT ad.last_updated_on,ad.journalist_factor FROM analytics_data ad", "`journalist_factor`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getPieChartDataObj(rs, chartData, "JournalistDistribution", "journalist_factor", null);
        close(rs);
        close(ps);
        logger.debug("getJournalistDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getTopEnglishPrintDistribution(ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getTopEnglishPrintDistribution::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "last_updated_on", "count(news_paper)", "SELECT ad.last_updated_on,ad.news_paper FROM analytics_data ad where ad.language = 'english'", "`news_paper`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Articles", "news_paper", null);
        close(rs);
        close(ps);
        logger.debug("getTopEnglishPrintDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getTopVernacularPrintDistribution(ChartsUtil.DateRanges dateRange) throws SQLException {
        logger.debug("getTopVernacularPrintDistribution::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(dateRange,
                "last_updated_on", "count(news_paper)", "SELECT ad.last_updated_on,ad.news_paper FROM analytics_data ad where ad.language <> 'english'", "`news_paper`"));
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, "Articles", "news_paper", null);
        close(rs);
        close(ps);
        logger.debug("getTopVernacularPrintDistribution::END");
        return chartData;
    }

    @Override
    public BarChartData getInventoryStatesByAttribute(Integer selectedCatalogAttr, String attributes, String label) throws SQLException {
        logger.debug("getInventorySalesByAttribute::START");
        BarChartData chartData = new BarChartData();
        PreparedStatement ps;
        ps = conn.prepareStatement(ChartsUtil.buildQueryForCustomAxis(null,
                "", "count(id)", "select cao.name, ci.id from sb_catalog_attribute_options cao \n"
                + "left join (select a.id, a.state, cav.`option` as opt from \n"
                + "	sb_catalog_attr_val cav  \n"
                + "	join sb_catalog_items a on cav.catalog_item = a.id\n"
                + "	join sb_lov b on a.state = b.id \n"
                + "	where b.`value` in (" + attributes + ")\n"
                + ") ci on cao.id = ci.opt \n"
                + "where cao.attribute = ? ", "name"));
        ps.setInt(1, selectedCatalogAttr);
        ResultSet rs = ps.executeQuery();
        ChartsUtil.getBarChartDataObj(rs, chartData, label, "name", null);
        close(rs);
        close(ps);
        logger.debug("getInventorySalesByAttribute::END");
        return chartData;
    }

    @Override
    public Data getData(Integer id)
            throws SQLException {
        logger.debug("getData::ByID::START::" + id);
        PreparedStatement ps = null;
        Data data = new Data();
        ps = conn.prepareStatement("select ad.* from analytics_data ad\n"
                + "where ad.id = ?");
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            data = getDataObj(rs);
            //  setAuditable(rs, data);
        }
        close(rs);
        close(ps);
        logger.debug("getData::ByID::END");
        return data;
    }

    @Override
    public List<Data> getDataByContext(User loggedInUser)
            throws SQLException {
        logger.debug("getDataByContext::START::");
        List<Data> datas = new ArrayList<>();
        PreparedStatement ps;
        ps = conn.prepareStatement("select * from analytics_data order by news_date desc");
        // ps.setInt(1, customer);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Data d = getDataObj(rs);
            datas.add(d);
        }
        close(rs);
        close(ps);
        logger.debug("getDataByContext::END");
        return datas;
    }

    @Override
    public Integer createData(Data data)
            throws SQLException {
        logger.debug("createData::START::");
        PreparedStatement ps = conn.prepareStatement("INSERT INTO analytics_data \n"
                + "(news_date, news_paper, language, \n"
                + "headline, edition, supplement, \n"
                + "source, page_no, \n"
                + "height, width, total_article_size, \n"
                + "circulation_figure, quantitative_AVE, journalist_factor, \n"
                + "image, image_exists, file_size, file_type,\n"
                + " created_by, last_updated_by, image_filename) \n"
                + "VALUES (?, ?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, \n"
                + "?, ?, ?, \n"
                + "?, ?, ?,\n"
                + "?, ?, ?, ?,\n"
                + "?, ?, ?)");
        ps.setTimestamp(1, data.getNewsDate());
        ps.setString(2, data.getNewsPaper());
        ps.setString(3, data.getLanguage());
        ps.setString(4, data.getHeadline());
        ps.setString(5, data.getEdition());
        ps.setString(6, data.getSupplement());
        ps.setString(7, data.getSource());
        ps.setInt(8, data.getPageNo());
        ps.setInt(9, data.getHeight());
        ps.setInt(10, data.getWidth());
        ps.setInt(11, data.getTotalArticleSize());
        ps.setInt(12, data.getCirculationFigure());
        ps.setInt(13, data.getQuantitativeAVE());
        ps.setInt(14, data.getJournalistFactor());
        ps.setBlob(15, data.getImage());
        ps.setString(16, data.getImageExists());
        ps.setInt(17, data.getFileSize());
        ps.setString(18, data.getFileType());
        ps.setInt(19, data.getCreatedBy());
        ps.setInt(20, data.getLastUpdatedBy());
        ps.setString(21, data.getImageFileName());

        ps.executeUpdate();
        Integer id = getLastInsertId();
        if (id > 0) {
            data.setId(id);
        }
        close(ps);
        logger.debug("createData::END");
        return id;
    }

    @Override
    public void updateData(Data data)
            throws SQLException {
        logger.debug("updateData::START::");
        PreparedStatement ps;

        ps = conn.prepareStatement("update analytics_data\n"
                + "set news_date = ?,\n"
                + "news_paper = ?,\n"
                + "language = ?,\n"
                + "headline = ?,\n"
                + "edition = ?,\n"
                + "supplement = ?,\n"
                + "source = ?,\n"
                + "page_no = ?,\n"
                + "height = ?,\n"
                + "width = ?,\n"
                + "total_article_size = ?,\n"
                + "circulation_figure = ?,\n"
                + "quantitative_AVE = ?,\n"
                + "journalist_factor = ?,\n"
                //  + "image = ?,\n"
                + "image_exists = ?,\n"
                // + "file_size = ?,\n"
                //  + "file_type = ?,\n"
                + "last_updated_by = ?\n"
                // + "image_filename = ?\n"
                + "where id = ?");
        ps.setTimestamp(1, data.getNewsDate());
        ps.setString(2, data.getNewsPaper());
        ps.setString(3, data.getLanguage());
        ps.setString(4, data.getHeadline());
        ps.setString(5, data.getEdition());
        ps.setString(6, data.getSupplement());
        ps.setString(7, data.getSource());
        ps.setInt(8, data.getPageNo());
        ps.setInt(9, data.getHeight());
        ps.setInt(10, data.getWidth());
        ps.setInt(11, data.getTotalArticleSize());
        ps.setInt(12, data.getCirculationFigure());
        ps.setInt(13, data.getQuantitativeAVE());
        ps.setInt(14, data.getJournalistFactor());
        //  ps.setBlob(15, data.getImage());
        ps.setString(15, data.getImageExists());
        //  ps.setInt(17, data.getFileSize());        
        //  ps.setString(18, data.getFileType());
        ps.setInt(16, data.getLastUpdatedBy());
        //  ps.setString(20, data.getImageFileName());
        ps.setInt(17, data.getId());
        ps.executeUpdate();
        close(ps);
        if (data.getImage() != null) {
            ps = conn.prepareStatement("update analytics_data\n"
                    + "set image = ?,\n"
                    + "file_size = ?,\n"
                    + "file_type = ?,\n"
                    + "image_filename = ?\n"
                    + "where id = ?");
            ps.setBlob(1, data.getImage());
            ps.setInt(2, data.getFileSize());
            ps.setString(3, data.getFileType());
            ps.setString(4, data.getImageFileName());
            ps.setInt(5, data.getId());
            ps.executeUpdate();
            close(ps);
        }
        logger.debug("updateData::End");
    }

    @Override
    public void deleteData(Integer id)
            throws SQLException {
        logger.debug("deleteData::START::");
        PreparedStatement ps = conn.prepareStatement("DELETE FROM analytics_data \n"
                + " WHERE id = ?");
        //ps.setInt(1, data.getActive());
        //ps.setInt(2, user.getLastUpdatedBy());
        ps.setInt(1, id);
        ps.executeUpdate();
        close(ps);
        logger.debug("deleteData::END");
    }

    @Override
    public Data getPhoto(Integer imageId) throws SQLException {
        logger.debug("getPhoto::START::");
        PreparedStatement ps = null;
        Data d = new Data();
        ps = conn.prepareStatement("SELECT ad.image FROM analytics_data ad where id = ?");
        ps.setInt(1, imageId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            d.setImage(rs.getBlob("image"));
        }
        close(rs);
        close(ps);
        logger.debug("getPhoto::END");
        return d;
    }

    @Override
    public List<Data> getDataForEmail(String inputFrom,
            String inputTo, String inputLanguage, String inputEdition,
            String inputSource, String inputNewsPaper)
            throws SQLException {
        logger.debug("getDataForEmail::START::");
        List<Data> datas = new ArrayList<>();
        PreparedStatement ps;
        // ps.setInt(1, customer);
        String selectDate = "select ad.* from analytics_data ad \n"
                + "where ad.news_date between '" + inputFrom + "' and '" + inputTo + "' \n";
        String language = !"".equals(inputLanguage)
                ? "and ad.language = '" + inputLanguage + "'\n" : "and lower(ad.language) = 'english'\n";
        String edition = !"".equals(inputEdition)
                ? "and ad.edition = '" + inputEdition + "'\n" : "";
        String source = !"".equals(inputSource)
                ? "and ad.source = '" + inputSource + "' \n" : "";
        String newsPaper = !"".equals(inputNewsPaper)
                ? "and ad.news_paper = '" + inputNewsPaper + "'\n" : "";
        String order = "order by date(ad.news_date) desc";

        String query = (selectDate + language + edition
                + source + newsPaper + order);

        ps = conn.prepareStatement(query);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Data d = getDataObj(rs);
            datas.add(d);
        }
        close(rs);
        close(ps);

        if ("".equals(inputLanguage)) {
            language = "and lower(ad.language) != 'english' \n";

            query = (selectDate + language + edition
                    + source + newsPaper + order);

            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Data d = getDataObj(rs);
                datas.add(d);
            }

            close(rs);
            close(ps);

        }

        logger.debug("getDataForEmail::END");
        return datas;
    }

}
