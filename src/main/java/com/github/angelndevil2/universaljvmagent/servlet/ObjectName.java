package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.client.CommandHandler;
import org.json.simple.JSONObject;

import javax.management.MalformedObjectNameException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Hashtable;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/object-name")
public class ObjectName {
    @Context
    private HttpServletRequest httpRequest;

    /**
     *
     * @return mbean server id
     * @throws RemoteException
     * @throws NotBoundException
     */
    @GET
    @Path("{name}/key-prop-list")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getKeyPropList(@PathParam("name") String name) throws RemoteException, NotBoundException, MalformedObjectNameException {

        checkArgument(name != null);

        Hashtable<String, String> props = CommandHandler.getMBeanKeyPropList(name);

        JSONObject ret = new JSONObject();
        for (String key : props.keySet()) ret.put(key, props.get(key));

        return Response.status(200).entity(ret.toJSONString()).build();
    }

}
