package com.github.angelndevil2.universaljvmagent.client;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IVmInfo;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author k, Created on 16. 2. 22.
 */
public class VmInfo implements IVmInfo {

    private static final long serialVersionUID = 8456561399410115516L;

    private final IVmInfo vmInfo;

    public VmInfo(IVmInfo vmInfo) {
        this.vmInfo = vmInfo;
    }

    @Override
    public List<String> getVmList() throws RemoteException {
        return vmInfo.getVmList();
    }
}
