package com.github.angelndevil2.universaljvmagent.server;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IMBeanServerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import javax.management.MBeanServer;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * @author k, Created on 16. 2. 22.
 */
@Slf4j
public class MBeanServerFactory implements IMBeanServerFactory {

    private static final long serialVersionUID = -2881069740095622627L;

    private transient final HashMap<String, com.github.angelndevil2.universaljvmagent.server.MBeanServer> servers
            = new HashMap<String, com.github.angelndevil2.universaljvmagent.server.MBeanServer>();

    public MBeanServerFactory() {
        for (MBeanServer ms : findMBeanServer(null)) {
            servers.put(getMBeanServerId(ms),
                    new com.github.angelndevil2.universaljvmagent.server.MBeanServer(ms));
        }
        if (servers.size() == 0) log.info("no mbean server exist. 'com.sun.management.jmxremote' property will be help.");
    }
    /**
     *
     * @param agentId
     * @return
     * @throws RemoteException
     */
    public static ArrayList<MBeanServer> findMBeanServer(final String agentId) {
        return javax.management.MBeanServerFactory.findMBeanServer(agentId);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public ArrayList<String> getAllMBeanServerId() throws RemoteException {
        ArrayList<String> ids = new ArrayList<String>();
        for (MBeanServer ms : findMBeanServer(null)) {
            ids.add(getMBeanServerId(ms));
        }

        return ids;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ObjectName> queryNames(final String serverId, ObjectName name, QueryExp queryExp) throws RemoteException {

        try {
            return getMBeanServer(serverId).queryNames(name, queryExp);
        } catch (InstanceNotFoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String[] getDomains(String serverId) throws RemoteException {
        try {
            return getMBeanServer(serverId).getDomains();
        } catch (InstanceNotFoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    @Override
    public MBeanInfo getMBeanInfo(String serverId, ObjectName name) throws RemoteException {
        try {
            return getMBeanServer(serverId).getMBeanInfo(name);
        } catch (InstanceNotFoundException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * Get the MBeanServerId of Agent ID for the provided MBeanServer.
     *
     * @param aMBeanServer MBeanServer whose Server ID/Agent ID is desired.
     * @return MBeanServerId/Agent ID of provided MBeanServer.
     */
    public static String getMBeanServerId(final MBeanServer aMBeanServer)
    {
        String serverId = null;
        final String SERVER_DELEGATE = "JMImplementation:type=MBeanServerDelegate";
        final String MBEAN_SERVER_ID_KEY = "MBeanServerId";
        try
        {
            ObjectName delegateObjName = new ObjectName(SERVER_DELEGATE);
            serverId = (String) aMBeanServer.getAttribute( delegateObjName,
                    MBEAN_SERVER_ID_KEY );
        }
        catch (MalformedObjectNameException malformedObjectNameException)
        {
            log.error(  "Problems constructing MBean ObjectName: {}",
                    malformedObjectNameException.getMessage() );
        }
        catch (AttributeNotFoundException noMatchingAttrException)
        {
            log.error(  "Unable to find attribute {} in MBean {} : {}",MBEAN_SERVER_ID_KEY,
                    SERVER_DELEGATE, noMatchingAttrException );
        }
        catch (MBeanException mBeanException)
        {
            log.error(  "Exception thrown by MBean's ({}'s {}) getter: {}",SERVER_DELEGATE
                    ,MBEAN_SERVER_ID_KEY, mBeanException.getMessage() );
        }
        catch (ReflectionException reflectionException)
        {
            log.error(  "Exception thrown by MBean's ({}'s ) setter: {}", SERVER_DELEGATE
                    ,MBEAN_SERVER_ID_KEY, reflectionException.getMessage() );
        }
        catch (InstanceNotFoundException noMBeanInstance)
        {
            log.error(  "No instance of MBean {} found in MBeanServer: {}", SERVER_DELEGATE
                    ,noMBeanInstance.getMessage() );
        }
        return serverId;
    }

    private com.github.angelndevil2.universaljvmagent.server.MBeanServer
    getMBeanServer(String serverId) throws InstanceNotFoundException {
        com.github.angelndevil2.universaljvmagent.server.MBeanServer server = servers.get(serverId);
        if (server == null) {
            ArrayList<MBeanServer> list = MBeanServerFactory.findMBeanServer(serverId);
            if (list == null || list.size() < 1) {
                servers.remove(serverId);
                throw new InstanceNotFoundException("mbean server(" + serverId + ") is not exist.");
            }
            if (list.size() > 1) {
                log.debug("{} has more than one mbean server. is it possible?", serverId);
            }
            server = new com.github.angelndevil2.universaljvmagent.server.MBeanServer(list.get(0));
            servers.put(serverId, server);
        }
        return server;
    }
}
