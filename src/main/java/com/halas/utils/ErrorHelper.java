package com.halas.utils;

import org.apache.logging.log4j.Logger;

public class ErrorHelper {
    private ErrorHelper() {
    }

    public static void outputError(final Logger LOG, Exception e) {
        LOG.error("Class: " + e.getClass());
        LOG.error("Message: " + e.getMessage());
        LOG.error(e.getStackTrace());
    }
}
