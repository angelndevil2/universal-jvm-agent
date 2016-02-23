package com.github.angelndevil2.universaljvmagent.server;

import com.github.angelndevil2.universaljvmagent.rmiobjects.IJndiContextTraverser;
import com.google.common.collect.ArrayListMultimap;
import lombok.extern.slf4j.Slf4j;

import javax.naming.*;
import java.rmi.RemoteException;
import java.util.Hashtable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author k, Created on 16. 2. 22.
 */
@Slf4j
public class JndiContextTraverser implements IJndiContextTraverser {

    private static final long serialVersionUID = -2978408714946789520L;

    private transient Context context;
    private final Hashtable env = new Hashtable();

    public JndiContextTraverser() {
        this(null);
    }

    public JndiContextTraverser(final Context ctx) {
        this(ctx, "\t");
    }

    public JndiContextTraverser(final Context ctx, final String indent) {
        context = ctx;
        try {
            setSecurityValues("netstraw", "netfunnel1234");
            setContext();
            traverse();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static ArrayListMultimap traverse(final Context context, final String name) {

        checkArgument(name != null, "name argument is null");
        checkArgument(context != null, "context argument is null");

        ArrayListMultimap<NameClassPair, ArrayListMultimap> ret = ArrayListMultimap.create();

        NamingEnumeration<NameClassPair> enumeration = null;
        try {
            enumeration = context.list(name);
        } catch (Throwable t) {
            log.trace(t.getMessage(), t);
            return ret;
        }

        try {
            while (enumeration != null && enumeration.hasMore()) {

                NameClassPair ncp = enumeration.next();

                ArrayListMultimap child = traverse(context, name.length() == 0? ncp.getName():name+"/"+ncp.getName());
                ret.put(ncp, child);
            }
        } catch (NamingException e) {
            log.trace(e.getMessage(), e);
        }

        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setContext() throws RemoteException {
        try {
            this.context = new InitialContext(env);
        } catch (NamingException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setContext(Context ctx) throws RemoteException {
        try {
            checkArgument(ctx != null, "context argument is null");
        } catch (IllegalArgumentException e) {
            throw new RemoteException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayListMultimap traverse(String name) throws RemoteException {
        return traverse(context, name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ArrayListMultimap traverse() throws RemoteException {
        return traverse(context, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public void setSecurityValues(final String id, final String password) throws RemoteException {
        if (id != null) env.put(Context.SECURITY_PRINCIPAL, id);
        if (password != null) env.put(Context.SECURITY_CREDENTIALS, password);
    }
}
