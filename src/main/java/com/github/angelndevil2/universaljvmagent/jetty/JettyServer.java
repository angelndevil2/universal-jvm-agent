package com.github.angelndevil2.universaljvmagent.jetty;

import com.github.angelndevil2.universaljvmagent.servlet.*;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.GzipHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

/**
 * <ol>
 *     <li>resource handler for static content</li>
 *     <li>default handler for not found</li>
 * </ol>
  * @author k, Created on 16. 2. 18.
 * @since 0.0.3
 */
@Data
@Slf4j
public class JettyServer implements Runnable, Serializable {

    private static final long serialVersionUID = 7346771122914495703L;

    private Server server;

    public void run() {

        Properties jettyProperties = new Properties();
        String pFileName = PropertiesUtil.getJettyPropertiesFile();
        try {
            jettyProperties.load(new FileInputStream(pFileName));
        } catch (IOException e) {
            log.error("load properties from {} error.", pFileName);
            return;
        }

        // crate server
        server = new Server(Integer.parseInt(jettyProperties.getProperty("http.port")));

        ServletContextHandler servletContext = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContext.setContextPath("/");

        ServletHolder jerseyServlet = servletContext.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/*");
        jerseyServlet.setInitOrder(0);

        // Tells the Jersey Servlet which REST service/class to load.
        jerseyServlet.setInitParameter(
                "jersey.config.server.provider.classnames",
                (new StringBuilder()).append((EntryPoint.class.getCanonicalName())).
                        append(",").
                        append(JndiTraverse.class.getCanonicalName()).
                        append(",").
                        append(MBeanServers.class.getCanonicalName()).
                        append(",").
                        append(MBeanDomains.class.getCanonicalName()).
                        append(",").
                        append(MBeans.class.getCanonicalName()).
                        append(",").
                        append(MBeanInfo.class.getCanonicalName()).
                        append(",").
                        append(MBean.class.getCanonicalName()).toString()
                );

        // Add the ResourceHandler to the server.
        GzipHandler gzip = new GzipHandler();
        server.setHandler(gzip);
        gzip.setHandler(servletContext);

        try {
            server.start();
        } catch (Exception e) {
            log.error("embedded jetty server start error.", e);
        }

        //server.dumpStdErr();

        /*
        try {
            server.join();
        } catch (InterruptedException e) {
            log.info("jetty server interrupted", e);
        }*/
    }

    public void stop() throws Exception {
        if (server != null) server.stop();
    }

    public void join() throws InterruptedException {
        if (server != null) server.join();
    }
}
