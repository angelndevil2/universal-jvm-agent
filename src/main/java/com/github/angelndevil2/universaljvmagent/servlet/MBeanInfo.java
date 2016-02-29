package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.Agent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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
@Path("/mbean-info/{id}/{object-name}")
public class MBeanInfo {

    /**
     *
     * @param id mbean server id
     * @param objectName object name
     * @return mbean info string
     * @throws InstanceNotFoundException
     * @throws IntrospectionException
     * @throws MalformedObjectNameException
     * @throws ReflectionException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getMBeanInfo(@PathParam("id") String id, @PathParam("object-name") String objectName)
            throws MalformedObjectNameException, IntrospectionException, InstanceNotFoundException, ReflectionException {

        checkArgument(id != null);
        checkArgument(objectName != null);

        javax.management.MBeanInfo info = Agent.getFactory().getMBeanInfo(id, new javax.management.ObjectName(objectName));
        return Response.status(200).entity(info.toString()).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    @Path("/attributes")
    public Response getAttributes(@PathParam("id") String id, @PathParam("object-name") String objectName)
            throws MalformedObjectNameException, IntrospectionException, InstanceNotFoundException, ReflectionException {

        checkArgument(id != null);
        checkArgument(objectName != null);

        javax.management.MBeanInfo info = Agent.getFactory().getMBeanInfo(id, new javax.management.ObjectName(objectName));
        MBeanAttributeInfo[] ainfo = info.getAttributes();
        JSONArray ret = new JSONArray();
        for (MBeanAttributeInfo mai : ainfo) {
            JSONObject attr = new JSONObject();
            attr.put("name", mai.getName());
            attr.put("type", mai.getType());
            attr.put("isReadable", mai.isReadable());
            attr.put("isWritable", mai.isWritable());
            attr.put("isIs", mai.isIs());
            ret.add(attr);
        }
        return Response.status(200).entity(ret.toJSONString()).build();
    }
}
