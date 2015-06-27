package org.tl4j.servlet;

import org.tl4j.LogContext;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Put this filter as early in the chain as possible.
 *
 *
 * Configuration:
 * - ignoreUrls: Logging every load of a static resource can be distracting. Use this to exclude certain urls. This is a regular expression
 * -- Default: \.(jpg,png,js,css)
 * -- TODO: Switch to ant style matching
 * - tokenSize: Default size is 10, representing 1Bil. 50% change of a collision after
 * -- Default: 10
 */
public class LoggingFilter implements Filter
{

    static {
        LogContext.setAdapter(new org.tl4j.sl4j.LogContext());
    }

    private static String IGNORE_URLS = "\\.(jpg,png,js,css)";

    public void init(FilterConfig config) throws ServletException {
        String ignoreUrls = config.getInitParameter("ignoreUrls");
        if (ignoreUrls != null) {
            IGNORE_URLS = ignoreUrls;
        }
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

         org.tl4j.LogContext.reset();

         String protocol = servletRequest.getProtocol();
         String method = "UNKNOWN";
         String url = "UNKNOWN";
         String ip = servletRequest.getRemoteAddr();
         if (servletRequest instanceof HttpServletRequest) {
             HttpServletRequest http = (HttpServletRequest) servletRequest;
             method = http.getMethod();
             url = http.getRequestURL().toString();
         }
         boolean ignore = url.matches(IGNORE_URLS);
         if (ignore) {
             filterChain.doFilter(servletRequest, servletResponse);
         } else {
             try {
                 org.tl4j.LogContext.start(protocol,method,url, ip);
                 filterChain.doFilter(servletRequest, servletResponse);
             } finally {
                 String result = "";
                 if (servletResponse instanceof HttpServletResponse) {
                     HttpServletResponse http = (HttpServletResponse) servletResponse;
                     result += "with http status code "+http.getStatus();
                 }
                 org.tl4j.LogContext.finish(result);
             }
         }
    }

    public void destroy() {
    }
}
