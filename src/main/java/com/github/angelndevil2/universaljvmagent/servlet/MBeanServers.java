package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.Agent;
import org.json.simple.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/mbean-servers/")
public class MBeanServers {
    @Context
    private HttpServletRequest httpRequest;

    /**
     *
     * @return mbean server id
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getIds() {
        ArrayList<String> list = Agent.getFactory().getAllMBeanServerId();
        JSONArray ret = new JSONArray();
        for (String id : list) ret.add(id);

        return Response.status(200).entity(ret.toJSONString()).build();
    }

}
