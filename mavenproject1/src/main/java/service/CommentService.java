/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.mycompany.mavenproject1.database.DatabaseClass;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Comment;

/**
 *
 * @author Maniek
 */
public class CommentService {
    
    DatabaseClass databaseConnection = new DatabaseClass();
         
    public List <Comment> getAllComments(long messageId){
        Connection connection = databaseConnection.getConnection();
        try {
            List<Comment> list = new ArrayList<Comment>();
            Statement statement = null;
            ResultSet resultSet = null;
            statement=connection.createStatement();
            resultSet=statement.executeQuery("SELECT * FROM comment WHERE message_id = " + messageId);
            while(resultSet.next()){
                long comment_id = 0;
                String comment_content = "";
                java.sql.Timestamp created = null;
                String author = "";
                long message_id = 0;
                
                comment_id = resultSet.getLong("comment_id");
                comment_content = resultSet.getString("comment_content");
                created = resultSet.getTimestamp("created");
                author = resultSet.getString("author");
                
                Comment newComment = new Comment(comment_id, comment_content, author);
                newComment.setCreated(created);
                list.add(newComment);        
            }
            return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Comment getComment(long messageId, long commentId){
        List<Comment> list = this.getAllComments(messageId);
        for (Comment comment : list){
            if (comment.getId() == commentId){
                return comment;
            }
        }
        return null;
    }
    
    public Comment addComment(long messageId, Comment comment){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "INSERT INTO comment(comment_content,created,author,message_id) VALUES (?,?,?,?)";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, comment.getMessage());
            preparedStatement.setTimestamp(2, comment.getCreated());
            preparedStatement.setString(3, comment.getAuthor());
            preparedStatement.setLong(4, messageId);
            preparedStatement.executeUpdate();
            long lastCommentIndex = this.getAllComments(messageId).get(this.getAllComments(messageId).size()-1).getId();
            return this.getComment(messageId, lastCommentIndex);
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Comment updateComment(long messageId, Comment comment){
        Connection connection = databaseConnection.getConnection();
        try {
            if (comment.getId() <= 0){
                return null;
            }
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "UPDATE comment SET comment_content = ?, created = ?, author = ? WHERE comment_id = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, comment.getMessage());
            preparedStatement.setTimestamp(2, comment.getCreated());
            preparedStatement.setString(3, comment.getAuthor());
            preparedStatement.setLong(4, comment.getId());
            preparedStatement.executeUpdate();
            return this.getComment(messageId, comment.getId());
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Comment removeComment(long messageId, long commentId){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "DELETE FROM comment WHERE comment_id = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, commentId);
            preparedStatement.executeUpdate();
            return new Comment();
        } catch (SQLException ex) {
            Logger.getLogger(CommentService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
}
