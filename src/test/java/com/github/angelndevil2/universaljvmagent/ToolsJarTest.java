package com.github.angelndevil2.universaljvmagent;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * @author k, Created on 16. 2. 21.
 */
public class ToolsJarTest {
    @Test
    public void JVMInstancesTest() {
        assertTrue(VirtualMachine.list().size() > 0);

        for (VirtualMachineDescriptor vd : VirtualMachine.list()) {
            System.out.println(vd.toString());
        }
    }
}
