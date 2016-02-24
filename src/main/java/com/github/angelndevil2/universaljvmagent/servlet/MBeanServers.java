package com.github.angelndevil2.universaljvmagent.servlet;

import com.github.angelndevil2.universaljvmagent.client.CommandHandler;
import com.github.angelndevil2.universaljvmagent.util.PropertiesUtil;
import com.github.angelndevil2.universaljvmagent.util.StringSplit;
import org.json.simple.JSONArray;

import javax.management.ObjectName;
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
import java.util.ArrayList;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/{host}/mbeanservers")
public class MBeanServers {
    @Context
    private HttpServletRequest httpRequest;

    /**
     *
     * @return mbean server id
     * @throws RemoteException
     * @throws NotBoundException
     */
    @GET
    @Path("ids")
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    public Response getIds(@PathParam("host") String host) throws RemoteException, NotBoundException {

        checkArgument(host != null);

        StringSplit.AddrAndPort addrAndPort = StringSplit.getAddrAndPort(host, PropertiesUtil.getRimServerPort());
        CommandHandler ch = new CommandHandler(addrAndPort.getAddr(), addrAndPort.getPort());

        ArrayList<String> list = ch.getAllMBeanServerId();
        JSONArray ret = new JSONArray();
        for (String id : list) ret.add(id);

        return Response.status(200).entity(ret.toJSONString()).build();
    }

    /**
     *
     * @param id mbean server id
     * @return objects's ObjectName.toString() of mbean server
     * @throws RemoteException
     * @throws NotBoundException
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @SuppressWarnings("unchecked")
    @Path("{id}/names")
    public Response getMBeanNames(@PathParam("host") String host, @PathParam("id") String id) throws RemoteException, NotBoundException {

        checkArgument(host != null);

        JSONArray ret = new JSONArray();

        StringSplit.AddrAndPort addrAndPort = StringSplit.getAddrAndPort(host, PropertiesUtil.getRimServerPort());
        CommandHandler ch = new CommandHandler(addrAndPort.getAddr(), addrAndPort.getPort());

        Set<ObjectName> list = ch.getAllMBeanNames(id);
        for (ObjectName name : list) ret.add(name.toString());

        return Response.status(200).entity(ret.toJSONString()).build();
    }
}
