package org.tl4j.servlet;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class LoggingFilter implements Filter
{

    private static String IGNORE_URLS = "\\.(jpg,png,js,css)";

    @Override
    public void init(FilterConfig config) throws ServletException {
        String ignoreUrls = config.getInitParameter("ignoreUrls");
        if (ignoreUrls != null) {
            IGNORE_URLS = ignoreUrls;
        }
    }

    @Override
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

    @Override
    public void destroy() {
    }
}
