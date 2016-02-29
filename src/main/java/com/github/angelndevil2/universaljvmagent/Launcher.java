package com.github.angelndevil2.universaljvmagent;

import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class Launcher {

    private static String vmArgs;

    /**
     * If the provided class has been loaded from a jar file that is on the local file system, will find the absolute path to that jar file.
     *
     * @param context The jar file that contained the class file that represents this class will be found. Specify {@code null} to let Agent find its own jar.
     * @throws IllegalStateException If the specified class was loaded from a directory or in some other way (such as via HTTP, from a database, or some
     *                               other custom classloading device).
     */
    public static String findPathJar(Class<?> context) throws IllegalStateException {
        if (context == null) context = Agent.class;
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

    /**
     * for command line to attach this agent.
     *
     * @param args command line arguments
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws IOException, ParseException {

        CmdOptions options = new CmdOptions();

        options.setArgs(args);

        CommandLine cmd = options.getCmd();
        if (cmd.hasOption('h')) {
            options.printUsage();
            return;
        }

        if (cmd.hasOption("d")) {
            try {

                PropertiesUtil.setDirs(cmd.getOptionValue("d").trim());

                vmArgs = cmd.getOptionValue("d").trim();

            } catch (IOException e) {

                System.err.println(PropertiesUtil.getConfDir() + File.separator + PropertiesUtil.AppProperties + " not found. may use -d option" + e);
                return;
            }
        }

        if (cmd.hasOption('p')) {

            String pid = cmd.getOptionValue('p');
            if (pid == null) throw new NullPointerException("pid is null");

                try {
                    Class vmClass = Class.forName("com.sun.tools.attach.VirtualMachine");
                    Object virtualMachine = vmClass.getMethod("attach", String.class).invoke(null, pid);
                    String jarName = findPathJar(null);
                    System.out.println(jarName+" will be loaded with args "+vmArgs);
                    virtualMachine.getClass().getMethod("loadAgent", String.class, String.class).invoke(virtualMachine, jarName, vmArgs);
                    log.debug(jarName + " registered.");
                    virtualMachine.getClass().getMethod("detach").invoke(virtualMachine);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }

        } else {
            System.out.println("d or p options is required");
            options.printUsage();
        }
    }
}
