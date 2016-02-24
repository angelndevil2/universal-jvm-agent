package com.github.angelndevil2.universaljvmagent;

import com.github.angelndevil2.universaljvmagent.jetty.JettyServer;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.client.ContentExchange;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.HttpExchange;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Slf4j
public class ServerModeTest {

    private static Properties jettyProp;
    private final static JettyServer server = new JettyServer();

    @BeforeClass
    public static void startJettyServer() {
        server.run();
    }

    @BeforeClass
    public static void initValues() {
        try {
            PropertiesUtil.setDirs("src/dist");
        } catch (IOException e) {
            log.error("base directory setting error.", e);
        }

    }

    @AfterClass
    public static void stopJettyServer() throws Exception {
        server.stop();
    }

    @Test
    public void getInfoFromEmbeddedTest() throws Exception {

        HttpClient client = new HttpClient();
        client.start();

        ContentExchange exchange = new ContentExchange(true);
        exchange.setURL("http://localhost:1080/entry-point/test");

        client.send(exchange);

        // Waits until the exchange is terminated
        int exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }

        /*exchange.reset();
        exchange.setURL("http://localhost:1080/192.168.100.242/jndi-traverse/all");

        client.send(exchange);

        // Waits until the exchange is terminated
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            //assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }*/

        exchange.reset();
        exchange.setURL("http://localhost:1080/192.168.100.242/mbeanservers/ids");

        client.send(exchange);

        // Waits until the exchange is terminated
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }

        exchange.reset();
        exchange.setURL("http://localhost:1080/192.168.100.242/mbeanservers/NSTest02_1456339127817/names");

        client.send(exchange);

        // Waits until the exchange is terminated
        exchangeState = exchange.waitForDone();

        if (exchangeState == HttpExchange.STATUS_COMPLETED) {
            assertEquals(200, exchange.getResponseStatus());
            System.out.println(exchange.getResponseContent());
        }
    }
}
