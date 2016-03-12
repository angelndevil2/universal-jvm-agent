package com.github.angelndevil2.universaljvmagent.server;

import com.google.common.collect.ArrayListMultimap;
import lombok.extern.slf4j.Slf4j;

import javax.naming.*;
import java.io.Serializable;
import java.util.Hashtable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @author k, Created on 16. 2. 22.
 */
@Slf4j
public class JndiContextTraverser implements Serializable{

    private static final long serialVersionUID = -2978408714946789520L;

    private transient Context context;
    private final Hashtable env = new Hashtable();

    public JndiContextTraverser() {
        this(null);
    }

    public JndiContextTraverser(final Context ctx) {
        context = ctx;
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
     * set context with {@link javax.naming.InitialContext}
     */
    public void setContext() throws NamingException {
        this.context = new InitialContext(env);
    }

    public void setContext(Context ctx) {
        checkArgument(ctx != null, "context argument is null");
        this.context = ctx;
    }

    /**
     * start travers from name
     *
     * @param name jndi name
     */
    public ArrayListMultimap traverse(String name) {
        return traverse(context, name);
    }

    /**
     * start travers from root
     *
     */
    public ArrayListMultimap traverse() {
        return traverse(context, "");
    }

    /**
     * set Context.SECURITY_PRINCIPAL, Context.SECURITY_CREDENTIALS from remote values
     */
    @SuppressWarnings("unchecked")
    public void setSecurityValues(final String id, final String password) {
        if (id != null) env.put(Context.SECURITY_PRINCIPAL, id);
        if (password != null) env.put(Context.SECURITY_CREDENTIALS, password);
    }
}
