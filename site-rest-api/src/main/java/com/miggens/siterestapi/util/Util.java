package com.miggens.siterestapi.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util {

    private static Logger LOG = LoggerFactory.getLogger(Util.class);

    public static String getHostname() {
/*
        try {
            return InetAddress.getLoopbackAddress().getHostName();
        }
        catch (UnknownHostException e) {

            LOG.error("Unknown Host Exception: "+ e.getLocalizedMessage() );

            return "ERROR: unknown host";
        }
 */
        return InetAddress.getLoopbackAddress().getHostName();
    }
}
