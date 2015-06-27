package org.tl4j;

/**
 *
 */
public interface LogContextAdapter {
    void start(String protocol, String method, String url, String origin);
    void reset();
    void finish(String result);
    String getContext(String name);
}
