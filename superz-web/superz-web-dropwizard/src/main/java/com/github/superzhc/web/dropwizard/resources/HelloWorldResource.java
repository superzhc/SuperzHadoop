package com.github.superzhc.web.dropwizard.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/helloworld")
@Produces(MediaType.APPLICATION_JSON)
public class HelloWorldResource {

    @GET
    public String get() {
        return null;
    }
}
