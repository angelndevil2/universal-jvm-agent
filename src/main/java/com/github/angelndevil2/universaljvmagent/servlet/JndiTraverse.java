package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.client.CommandHandler;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * servlet class for test
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/jndi-traverse")
public class JndiTraverse {
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String listContext(@PathParam("id") String id) throws RemoteException, NotBoundException {
        checkArgument(id != null);

        CommandHandler ch = new CommandHandler("192.168.100.242");
        if (id.equals("all")) {
            return ch.getJndiContext().toString();
        } else {
            System.out.println(id);
            return ch.getJndiContext(id.replace("_", "/")).toString();
        }
    }
}
