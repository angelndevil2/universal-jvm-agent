package com.github.angelndevil2.universaljvmagent.server;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IMBeanServer;

import javax.management.*;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public class MBeanServer implements IMBeanServer {

    private static final long serialVersionUID = -680104697021234725L;

    private transient WeakReference<javax.management.MBeanServer> server;
    private final String serverId;

    public MBeanServer(javax.management.MBeanServer server) {
        this.server = new WeakReference<javax.management.MBeanServer>(server);
        serverId = MBeanServerFactory.getMBeanServerId(server);

    }

    @Override
    public Set<ObjectName> queryNames(final ObjectName name, final QueryExp queryExp) throws RemoteException {
        javax.management.MBeanServer server;
        try {
            server = getMBeanServer();
        } catch (NullPointerException e) {
            throw new RemoteException(e.getMessage());
        }
        return server.queryNames(name, queryExp);
    }

    /**
     * @return domain string array
     * @throws RemoteException
     */
    @Override
    public String[] getDomains() throws RemoteException {
        javax.management.MBeanServer server;
        try {
            server = getMBeanServer();
        } catch (NullPointerException e) {
            throw new RemoteException(e.getMessage());
        }

        return server.getDomains();
    }

    public MBeanInfo getMBeanInfo(ObjectName name) throws RemoteException {
        javax.management.MBeanServer server;
        try {
            server = getMBeanServer();
        } catch (NullPointerException e) {
            throw new RemoteException(e.getMessage());
        }

        try {
            return server.getMBeanInfo(name);
        } catch (Exception e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    private javax.management.MBeanServer getMBeanServer() throws NullPointerException {
        javax.management.MBeanServer server = this.server.get();
        if (server == null)
            return reStoreMBeanServer();
        return server;
    }

    private javax.management.MBeanServer reStoreMBeanServer() throws NullPointerException {
        ArrayList< javax.management.MBeanServer> list = MBeanServerFactory.findMBeanServer(serverId);
        if (list == null || list.size() < 1) throw new NullPointerException("mbean server is not exist.");

        server = new WeakReference<javax.management.MBeanServer>(list.get(0));
        return list.get(0);
    }
}
