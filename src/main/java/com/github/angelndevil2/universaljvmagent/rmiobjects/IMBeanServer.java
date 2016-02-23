package com.github.angelndevil2.universaljvmagent.rmiobjects;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.QueryExp;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public interface IMBeanServer extends IRmiObject {

    /**
     * @param name object name
     * @param queryExp
     * @return object name set
     * @throws RemoteException
     */
    Set<ObjectName> queryNames(final ObjectName name, final QueryExp queryExp) throws RemoteException;
    /**
     * @return domain string array
     * @throws RemoteException
     */
    String[] getDomains() throws RemoteException;

    MBeanInfo getMBeanInfo(ObjectName name) throws RemoteException;
}
