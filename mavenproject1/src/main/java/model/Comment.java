/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Maniek
 */
public class Comment {
    private long id;
    private String message;
    private java.sql.Timestamp created;
    private String author;
    private List<Link> links;
    

    public Comment(){
        this.links = new ArrayList<>();
    }
    public Comment(long id, String message, String author){
        this.id = id;
        this.message = message;
        this.author = author;
        this.created = getCurrentTimeStamp();
        this.links = new ArrayList<>();
    }

    /**
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the created
     */
    public java.sql.Timestamp getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(java.sql.Timestamp created) {
        this.created = created;
    }

    /**
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author to set
     */
    public void setAuthor(String author) {
        this.author = author;
    }
         /**
     * @return the links
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
    public void addLink(Link link){
        this.links.add(link);
    }
    
    private static java.sql.Timestamp getCurrentTimeStamp() {
        java.util.Date today = new java.util.Date();
	return new java.sql.Timestamp(today.getTime());
    }
}
