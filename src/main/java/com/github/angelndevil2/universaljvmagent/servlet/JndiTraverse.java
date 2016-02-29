package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.server.JndiContextTraverser;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * servlet class for test
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/jndi-traverse/")
public class JndiTraverse {
    /**
     * get context list
     *
     * @return
     * @throws NamingException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listContext() throws NamingException {

        JndiContextTraverser traverser = new JndiContextTraverser();
        traverser.setSecurityValues(PropertiesUtil.getJndiUserId(), PropertiesUtil.getJndiUserPassword());
        traverser.setContext();
        return Response.status(200).entity(traverser.traverse().toString()).build();
    }

    /**
     * get context list start from id
     *
     * @param id
     * @return
     * @throws NamingException
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listContext(@PathParam("id") String id) throws NamingException {
        checkArgument(id != null);

        JndiContextTraverser traverser = new JndiContextTraverser();
        traverser.setSecurityValues(PropertiesUtil.getJndiUserId(), PropertiesUtil.getJndiUserPassword());
        traverser.setContext();
        return Response.status(200).entity(traverser.traverse(id.replace("_", "/")).toString()).build();

    }
}
