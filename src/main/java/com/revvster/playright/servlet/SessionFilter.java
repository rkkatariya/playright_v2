/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.revvster.playright.servlet;

import com.revvster.playright.util.SystemConstants;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Rahul
 */
@WebFilter(filterName = "SessionFilter", urlPatterns = {"/*"},
        initParams = {
            @WebInitParam(name = "no-validation-urls",
                    value = "/login.jsp,/index.jsp,login.jsp,/login,/pages/image.jsp,/pages/printImage.jsp,/pages/emailAnalytics.jsp"),
            @WebInitParam(name = "exclude-patterns",
                    value = ".css,.js,.woff2,.ttf,.png,.gif"),
            @WebInitParam(name = "exclude-url-patterns",
                    value = "/secure/api,/api,/image,/chart")})
public class SessionFilter implements Filter {

    private static final Logger logger = LogManager.getLogger(SessionFilter.class.getName());

    private ArrayList<String> avoidUrlList;

    private ArrayList<String> excludePatterns;

    private ArrayList<String> excludeUrlPatterns;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public SessionFilter() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        logger.debug("DoBeforeProcessing");
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        logger.debug("DoAfterProcessing");
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {

        logger.debug("doFilter::START");

        doBeforeProcessing(request, response);

        Throwable problem = null;
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;
            
            logger.debug("doFilter::handleRestRequests");
            //Handle Rest Requests
            boolean excludedUrl = false;
            boolean redirect = false;
            String path = req.getRequestURI();
            for (String urlPattern : excludeUrlPatterns) {
                if (path.startsWith(req.getServletContext().getContextPath() + urlPattern)) {
                    logger.debug("doFilter::handleRestRequests::MATCHED::"+urlPattern);
                    excludedUrl = true;
                    break;
                }
            }
            if (!excludedUrl) {
                
                String url = req.getServletPath();
                boolean allowedRequest = false;
                logger.debug("doFilter::RequestedURL::"+url);
                if (avoidUrlList.contains(url)) {
                    logger.debug("doFilter::excludedUrl::MATCHED::"+url);
                    allowedRequest = true;
                }

                logger.debug("doFilter::excludedPatterns");
                for (String pattern : excludePatterns) {
                    if (url.endsWith(pattern)) {
                        logger.debug("doFilter::excludedPatterns::MATCHED::"+pattern);
                        allowedRequest = true;
                    }
                }
                
                if (request.getParameter("action") != null &&
                        "forgotPwd".equals(request.getParameter("action"))) {
                    allowedRequest = true;
                }                

                logger.debug("doFilter::requestAllowed::"+allowedRequest);
                if (!allowedRequest) {
                    HttpSession session = req.getSession(false);
                    if (null == session || null == session.getAttribute(SystemConstants.LoggedInUser)) {
                        logger.info("doFilter::Unauthorized Request::Redirecting to login page");
                        redirect = true;
                        res.sendRedirect(req.getServletContext().getContextPath() + "/login.jsp");
                    }
                }
            }
            if (!redirect) {
                chain.doFilter(request, response);
            }
        } catch (Throwable t) {
            // If an exception is thrown somewhere down the filter chain,
            // we still want to execute our after processing, and then
            // rethrow the problem after that.
            problem = t;
            t.printStackTrace();
        }

        doAfterProcessing(request, response);

        // If there was a problem, we want to rethrow it if it is
        // a known type, otherwise log it.
        if (problem != null) {
            if (problem instanceof ServletException) {
                throw (ServletException) problem;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
        logger.debug("doFilter::END");
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    @Override
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            logger.debug("Initializing filter");
        }

        String urls = filterConfig.getInitParameter("no-validation-urls");
        StringTokenizer tokenUrls = new StringTokenizer(urls, ",");
        avoidUrlList = new ArrayList<String>();
        while (tokenUrls.hasMoreTokens()) {
            avoidUrlList.add(tokenUrls.nextToken());
        }
        logger.debug("init::avoidUrls::"+avoidUrlList.size());
        
        String patterns = filterConfig.getInitParameter("exclude-patterns");
        StringTokenizer tokenPatterns = new StringTokenizer(patterns, ",");
        excludePatterns = new ArrayList<String>();
        while (tokenPatterns.hasMoreTokens()) {
            excludePatterns.add(tokenPatterns.nextToken());
        }
        logger.debug("init::excludePatterns::"+excludePatterns.size());

        String urlPatterns = filterConfig.getInitParameter("exclude-url-patterns");
        StringTokenizer tokenUrlPatterns = new StringTokenizer(urlPatterns, ",");
        excludeUrlPatterns = new ArrayList<String>();
        while (tokenUrlPatterns.hasMoreTokens()) {
            excludeUrlPatterns.add(tokenUrlPatterns.nextToken());
        }
        logger.debug("init::excludeUrlPatterns::"+excludeUrlPatterns.size());
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("SessionFilter()");
        }
        StringBuffer sb = new StringBuffer("SessionFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

}
