package com.github.angelndevil2.universaljvmagent.util;

import lombok.Cleanup;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author k, Created on 16. 1. 30.
 */
public class PropertiesUtil {

    public static final String LogbackConfig = "logback.xml";
    public static final String AppProperties = "agent.properties";
    @Setter @Getter
    private static String baseDir;
    @Setter @Getter
    private static String confDir;
    @Setter @Getter
    private static String binDir;
    /**
     * @since 0.0.3
     */
    @Setter @Getter
    private static String libDir;
    /**
     *  * @since 0.0.3
     */
    public static final String JettyProperties = "jetty.properties";
    @Getter
    private static final Properties properties = new Properties();

    /**
     * load application's configuration
     * @throws IOException
     */
    private static void loadProperties() throws IOException {
        @Cleanup FileInputStream is = new FileInputStream(confDir + File.separator + AppProperties);

        properties.load(is);

        System.out.println("properties loaded from " +confDir + File.separator + AppProperties);
    }

    /**
     * @since 0.0.3
     * @return
     */
    public static String getJettyPropertiesFile() {
        return confDir+File.separator+JettyProperties;
    }

    /**
     * set application's proper directory from base directory
     *
     * @throws IOException
     */
    private static void setDirs() throws IOException {
        if (baseDir == null) baseDir = ".";
        confDir = baseDir+File.separator+"conf";
        binDir = baseDir+File.separator+"bin";
        libDir = baseDir+File.separator+"lib";

        System.out.println("base dir : "+baseDir);
        System.out.println("conf dir : "+confDir);
        System.out.println("bin dir : "+binDir);
        System.out.println("lib dir : "+libDir);

        loadProperties();

        setHomeDir(baseDir);
    }

    /**
     * set "universal-jvm-agent.home" system property
     *
     * @param dir
     */
    public static void setHomeDir(String dir) {
        System.setProperty("universal-jvm-agent.home", dir);

        System.out.println("system property set : universal-jvm-agent.home="+dir);
    }

    public static void setDirs(@NonNull String bd) throws IOException {
        baseDir = bd;
        setDirs();
    }

    /**
     *
     * @return id to access jndi context
     */
    public static String getJndiUserId() {
        return properties.getProperty("jndi.user.id");
    }

    /**
     *
     * @return password to access jndi context
     */
    public static String getJndiUserPassword() {
        return properties.getProperty("jndi.user.password");
    }

    /**
     * workaround for Jeus
     * @return
     */
    public static boolean isJeus() {
        return System.getProperty("jeus.home") != null;
    }

    /**
     * workaround for webLogic
     */
    public static boolean isWebLogic() {
        return System.getProperty("weblogic.Name") != null;
    }
}
