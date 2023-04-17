package org.acme;

import jakarta.annotation.security.PermitAll;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@PermitAll
@Path("acme")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StorageResource {

    @GET
    @Path("/{directory}")
    public Response listData() {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        return Response.ok(Map.of("data", "data")).build();
    }

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadData(@MultipartForm DataDto dataDto) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return Response.ok(Map.of("data", "data")).build();
    }
}
