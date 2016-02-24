package com.github.angelndevil2.universaljvmagent.rmiobjects;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.QueryExp;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public interface IMBeanServerFactory extends IRmiObject {

    /**
     * @see java.lang.management.ManagementFactory
     * @return mbean server id array
     * @throws RemoteException
     */
    ArrayList<String> getAllMBeanServerId() throws RemoteException;

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @param queryExp
     * @return object name set
     * @throws RemoteException
     */
    Set<ObjectName> queryNames(final String serverId, ObjectName name, QueryExp queryExp) throws RemoteException;

    /**
     * Get all domains from mbean server whose id is serverId
     * @param serverId mbean server id
     * @return domain string array
     * @throws RemoteException
     */
    String[] getDomains(String serverId) throws RemoteException;

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @return
     * @throws RemoteException
     */
    MBeanInfo getMBeanInfo(String serverId, ObjectName name) throws RemoteException;
}
