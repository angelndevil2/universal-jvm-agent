package com.github.angelndevil2.universaljvmagent;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.util.ContextInitializer;
import ch.qos.logback.core.joran.spi.JoranException;
import com.github.angelndevil2.universaljvmagent.jetty.JettyServer;
import com.github.angelndevil2.universaljvmagent.server.MBeanServerFactory;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.net.URL;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class Agent {

    private static final String START = "start";
    private static final String STOP = "stop";

    private static JettyServer server;
    @Getter
    private static final MBeanServerFactory factory = new MBeanServerFactory();

    /**
     * new JettyServer after properties set is done. so not in constructor.
     *
     * {@link JettyServer} start
     */
    public void startServer() {
        ManagementFactory.getPlatformMBeanServer();
        server = new JettyServer();
        server.run();
        log.debug("embedded server started.");
    }
    /**
     * {@link JettyServer} stop
     */
    public void stopServer() throws Exception {
        if (server == null) throw new NullPointerException("server is not running");
        server.stop();
        log.debug("embedded jetty server stopped.");
    }

    public static void reloadLogger() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        ContextInitializer ci = new ContextInitializer(loggerContext);
        URL url = ci.findURLOfDefaultConfigurationFile(true);

        try {
            JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(loggerContext);
            loggerContext.reset();
            configurator.doConfigure(url);
        } catch (JoranException je) {
            // StatusPrinter will handle this
        }
        //StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
    }

    public static void loadLogbackConfiguration() {
        System.setProperty("logback.configurationFile", PropertiesUtil.getConfDir()+File.separator+PropertiesUtil.LogbackConfig);
        reloadLogger();
    }

    public void runAgent(String options) throws IOException {

        String jarName = Bootstrap.findPathJar(Agent.class);
        PropertiesUtil.setDirs(jarName.substring(0, jarName.lastIndexOf("/"))+"/..");

        if (Boolean.valueOf(PropertiesUtil.getProperties().getProperty("logback.use"))) loadLogbackConfiguration();

        startServer();
    }
}
