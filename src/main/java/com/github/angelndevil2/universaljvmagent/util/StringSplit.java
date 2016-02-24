package com.github.angelndevil2.universaljvmagent.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 25.
 */
public class StringSplit {

    @AllArgsConstructor
    @NoArgsConstructor
    public static class AddrAndPort{
        @Getter @Setter
        private String addr;

        @Getter @Setter
        private int port;

    }

    /**
     *
     * @param host host string for parsing with ':'
     * @param defaultPort default port if not exists in host string
     * @return
     */
    public static AddrAndPort getAddrAndPort(final String host, final int defaultPort) {

        String addr;
        int port;
        int pos = host.indexOf(":");
        if (pos > 0) {
            addr = host.substring(0, pos);
            port = Integer.valueOf(host.substring(pos+1));
        } else {
            addr = host;
            port = defaultPort;
        }

        return new AddrAndPort(addr, port);
    }
}
