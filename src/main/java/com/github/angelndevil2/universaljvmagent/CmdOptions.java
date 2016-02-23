package com.github.angelndevil2.universaljvmagent;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.cli.*;

/**
 * @author k, Created on 16. 2. 21.
 */
@Setter @Getter
public class CmdOptions {

    private final Options options = new Options();
    private final CommandLineParser parser = new DefaultParser();
    @Setter(AccessLevel.NONE)
    private CommandLine cmd;
    private String[] args;

    public CmdOptions() {

        options.addOption("h", "help", false, "print this message");
        options.addOption("c", "client", false, "run in client mode");
        options.addOption(
                Option.builder("p").
                        argName("pid").
                        longOpt("pid").
                        numberOfArgs(1).
                        desc("Running JVM pid which interested in.").build());
        options.addOption(
                Option.builder("host").
                        argName("host").
                        numberOfArgs(1).
                        desc("host address to connect.").build());
        options.addOption(
                Option.builder("d").
                        argName("dir").
                        numberOfArgs(1).
                        desc("set base directory.").build());
    }

    /**
     * set commaond line arguments and parse
     *
     * @param args
     * @throws ParseException
     */
    public void setArgs(String[] args) throws ParseException {
        this.args = args;
        cmd = parser.parse(options, args);
    }

    /**
     * print usage and help information
     */
    public void printUsage() {
        // automatically generate the help statement
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("universal-jvm-agent", options, true);
    }
}
