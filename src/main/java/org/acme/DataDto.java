package org.acme;

import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;
import org.jboss.resteasy.annotations.providers.multipart.PartType;

import java.io.File;

public class DataDto {

    @FormParam("data")
    public File data;

    @FormParam("filename")
    @PartType(MediaType.TEXT_PLAIN)
    public String filename;

    @FormParam("mimeType")
    @PartType(MediaType.TEXT_PLAIN)
    public String mimeType;
}
