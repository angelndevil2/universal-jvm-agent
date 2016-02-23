package com.github.angelndevil2.universaljvmagent.rmiobjects;

import com.google.common.collect.ArrayListMultimap;

import javax.naming.Context;
import java.rmi.RemoteException;

/**
 * @author k, Created on 16. 2. 23.
 */
public interface IJndiContextTraverser extends IRmiObject {

    /**
     * set context with {@link javax.naming.InitialContext}
     */
    void setContext() throws RemoteException;

    /**
     *
     * @param ctx
     */
    void setContext(final Context ctx) throws RemoteException;
    /**
     * start travers from name
     *
     * @param name jndi name
     * @throws RemoteException
     */
    ArrayListMultimap traverse(final String name) throws RemoteException;
    /**
     * start travers from root
     *
     * @throws RemoteException
     */
    ArrayListMultimap traverse() throws RemoteException;

    /**
     * set Context.SECURITY_PRINCIPAL, Context.SECURITY_CREDENTIALS from remote values
     */
    void setSecurityValues(final String id, final String password) throws RemoteException;
}
