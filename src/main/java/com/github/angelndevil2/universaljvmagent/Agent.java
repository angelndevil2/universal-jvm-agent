package com.github.angelndevil2.universaljvmagent;

import com.github.angelndevil2.universaljvmagent.jetty.JettyServer;
import com.github.angelndevil2.universaljvmagent.server.MBeanServerFactory;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class Agent {

    private static JettyServer server;
    @Getter
    private static final MBeanServerFactory factory = new MBeanServerFactory();

    /**
     * used with -javaagent:
     * @param options
     * @param instrumentation
     */
    public static void premain(String options, Instrumentation instrumentation) {
        handleOptions(options);
    }

    /**
     * used by dynamic attach
     * @param options
     * @param instrumentation
     */
    public static void agentmain(String options, Instrumentation instrumentation) {
        handleOptions(options);
    }

    /**
     * new JettyServer after properties set is done. so not in constructor.
     *
     * {@link JettyServer} start
     */
    public static void startServer() {
        server = new JettyServer();
        server.run();
        log.debug("embedded server started.");
    }
    /**
     * {@link JettyServer} stop
     */
    public static void stopServer() throws Exception {
        checkArgument(server != null);
        server.stop();
        log.debug("embedded jetty server stopped.");
    }

    private static void handleOptions(final String opt) {
        log.debug("agent options = {}", opt);
        System.err.println("agent options = " +opt);
        if ("stop".equals(opt)) {
            try {
                stopServer();
            } catch (Exception e) {
                log.error("embedded jetty server stop error.",e);
            }
        } else {
            if (opt != null) try {
                PropertiesUtil.setDirs(opt);
            } catch (IOException e) {
                System.err.println(e.toString());
            }
            startServer();
        }
    }
}
