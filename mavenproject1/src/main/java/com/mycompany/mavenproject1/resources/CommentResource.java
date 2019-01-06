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
import model.Comment;
import model.ErrorMessage;
import model.Link;
import service.CommentService;

/**
 *
 * @author Maniek
 */
@Consumes(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
@Produces(value = {MediaType.APPLICATION_JSON, MediaType.TEXT_XML})
public class CommentResource {
    
    ErrorMessage notFound = new ErrorMessage("File not found",404,"");
    
    private CommentService commentService = new CommentService();
    
    @GET
    public List<Comment> getAllComments(@PathParam("messageId") long messageId){
        return commentService.getAllComments(messageId);
    }
    
    @GET
    @Path("/{commentId}")
    public Comment getComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId, @Context UriInfo uriInfo){
        Comment newComment = commentService.getComment(messageId, commentId);
        if (newComment == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build()); 
        }
        String uriSelf = uriInfo.getBaseUriBuilder().path(MessageResource.class).path(MessageResource.class, "getCommentResource").resolveTemplate("messageId", messageId).path(Long.toString(commentId)).build().toString();
        String uriProfile = uriInfo.getBaseUriBuilder().path(ProfileResource.class).path(newComment.getAuthor()).build().toString();
        newComment.addLink(new Link(uriSelf, "self"));  
        newComment.addLink(new Link(uriProfile, "profile"));
        return newComment;
    }
    
    @POST
    public Response addComment(@PathParam("messageId") long messageId, Comment comment, @Context UriInfo uriInfo){
        Comment newComment = commentService.addComment(messageId, comment);
        String newId = String.valueOf(newComment.getId());  //adding header value;
        URI location = uriInfo.getAbsolutePathBuilder().path(newId).build();
        return Response.created(location).entity(newComment).build();
    }
    
    @PUT
    @Path("/{commentId}")
    public Comment updateComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId, Comment comment){
        comment.setId(commentId);
        Comment response = commentService.updateComment(messageId, comment);
        if (response == null){
            throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
        }   
        return response;
    }
    
    @DELETE
    @Path("/{commentId}")
    public void deleteComment(@PathParam("messageId") long messageId, @PathParam("commentId") long commentId){
       if(commentService.removeComment(messageId, commentId) == null){
           throw new WebApplicationException(Response.status(Response.Status.NOT_FOUND).entity(notFound).build());
       }
    }
    
}
