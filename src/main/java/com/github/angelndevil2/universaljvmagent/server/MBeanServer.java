package com.github.angelndevil2.universaljvmagent.server;

import javax.management.*;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
public class MBeanServer implements Serializable {

    private static final long serialVersionUID = -680104697021234725L;

    private transient WeakReference<javax.management.MBeanServer> server;
    private final String serverId;

    public MBeanServer(javax.management.MBeanServer server) {
        this.server = new WeakReference<javax.management.MBeanServer>(server);
        serverId = MBeanServerFactory.getMBeanServerId(server);

    }

    /**
     * @param name object name
     * @param queryExp
     * @return object name set
     * @throws RemoteException
     */
    public Set<ObjectName> queryNames(final ObjectName name, final QueryExp queryExp) {
        return getMBeanServer().queryNames(name, queryExp);
    }

    /**
     * @return domain string array
     * @throws RemoteException
     */
    public String[] getDomains() {
        return getMBeanServer().getDomains();
    }

    public MBeanInfo getMBeanInfo(final ObjectName name)
            throws IntrospectionException, InstanceNotFoundException, ReflectionException {
        return getMBeanServer().getMBeanInfo(name);
    }

    public Object getAttribute(final ObjectName oname, final String name)
            throws AttributeNotFoundException, MBeanException, ReflectionException, InstanceNotFoundException {
        return getMBeanServer().getAttribute(oname, name);
    }

    /**
     * @since 0.0.3
     * @return default domain string
     */
    public String getDefaultDomain() {
        return getMBeanServer().getDefaultDomain();
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
