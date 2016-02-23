package com.github.angelndevil2.universaljvmagent;

import org.junit.Test;

/**
 * @author k, Created on 16. 2. 21.
 */
public class AgentTest {

    @Test(expected=IllegalStateException.class)
    public void testFindPathJar() throws Exception {
        System.out.println(Launcher.findPathJar(this.getClass()));
    }
}