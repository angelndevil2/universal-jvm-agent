package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.jmx.json.StatsJson;
import com.github.angelndevil2.universaljvmagent.Agent;
import com.github.angelndevil2.xii4j.Stats;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.management.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import static com.google.common.base.Preconditions.checkArgument;

/**
 *
 * @since 0.0.3
 * @author k, Created on 16. 2. 28.
 */
@Path("/mbean/{id}/{object-name}")
public class MBean {
    /**
     *  create json string from attribute object with {@link Gson#toJson(Object, Type)} which Type parameter is attribute.getClass()
     *
     * @param id mbean server id
     * @param objectName object name
     * @param name attribute name
     * @return Response which body is json string of attribute
     * @throws MalformedObjectNameException
     * @throws AttributeNotFoundException
     * @throws MBeanException
     * @throws ReflectionException
     * @throws InstanceNotFoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attribute/{name}")
    public Response getAttribute(
                                 @PathParam("id") String id,
                                 @PathParam("object-name") String objectName,
                                 @PathParam("name") String name)
            throws
            MalformedObjectNameException,
            AttributeNotFoundException,
            MBeanException,
            ReflectionException,
            InstanceNotFoundException {

        checkArgument(id != null);
        checkArgument(objectName != null);
        checkArgument(name != null);

         Object attribute = Agent.getFactory().getMBeanAttribute(id, new ObjectName(objectName), name);

         if (attribute == null) return nullReturn();

        return getAttribute(attribute);
    }

    /**
     *
     * @param id mbean server id
     * @param objectName object name
     * @param name attribute name
     * @param type
     * @return Response which body is json string of attribute
     * @throws MalformedObjectNameException
     * @throws AttributeNotFoundException
     * @throws MBeanException
     * @throws ReflectionException
     * @throws InstanceNotFoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/attribute/{name}/{type}")
    public Response getAttribute(
            @PathParam("id") String id,
            @PathParam("object-name") String objectName,
            @PathParam("name") String name,
            @PathParam("type") String type) throws
            MalformedObjectNameException,
            AttributeNotFoundException,
            MBeanException,
            ReflectionException,
            InstanceNotFoundException,
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException {

        checkArgument(id != null);
        checkArgument(objectName != null);
        checkArgument(name != null);
        checkArgument(type != null);

        Object attribute = Agent.getFactory().getMBeanAttribute(id, new ObjectName(objectName), name);

        if (attribute == null) return nullReturn();

        if (type.equals(Stats.IMPLEMENTED_FOR)) {
            return Response.status(200).entity(StatsJson.toJsonString(attribute)).build();
        }

        return getAttribute(attribute);
    }

    private Response getAttribute(Object attribute) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        return Response.status(200).entity(gson.toJson(attribute, attribute.getClass())).build();
    }

    private Response nullReturn() {
        return Response.status(200).entity("null").build();
    }
}
