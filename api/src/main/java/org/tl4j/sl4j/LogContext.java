package org.tl4j.sl4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tl4j.HumanToken;
import org.tl4j.LogContextAdapter;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: Iain
 * Date: 23/12/14
 * Time: 11:43 AM
 * To change this template use File | Settings | File Templates.
 */
public class LogContext implements LogContextAdapter {

    private Logger logger = LoggerFactory.getLogger(org.tl4j.LogContext.class);

    @Override
    public void start(String protocol, String method, String url, String origin) {

        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_METHOD,method);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_PROTOCOL,protocol);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_TRANSACTION_ID, HumanToken.next());
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_URL,url);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_START_TICKS,System.nanoTime()+"");
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_ORIGIN,origin);

        String logMsg = "Starting " + protocol + " " + method + " @ " +  url + " from " + origin;
        logger.info(logMsg);

    }

    @Override
    public void reset() {
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_METHOD, null);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_PROTOCOL, null);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_TRANSACTION_ID, HumanToken.next());
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_URL, null);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_START_TICKS, null);
        org.slf4j.MDC.put(org.tl4j.LogContext.CONTEXT_ORIGIN, null);
    }

    @Override
    public void finish(String result) {
        long finish = System.nanoTime();
        String start =  org.slf4j.MDC.get(org.tl4j.LogContext.CONTEXT_START_TICKS);
        String logMsg = "Finished " + result;
        if (start != null) {
            long startTicks = Long.parseLong(start);
            long durationNs = finish - startTicks;
            long durationInMs = TimeUnit.MILLISECONDS.convert(durationNs, TimeUnit.NANOSECONDS);
            DecimalFormat df2 = new DecimalFormat( "#,###,###,##0" );
            logMsg += " in " + df2.format(durationInMs) + "ms";
        }
        logger.info(logMsg);
    }

    @Override
    public String getContext(String name) {
        return org.slf4j.MDC.get(name);
    }
}
