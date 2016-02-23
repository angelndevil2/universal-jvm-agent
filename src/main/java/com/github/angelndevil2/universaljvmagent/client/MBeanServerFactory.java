package com.github.angelndevil2.universaljvmagent.client;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IMBeanServerFactory;

import javax.management.MBeanInfo;
import javax.management.ObjectName;
import javax.management.QueryExp;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public class MBeanServerFactory implements IMBeanServerFactory {

    private static final long serialVersionUID = 2723964162907153738L;

    public final IMBeanServerFactory factory;

    public MBeanServerFactory(IMBeanServerFactory factory) {
        this.factory = factory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayList<String> getAllMBeanServerId() throws RemoteException {
        return factory.getAllMBeanServerId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ObjectName> queryNames(String serverId, ObjectName name, QueryExp queryExp) throws RemoteException {
        return factory.queryNames(serverId, name, queryExp);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getDomains(String serverId) throws RemoteException {
        return factory.getDomains(serverId);
    }

    @Override
    public MBeanInfo getMBeanInfo(String serverId, ObjectName name) throws RemoteException {
        return factory.getMBeanInfo(serverId, name);
    }

}
