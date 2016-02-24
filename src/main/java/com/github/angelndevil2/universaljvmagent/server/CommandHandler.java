package com.github.angelndevil2.universaljvmagent.server;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IRmiObject;
import com.github.angelndevil2.universaljvmagent.rmiobjects.RmiObjectNames;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;

/**
 * @author k, Created on 16. 2. 21.
 */
@Slf4j
public class CommandHandler implements Runnable {

    @Getter
    private Thread thread;
    private int port;

    private final HashMap<String,IRmiObject> rmiObjectHashMap = new HashMap<String,IRmiObject>();

    public CommandHandler() {
        try {
            port = PropertiesUtil.getRimServerPort();
        } catch (Throwable t) {
            log.warn("rmi.server.port property is malformed");
            port = 1099;
        }

        rmiObjectHashMap.put(RmiObjectNames.VM_INFO, new VmInfo());
        rmiObjectHashMap.put(RmiObjectNames.MBEAN_FACTORY, new MBeanServerFactory());
        rmiObjectHashMap.put(RmiObjectNames.CONTEXT_TRAVERSER, new JndiContextTraverser());
    }

    /**
     *
     * @param port rmi port to use
     */
    public CommandHandler(int port) {
        this.port = port;
    }

    @Override
    public void run() {

        try {
            startRmiServer();
        } catch (RemoteException e) {
            log.error("rmi server start failed.", e);
            return;
        }
    }

    public void start() {
        thread = new Thread(this);
        thread.setDaemon(true);
        thread.start();
    }

    /**
     *
     * @throws RemoteException
     */
    public void startRmiServer() throws RemoteException {

        try {

            InetAddress localHost=null;
            // Bug 47980 - allow override of local hostname
            String host = System.getProperty("java.rmi.server.hostname"); // $NON-NLS-1$
            try {
                if( host==null ) {
                    log.info("System property 'java.rmi.server.hostname' is not defined, using localHost address");
                    localHost = InetAddress.getLocalHost();
                } else {
                    log.info("Resolving by name the value of System property 'java.rmi.server.hostname':"+host);
                    localHost = InetAddress.getByName(host);
                }
            } catch (UnknownHostException e1) {
                throw new RemoteException("Cannot start. Unable to get local host IP address.", e1);
            }
            log.info("Local IP address="+localHost.getHostAddress());
            // BUG 52469 : Allow loopback address for SSH Tunneling of RMI traffic
            if (host == null && localHost.isLoopbackAddress()){
                String hostName = localHost.getHostName();
                throw new RemoteException("Cannot start. "+hostName+" is a loopback address.");
            }
            if (localHost.isSiteLocalAddress()){
                // should perhaps be log.warn, but this causes the client-server test to fail
                log.info("IP address is a site-local address; this may cause problems with remote access.\n"
                        + "\tCan be overridden by defining the system property 'java.rmi.server.hostname'");
            }

            try {
                LocateRegistry.createRegistry(port);
            } catch (RemoteException e){
                String msg="Problem creating registry: "+e;
                log.warn(msg);
                System.err.println(msg);
                System.err.println("Continuing...");
            }

            // bind all rmi object
            bindAll();
            System.out.println("rmi server(" +localHost.getHostAddress()+") started.");
        } catch (Exception ex) {
            // Throw an Exception to ensure caller knows ...
            throw new RemoteException("Cannot start. ", ex);
        }
    }

    private void bindAll() throws AlreadyBoundException, RemoteException {
        for (String name: rmiObjectHashMap.keySet()) {
            // export to java runtime
            IRmiObject object = (IRmiObject) UnicastRemoteObject.exportObject(rmiObjectHashMap.get(name), 0);
            // bind the remote object in the registry
            Registry registry = LocateRegistry.getRegistry(port); // use 1099
            registry.bind(name, object);
        }
    }
}
