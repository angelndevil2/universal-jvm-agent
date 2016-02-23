package com.github.angelndevil2.universaljvmagent.rmiobjects;

import java.rmi.RemoteException;
import java.util.List;

/**
 * @author k, Created on 16. 2. 22.
 */
public interface IVmInfo extends IRmiObject {
    List<String> getVmList() throws RemoteException;
}
