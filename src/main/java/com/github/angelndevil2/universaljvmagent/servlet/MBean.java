package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.Agent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;

import javax.management.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 28.
 */
@Path("/mbean/{id}/{object-name}")
@Slf4j
public class MBean {
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    @Path("/attribute/{name}")
    public Response getAttribute(
                                 @PathParam("id") String id,
                                 @PathParam("object-name") String objectName,
                                 @PathParam("name") String name)
            throws MalformedObjectNameException, AttributeNotFoundException,
            MBeanException, ReflectionException, InstanceNotFoundException {

        //JSONObject ret = new JSONObject();
        checkArgument(id != null);
        checkArgument(objectName != null);
        checkArgument(name != null);

        Object attribute = Agent.getFactory().getMBeanAttribute(id, new ObjectName(objectName), name);
        Gson gson = new GsonBuilder().serializeNulls().create();
/*        if (attribute != null)
            ret.put(name,attribute.toString());
        else ret.put(name, null);*/
        return Response.status(200).entity(gson.toJson(attribute)).build();
    }
}
