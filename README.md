#About#
The intent of this project is to assist debugging/tracing issues by easily correlating log messages to their context and other log messages.
This is achieved via the addition of a *transaction token*, and start/finish messages.

e.g. HTTP Requests including a transaction token `C3VNUF`, a start message, and a finish message.
 > 14:14:11.505 [C3VNUF] INFO  org.tl4j.LogContext - Starting HTTP/1.1 GET @ http://localhost:8081/ from 127.0.0.1
 > 14:14:11.505 [C3VNUF] INFO  org.tl4j.LogContext - Finished with http status code 200 in 0ms

#How it works#
At the core is the `LogContext` class. This class exposes a `LogContext.start` and `LogContext.finish` methods that are
called for each incoming http request. You may also call it for other transaction types, such as:
- EJB Jobs
- Background Threads
- Message Queue handlers

#How to use it#
## Servlet Filter
1. Add maven dependency
```
    <dependency>
        <groupId>org.tl4j</groupId>
        <artifactId>tl4j-servlet</artifactId>
        <version>0.1.1</version>
    </dependency>
```

2. Add to web.xml

Add this as early in the filter list as possible.
```
       <filter-mapping>
           <filter-name>logging</filter-name>
           <url-pattern>/</url-pattern>
       </filter-mapping>
       <filter>
           <filter-name>logging</filter-name>
           <filter-class>org.tl4j.servlet.LoggingFilter</filter-class>
       </filter>
```

## Batches, Jobs, and any other transaction
Simply call `org.tl4j.LogContext.start` as early as you can,
and `org.tl4j.LogContext.finish` as late as you can. e.g.:
```
             try {
                 org.tl4j.LogContext.start("JOB", "EXECUTE", "job://check/something", "0.0.0.0");
                 String outcome = execute();
             } finally {
                 org.tl4j.LogContext.finish(outcome);
             }
```
## Log ip address
By default it will log the IP address that the client *appears* to come from.

TODO support this scenario
If you have a load balancer, then it is essential that you configure the load balancer to include the
[X-Forwarded-For|https://en.wikipedia.org/wiki/X-Forwarded-For] htttp header to log the client,
and not the load balancer IP. If you do this, add the following line to the web.xml configuration:
```
TODO
```
X-Forwarded-For may include multiple IP's, the first being the actual client but is easily spoofed and the
last being that set by your load balancer and *trusted*. All IP's are logged as they appear in the header.

## Log user and other business contexts
Not Implemented Yet, but the plan is:

Often you want to trace issues based on a business context, such as:
- The authenticated user
- The record being viewed/modified
- The parent/children transactions

Call the following anytime during the transaction
```
LogContext.appendContext("user",username);
```

See the outcome

 > 14:14:11.505 [C3VNUF] INFO  org.tl4j.LogContext - Finished with http status code 200 in 0ms [user=bob]

To minamise the number of messages written, the context will be written at the finish message,
or when resetContext is called.

## Exception View
In your exception view you may render the *transaction token* to assist your
IT department in locating the relevant log messages.

e.g.
> Sorry, something went wrong. Please try again, or if the problem persists please contact IT and quote **C3VNUF**.

# Logging Frameworks
Currently only SL4J is supported.

The intent is to support the common frameworks, sl4j, log4j, and logback. I'm tempted to do this via
reflection to avoid having a different project for each one. More research is required.

http://axelfontaine.com/blog/optional-dependencies.html

## sl4j
## log4j
## logback

# Log Viewers
To make the most of the data its best to something better than a text viewer.
## ??
## Website
## Tool
## IntelliJ

# Alternatives
I haven't seen many other projects like this, so either I'm not looking hard enough, or this is a bad idea.

# Similar
## MiniProfiler
Awesome for finding those performance bottlenecks

https://github.com/alvins82/java-mini-profiler-core
