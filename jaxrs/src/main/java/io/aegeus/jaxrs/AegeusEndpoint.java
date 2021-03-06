package io.aegeus.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/")
public interface AegeusEndpoint {
    
    @GET
    @Path("/register")
    @Produces(MediaType.TEXT_PLAIN)
    String register(@QueryParam("addr") String rawAddr) throws GeneralSecurityException, IOException;

    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    SFHandle add(@QueryParam("addr") String rawAddr, @QueryParam("path") String path, InputStream input) throws IOException, GeneralSecurityException;
    
    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    SFHandle get(@QueryParam("addr") String rawAddr, @QueryParam("cid") String cid, @QueryParam("path") String path, @QueryParam("timeout") Long timeout) throws IOException, GeneralSecurityException;

    @GET
    @Path("/send")
    @Produces(MediaType.APPLICATION_JSON)
    SFHandle send(@QueryParam("addr") String rawAddr, @QueryParam("cid") String cid, @QueryParam("target") String rawTarget, @QueryParam("timeout") Long timeout) throws IOException, GeneralSecurityException;

    @GET
    @Path("/findkey")
    @Produces(MediaType.APPLICATION_JSON)
    String findRegistation(@QueryParam("addr") String rawAddr) throws IOException;

    @GET
    @Path("/findipfs")
    @Produces(MediaType.APPLICATION_JSON)
    List<SFHandle> findIPFSContent(@QueryParam("addr") String rawAddr, @QueryParam("timeout") Long timeout) throws IOException;

    @GET
    @Path("/findlocal")
    @Produces(MediaType.APPLICATION_JSON)
    List<SFHandle> findLocalContent(@QueryParam("addr") String rawAddr) throws IOException;
    
    @GET
    @Path("/getlocal")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    InputStream getLocalContent(@QueryParam("addr") String rawAddr, @QueryParam("path") String path) throws IOException;
    
    @GET
    @Path("/dellocal")
    @Produces(MediaType.TEXT_PLAIN)
    boolean deleteLocalContent(@QueryParam("addr") String rawAddr, @QueryParam("path") String path) throws IOException;
}