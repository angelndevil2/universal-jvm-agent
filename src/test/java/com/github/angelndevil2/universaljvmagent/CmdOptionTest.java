package com.github.angelndevil2.universaljvmagent;

import org.apache.commons.cli.ParseException;
import org.junit.Test;

/**
 * @author k, Created on 16. 2. 5.
 */
public class CmdOptionTest {

    @Test
    public void printUsage() throws ParseException {
        CmdOptions cmd = new CmdOptions();

        cmd.printUsage();
    }
}
