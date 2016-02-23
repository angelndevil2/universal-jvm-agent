package com.github.angelndevil2.universaljvmagent;

import com.github.angelndevil2.universaljvmagent.server.CommandHandler;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.instrument.Instrumentation;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class Agent {

    private static final CommandHandler ch = new CommandHandler();

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
     * {@link CommandHandler} start
     */
    public static void startCommandHandler() {
        ch.start();
        log.debug("command handler started.");
    }
    /**
     * {@link CommandHandler} stop
     */
    public static void stopCommandHandler() {
        ch.getThread().interrupt();
        log.debug("command handler stopped.");
    }

    private static void handleOptions(String opt) {
        log.debug("agent options = {}", opt);
        System.err.println("agent options = " +opt);
        if ("stop".equals(opt)) stopCommandHandler();
        else {
            if (opt != null) try {
                PropertiesUtil.setDirs(opt);
            } catch (IOException e) {
                System.err.println(e.toString());
            }
            startCommandHandler();
        }
    }
}
