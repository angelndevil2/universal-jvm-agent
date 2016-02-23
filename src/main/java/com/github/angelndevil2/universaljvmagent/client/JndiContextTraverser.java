package com.github.angelndevil2.universaljvmagent.client;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IJndiContextTraverser;
import com.google.common.collect.ArrayListMultimap;

import javax.naming.Context;
import java.rmi.RemoteException;

/**
 * @author k, Created on 16. 2. 23.
 */
public class JndiContextTraverser implements IJndiContextTraverser {

    private static final long serialVersionUID = 8964686371368881818L;

    public IJndiContextTraverser traverser;

    public JndiContextTraverser(IJndiContextTraverser traverser) {
        this.traverser = traverser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext() throws RemoteException {
        traverser.setContext();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(final Context ctx) throws RemoteException {
        traverser.setContext(ctx);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayListMultimap traverse(final String name) throws RemoteException {
        return traverser.traverse(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayListMultimap traverse() throws RemoteException {
        return traverser.traverse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSecurityValues(final String id, final String password) throws RemoteException {
        traverser.setSecurityValues(id, password);
    }
}
