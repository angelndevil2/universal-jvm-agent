package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.Agent;
import org.json.simple.JSONArray;

import javax.management.InstanceNotFoundException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 27.
 */
@Path("/mbeans/{id}")
public class MBeans {
    /**
     *
     * @param id mbean server id
     * @return objects's ObjectName.toString() of mbean server
     * @throws InstanceNotFoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getMBeanNames(@PathParam("id") String id)
            throws InstanceNotFoundException {

        checkArgument(id != null);

        JSONArray ret = new JSONArray();

        Set<javax.management.ObjectName> list = Agent.getFactory().queryNames(id, (ObjectName) null, null);
        for (javax.management.ObjectName name : list) ret.add(name.toString());

        return Response.status(200).entity(ret.toJSONString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    @Path("/{domain}")
    public Response getMBeanNames(@PathParam("id") String id, @PathParam("domain") String domain)
            throws MalformedObjectNameException, InstanceNotFoundException {

        checkArgument(id != null);
        checkArgument(domain != null);

        JSONArray ret = new JSONArray();

        Set<javax.management.ObjectName> list = Agent.getFactory().queryNames(id, domain+":*", null);
        for (javax.management.ObjectName name : list) ret.add(name.toString());

        return Response.status(200).entity(ret.toJSONString()).build();
    }

}
