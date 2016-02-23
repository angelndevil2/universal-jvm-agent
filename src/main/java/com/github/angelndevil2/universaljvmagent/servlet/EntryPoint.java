package com.github.angelndevil2.universaljvmagent.servlet;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * servlet class for test
 * @since 0.0.3
 * @author k, Created on 16. 2. 24.
 */
@Path("/entry-point")
public class EntryPoint {
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String test() {
        return "Test";
    }
}
