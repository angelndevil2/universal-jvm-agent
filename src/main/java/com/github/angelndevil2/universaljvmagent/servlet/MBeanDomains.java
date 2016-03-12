package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.Agent;
import org.json.simple.JSONArray;

import javax.management.InstanceNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 27.
 */
@Path("/domains/{id}")
public class MBeanDomains {
    /**
     * @param id mbean server id
     * @return mbean domains
     * @throws InstanceNotFoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getDomains(@PathParam("id") String id) throws InstanceNotFoundException {

        checkArgument(id != null);

        String[] list = Agent.getFactory().getDomains(id);
        JSONArray ret = new JSONArray();
        Collections.addAll(ret, list);

        return Response.status(200).entity(ret.toJSONString()).build();
    }
}
