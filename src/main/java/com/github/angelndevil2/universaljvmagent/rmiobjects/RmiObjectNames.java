package com.github.angelndevil2.universaljvmagent.rmiobjects;

import java.io.Serializable;

/**
 * @author k, Created on 16. 2. 22.
 */
public class RmiObjectNames implements Serializable {
    private static final long serialVersionUID = -8446767601118642301L;

    public final static String VM_INFO = IVmInfo.class.getCanonicalName();
    public final static String MBEAN_FACTORY = IMBeanServerFactory.class.getCanonicalName();
    public final static String CONTEXT_TRAVERSER = IJndiContextTraverser.class.getCanonicalName();
}
