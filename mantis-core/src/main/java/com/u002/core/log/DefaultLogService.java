package com.u002.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultLogService implements LogService {
    private static final Logger trace = LoggerFactory.getLogger("trace");
    private static final Logger debug = LoggerFactory.getLogger("debug");
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger warn = LoggerFactory.getLogger("warn");
    private static final Logger error = LoggerFactory.getLogger("error");
    private static final Logger access = LoggerFactory.getLogger("accessLog");
    private static final Logger serviceStats = LoggerFactory.getLogger("serviceStatsLog");
    private static final Logger profileLogger = LoggerFactory.getLogger("profile");


    public void trace(String msg) {
        trace.trace(msg);
    }

    @Override
    public void trace(String format, Object... argArray) {
        trace.trace(format, argArray);
    }

    public void debug(String msg) {
        debug.debug(msg);
    }

    public void debug(String format, Object... argArray) {
        debug.debug(format, argArray);
    }

    public void debug(String msg, Throwable t) {
        debug.debug(msg, t);
    }

    public void info(String msg) {
        info.info(msg);
    }

    public void info(String format, Object... argArray) {
        info.info(format, argArray);
    }

    public void info(String msg, Throwable t) {
        info.info(msg, t);
    }

    public void warn(String msg) {
        warn.warn(msg);
    }

    public void warn(String format, Object... argArray) {
        warn.warn(format, argArray);
    }

    public void warn(String msg, Throwable t) {
        warn.warn(msg, t);
    }

    public void error(String msg) {
        error.error(msg);
    }

    public void error(String format, Object... argArray) {
        error.error(format, argArray);
    }

    public void error(String msg, Throwable t) {
        error.error(msg, t);
    }

    public void accessLog(String msg) {
        access.info(msg);
    }

    public void accessStatsLog(String msg) {
        serviceStats.info(msg);
    }

    public void accessStatsLog(String format, Object... argArray) {
        serviceStats.info(format, argArray);
    }

    public void accessProfileLog(String format, Object... argArray) {
        profileLogger.info(format, argArray);
    }

    @Override
    public boolean isTraceEnabled() {
        return trace.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return debug.isDebugEnabled();
    }
    
    @Override
    public boolean isInfoEnabled() {
        return info.isInfoEnabled();
    }

    @Override
    public boolean isWarnEnabled() {
        return warn.isWarnEnabled();
    }

    @Override
    public boolean isErrorEnabled() {
        return error.isErrorEnabled();
    }

    @Override
    public boolean isStatsEnabled() {
        return serviceStats.isInfoEnabled();
    }

}
