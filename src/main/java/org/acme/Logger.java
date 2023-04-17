package org.acme;

import org.apache.commons.lang3.StringUtils;

public final class Logger {

    private static final org.jboss.logging.Logger LOG = org.jboss.logging.Logger.getLogger(Logger.class);

    public static void error(String errorId, String customerId, Exception e) {
        if (StringUtils.isBlank(errorId)) {
            LOG.error(shortMessage(customerId, e), e);
        } else {
            LOG.error(longMessage(errorId, customerId, e), e);
        }
    }

    public static void warn(String errorId, String customerId, Exception e) {
        if (StringUtils.isBlank(errorId)) {
            LOG.warn(shortMessage(customerId, e), e);
        } else {
            LOG.warn(longMessage(errorId, customerId, e), e);
        }
    }

    public static void error(String customerId, Exception e) {
        LOG.error(shortMessage(customerId, e), e);
    }

    public static void warn(String customerId, Exception e) {
        LOG.warn(shortMessage(customerId, e), e);
    }

    public static void error(Exception e) {
        LOG.error(e.toString(), e);
    }

    public static void error(String message) {
        LOG.error(message);
    }

    public static void warn(Exception e) {
        LOG.warn(e.toString(), e);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void info(Exception e) {
        LOG.info(e.getMessage(), e);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    private static String shortMessage(String customerId, Exception e) {
        return "customerId: " + customerId + ", " + e.toString();
    }

    private static String longMessage(String errorId, String customerId, Exception e) {
        return "errorId: " + errorId + ", customerId: " + customerId + ", " + e.toString();
    }

    private Logger() {
        throw new IllegalStateException("do not invoke");
    }
}
