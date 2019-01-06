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
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import model.ErrorMessage;
import model.Link;
import model.Message;
import service.MessageService;

/**
 *
 * @author Maniek
 */


@Path("/messages")
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class MessageResource {
       
    MessageService messageService = new MessageService();
    
    ErrorMessage notFound = new ErrorMessage("File not found",404,"");
    
    @GET
    public List<Message> getMessages(@QueryParam("year") int year,
                                     @QueryParam("start") int start,
                                     @QueryParam("size") int size){
        if (year > 0){
            return messageService.getAllMessagesForYear(year);
        }
        if (start > 0 && size > 0){
            return messageService.getAllMessagesPaginated(start, size);
        }
        return messageService.getAllMessages();
    }
    
    @GET
    @Path("/{messageId}")
    public Message getMessage(@PathParam("messageId") long id, @Context UriInfo uriInfo){
        Message newMessage = messageService.getMessage(id);
        if (newMessage == null){  
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
        String uriSelf = uriInfo.getBaseUriBuilder().path(MessageResource.class).path(Long.toString(newMessage.getId())).build().toString();
        String uriProfile = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(newMessage.getAuthor()).build().toString();
        String uriComments = uriInfo.getBaseUriBuilder().path(MessageResource.class).path(MessageResource.class, "getCommentResource").resolveTemplate("messageId", newMessage.getId()).build().toString();
        newMessage.addLink(new Link(uriSelf, "self"));  
        newMessage.addLink(new Link(uriProfile, "profile"));
        newMessage.addLink(new Link(uriComments, "comments"));
        return newMessage;
    }
    
    @POST
    public Response addMessage(Message message, @Context UriInfo uriInfo){
        Message newMessage = messageService.addMessage(message);
        String newId = String.valueOf(newMessage.getId());  //adding header value;
        URI location = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(location).entity(newMessage).build();
    }
    
    @PUT
    @Path("/{messageId}")
    public Message updateMessage(@PathParam("messageId") long id, Message message){
        message.setId(id);
        if (messageService.getMessage(id) == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
        Message response = messageService.updateMessage(message);
        if (response == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
        return response;
    }
    
    @DELETE
    @Path("/{messageId}")
    public void deleteMessage(@PathParam("messageId") long id){
        if (messageService.removeMessage(id) == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }
    }
    
    @Path("/{messageId}/comments")
    public CommentResource getCommentResource(){
        return new CommentResource(); 
    } 
    
}
