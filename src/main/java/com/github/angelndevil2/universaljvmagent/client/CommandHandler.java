package com.github.angelndevil2.universaljvmagent.client;

import com.github.angelndevil2.universaljvmagent.rmiobjects.*;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import com.google.common.collect.ArrayListMultimap;
import lombok.extern.slf4j.Slf4j;

import javax.management.MBeanInfo;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.naming.NameClassPair;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;

/**
 * @author k, Created on 16. 1. 30.
 */
@Slf4j
public class CommandHandler {

    private final HashMap<String,IRmiObject> rmiObjectHashMap = new HashMap<String,IRmiObject>();

    /**
     * @since 0.0.3
     * @param host
     * @param port
     * @throws RemoteException
     * @throws NotBoundException
     */
    public CommandHandler(final String host, int port) throws RemoteException, NotBoundException {
        Registry registry = LocateRegistry.getRegistry(host, port);

        rmiObjectHashMap.put(RmiObjectNames.VM_INFO, new VmInfo ((IVmInfo) registry.lookup(RmiObjectNames.VM_INFO)));
        rmiObjectHashMap.put(RmiObjectNames.MBEAN_FACTORY,
                new MBeanServerFactory ((IMBeanServerFactory) registry.lookup(RmiObjectNames.MBEAN_FACTORY)));

        rmiObjectHashMap.put(RmiObjectNames.CONTEXT_TRAVERSER,
                new JndiContextTraverser ((IJndiContextTraverser) registry.lookup(RmiObjectNames.CONTEXT_TRAVERSER)));
    }

    public CommandHandler(final String host) throws RemoteException, NotBoundException {
        this(host, PropertiesUtil.getRimServerPort());
    }

    public void printVmList() throws RemoteException {
        IVmInfo object = (IVmInfo) rmiObjectHashMap.get(RmiObjectNames.VM_INFO);
        for (String vm : object.getVmList()) {
            System.out.println(vm);
        }
    }
    /**
     * @see java.lang.management.ManagementFactory
     * @return mbean server id array
     * @throws RemoteException
     */
    public ArrayList<String> getAllMBeanServerId() throws RemoteException {
        IMBeanServerFactory factory = (IMBeanServerFactory)rmiObjectHashMap.get(RmiObjectNames.MBEAN_FACTORY);
        return factory.getAllMBeanServerId();
    }
    /**
     * @param serverId mbean server id
     * @return object name set
     * @throws RemoteException
     */
    public Set<ObjectName> getAllMBeanNames(String serverId) throws RemoteException {
        IMBeanServerFactory factory = (IMBeanServerFactory)rmiObjectHashMap.get(RmiObjectNames.MBEAN_FACTORY);
        return factory.queryNames(serverId, null, null);
    }
    /**
     * Get all domains from mbean server whose id is serverId
     * @param serverId mbean server id
     * @return domain string array
     * @throws RemoteException
     */
    public String[] getDomains(String serverId) throws RemoteException {
        IMBeanServerFactory factory = (IMBeanServerFactory)rmiObjectHashMap.get(RmiObjectNames.MBEAN_FACTORY);
        return factory.getDomains(serverId);
    }

    /**
     *
     * @param serverId mbean server id
     * @param name object name
     * @return MBeanInfo
     * @throws RemoteException
     */
    public MBeanInfo getMBeanInfo(String serverId, ObjectName name) throws RemoteException {
        IMBeanServerFactory factory = (IMBeanServerFactory)rmiObjectHashMap.get(RmiObjectNames.MBEAN_FACTORY);
        return factory.getMBeanInfo(serverId, name);
    }

    /**
     *
     * @return initial jndi context traversing result
     * @throws RemoteException
     */
    @SuppressWarnings("unchecked")
    public ArrayListMultimap getJndiContext() throws RemoteException {
        IJndiContextTraverser traverser = (IJndiContextTraverser)rmiObjectHashMap.get(RmiObjectNames.CONTEXT_TRAVERSER);
        traverser.setSecurityValues(PropertiesUtil.getJndiUserId(), PropertiesUtil.getJndiUserPassword());
        traverser.setContext();
        return traverser.traverse();
    }

    /**
     *
     * @param name root name
     * @return initial jndi context traversing result, root from name
     * @throws RemoteException
     */
    @SuppressWarnings("unchecked")
    public ArrayListMultimap getJndiContext(String name) throws RemoteException {
        IJndiContextTraverser traverser = (IJndiContextTraverser)rmiObjectHashMap.get(RmiObjectNames.CONTEXT_TRAVERSER);
        traverser.setSecurityValues(PropertiesUtil.getJndiUserId(), PropertiesUtil.getJndiUserPassword());
        traverser.setContext();
        return traverser.traverse(name);
    }

    /**
     * @since 0.0.3
     * @param on object name
     * @return key property list of object name
     */
    public static Hashtable<String, String> getMBeanKeyPropList(ObjectName on) {
        return on.getKeyPropertyList();
    }

    /**
     * @since 0.0.3
     * @param on object name
     * @return key property list of object name
     */
    public static Hashtable<String, String> getMBeanKeyPropList(String on) throws MalformedObjectNameException {
        return getMBeanKeyPropList(new ObjectName(on));
    }


    public void printAllMBeanServerIds() throws RemoteException {
        for (String id : getAllMBeanServerId()) {
            System.out.println(id);
        }
    }

    public void printAllMBeanNames(String serverId) throws RemoteException {
        for (ObjectName on : getAllMBeanNames(serverId)) {
            System.out.println(on.toString());
        }
    }

    public void printDomains(String serverId) throws RemoteException {
        for (String dm : getDomains(serverId)) {
            System.out.println(dm);
        }
    }

    public void printAllMBeanInfo(String serverId) throws RemoteException {
        for (ObjectName on : getAllMBeanNames(serverId)) {
            System.out.println(getMBeanInfo(serverId, on));
        }
    }

    @SuppressWarnings("unchecked")
    public void printJndiContext() throws RemoteException {
        IJndiContextTraverser traverser = (IJndiContextTraverser)rmiObjectHashMap.get(RmiObjectNames.CONTEXT_TRAVERSER);
        traverser.setSecurityValues(PropertiesUtil.getJndiUserId(), PropertiesUtil.getJndiUserPassword());
        traverser.setContext();
        ArrayListMultimap alm = traverser.traverse();

        printArrayListMultiMap(alm, "-->");
    }

    @SuppressWarnings("unchecked")
    private void printArrayListMultiMap(ArrayListMultimap alm, String indent) {

        for (NameClassPair key : (Set<NameClassPair>) alm.keySet()) {
            System.out.println(indent+key);
            for (Object m : (alm.get(key))) {
                printArrayListMultiMap((ArrayListMultimap) m, "--"+indent);
            }
        }
    }


}
