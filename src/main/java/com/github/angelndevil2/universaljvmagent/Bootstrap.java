package com.github.angelndevil2.universaljvmagent;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * @author k, Created on 16. 2. 29.
 */
public class Bootstrap {

    /**
     * used with -javaagent:
     * @param options
     * @param instrumentation
     */
    public static void premain(String options, Instrumentation instrumentation) {

        try {

            bootStrap(options);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }

    }

    /**
     * used by dynamic attach
     * @param options
     * @param instrumentation
     */
    public static void agentmain(String options, Instrumentation instrumentation) {

        try {

            bootStrap(options);

        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }

    @SuppressWarnings("unchecked")
    private static void bootStrap(String options) throws IOException, NullPointerException {
        //
        // Bootstrap
        //

        // add jar file url
        ArrayList<URL> urls = new ArrayList<URL>();

        String jarName = findPathJar(null);
        File[] files = new File(jarName.substring(0, jarName.lastIndexOf("/"))).listFiles();
        for (File f : files) {
            urls.add(f.getAbsoluteFile().toURI().toURL());
        }

        // feed your URLs to a URLClassLoader!
        ClassLoader classloader =
                new URLClassLoader(
                        urls.toArray(new URL[1]),
                        ClassLoader.getSystemClassLoader().getParent());

        // well-behaved Java packages work relative to the
        // context classloader.  Others don't (like commons-logging)
        Thread.currentThread().setContextClassLoader(classloader);

        try {
           Class agent = Class.forName("com.github.angelndevil2.universaljvmagent.Agent", false, classloader);
            Object i = agent.newInstance();
            agent.getMethod("runAgent", String.class).invoke(i, options);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    /**
     * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
     *
     * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let Agent find its own jar.
     * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
     *                               other custom classloading device).
     */
    public static String findPathJar(Class<?> context) throws IllegalStateException {
        if (context == null) context = Bootstrap.class;
        String rawName = context.getName();
        String classFileName;
        /* rawName is something like package.name.ContainingClass$ClassName. We need to turn this into ContainingClass$ClassName.class. */
        {
            int idx = rawName.lastIndexOf('.');
            classFileName = (idx == -1 ? rawName : rawName.substring(idx+1)) + ".class";
        }

        String uri = context.getResource(classFileName).toString();
        if (uri.startsWith("file:")) throw new IllegalStateException("This class has been loaded from a directory and not from a jar file.");
        if (!uri.startsWith("jar:file:")) {
            int idx = uri.indexOf(':');
            String protocol = idx == -1 ? "(unknown)" : uri.substring(0, idx);
            throw new IllegalStateException("This class has been loaded remotely via the " + protocol +
                    " protocol. Only loading from a jar on the local file system is supported.");
        }

        int idx = uri.indexOf('!');
        //As far as I know, the if statement below can't ever trigger, so it's more of a sanity check thing.
        if (idx == -1) throw new IllegalStateException("You appear to have loaded this class from a local jar file, but I can't make sense of the URL!");

        try {
            String fileName = URLDecoder.decode(uri.substring("jar:file:".length(), idx), Charset.defaultCharset().name());
            return new File(fileName).getAbsolutePath();
        } catch (UnsupportedEncodingException e) {
            throw new InternalError("default charset doesn't exist. Your VM is borked.");
        }
    }
}
