package org.tl4j;

/**
 *
 */
public class LogContext
{
    public static String CONTEXT_URL = "url";
    public static String CONTEXT_PROTOCOL = "protocol";
    public static String CONTEXT_METHOD = "method";
    public static String CONTEXT_TRANSACTION_ID = "tid";
    public static String CONTEXT_START_TICKS = "startns";
    public static String CONTEXT_ORIGIN = "origin";

    /**
     * This is currently set by the static method below.
     */
    static LogContextAdapter logContextAdapter;

    public static void start(String protocol, String method, String url, String origin) {
        logContextAdapter.start(protocol,method, url, origin);
    }

    public static void reset() {
        logContextAdapter.reset();
    }

    public static void finish(String result) {
        logContextAdapter.finish(result);
    }

    public static String getContext(String name) {
        return logContextAdapter.getContext(name);
    }

    public static void setAdapter(LogContextAdapter adapter) {
        logContextAdapter = adapter;
    }
}
