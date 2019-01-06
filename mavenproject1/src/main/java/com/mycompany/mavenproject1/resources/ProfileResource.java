/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.resources;

import java.net.URI;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.ErrorMessage;
import model.Link;
import model.Profile;
import service.ProfileService;

/**
 *
 * @author Maniek
 */
@Path("/profiles")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class ProfileResource {
    
    ErrorMessage notFound = new ErrorMessage("File not found",404,"");
    ErrorMessage alreadyExist = new ErrorMessage("Already exists", 406, "");
    
    private ProfileService profileService = new ProfileService();
    
    @GET
    public List<Profile> getProfiles(){
        return profileService.getAllProfiles();
    }
    
    @GET
    @Path("/{profileName}")
    public Profile getProfile(@PathParam("profileName") String name, @Context UriInfo uriInfo){
        Profile newProfile = profileService.getProfile(name);
        if (newProfile == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
        String uriSelf = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(name).build().toString();
        newProfile.addLink(new Link(uriSelf, "self"));  
        return newProfile;
    }
    
    @POST
    public Response addProfile(Profile profile, @Context UriInfo uriInfo){
        Profile newProfile = profileService.addProfile(profile);
        if (newProfile == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_ACCEPTABLE).entity(alreadyExist).build());
        }
        String newId = String.valueOf(newProfile.getId());  //adding header value;
        URI location = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(location).entity(newProfile).build();
    }
    
    @PUT
    @Path("/{profileName}")
    public Profile updateProfile(@PathParam("profileName") String name, Profile profile){
        if (profileService.getProfile(name) == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
        profile.setId(profileService.getProfile(name).getId());
        profile.setProfileName(name);
        return profileService.updateProfile(profile);
    }
    
    @DELETE
    @Path("/{profileName}")
    public void deleteMessage(@PathParam("profileName") String name){
       if(profileService.removeProfile(name) == null){
           throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build()); 
       }
    }
}
