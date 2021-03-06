package com.github.angelndevil2.universaljvmagent.server;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author k, Created on 16. 2. 21.
 */
public class VmInfo implements Serializable {

    private static final long serialVersionUID = 5436127613485375196L;

    public enum Cmd {
        VM_LIST;
    }

    public List<String> getVmList() throws RemoteException {
        try {
            ArrayList<String> list = new ArrayList<String>();
            for (VirtualMachineDescriptor d : VirtualMachine.list()) list.add(d.toString());
            return list;
        } catch (Throwable t) {
            throw new RemoteException("vm list failed.", t);
        }
    }
}
