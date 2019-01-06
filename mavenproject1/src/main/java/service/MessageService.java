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
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Message;

/**
 *
 * @author Maniek
 */
public class MessageService {
    
    DatabaseClass databaseConnection = new DatabaseClass();
    
    public MessageService(){
    }
    
    public List<Message> getAllMessages(){
        Connection connection = databaseConnection.getConnection();
        try {
            List<Message> list = new ArrayList<Message>();
            Statement statement = null;
            ResultSet resultSet = null;
            statement=connection.createStatement();
            resultSet=statement.executeQuery("SELECT * FROM message");
                while(resultSet.next()){
                    long message_id = 0;
                    String message_content = "";
                    java.sql.Timestamp created = null;
                    String author = "";
            
                    message_id = resultSet.getLong("message_id");
                    message_content = resultSet.getString("message_content");
                    created = resultSet.getTimestamp("created");
                    author = resultSet.getString("author");
                
                    Message newMessage = new Message(message_id, message_content, author);
                    newMessage.setCreated(created);
                    list.add(newMessage);
                }
                return list;
            
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public List<Message> getAllMessagesForYear(int year){
        List<Message> list = this.getAllMessages();
        List<Message> help = new ArrayList<Message>();
        Calendar calendar = Calendar.getInstance();
        for (Message message : list){  
            calendar.setTime(message.getCreated());
            if (calendar.get(Calendar.YEAR) == year){
                help.add(message);
            }
        }
        return help;
    }
    
    public List<Message> getAllMessagesPaginated(int start, int size){
        List<Message> list = this.getAllMessages();
        List<Message> help = new ArrayList<Message>();
        for (Message message : list){
           if ((message.getId() >= start) && (message.getId() < (start + size))){
               help.add(message);
           } 
        }
        return help;
    }
    
    public Message getMessage(long id){
        List<Message> list = this.getAllMessages();
        for (Message message : list){
            if (message.getId() == id){
                return message;
            }
        }
        return null;
    }
    public Message addMessage(Message message){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "INSERT INTO message(message_content,created,author) VALUES (?,?,?)";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setTimestamp(2, message.getCreated());
            preparedStatement.setString(3, message.getAuthor());
            preparedStatement.executeUpdate();
            long lastMessageIndex = this.getAllMessages().get(this.getAllMessages().size()-1).getId();
            return this.getMessage(lastMessageIndex);
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    public Message updateMessage(Message message){
        Connection connection = databaseConnection.getConnection();
        try {
            if (message.getId() <= 0){
                return null;
            }
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "UPDATE message SET message_content = ?, created = ?, author = ? WHERE message_id = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, message.getMessage());
            preparedStatement.setTimestamp(2, message.getCreated());
            preparedStatement.setString(3, message.getAuthor());
            preparedStatement.setLong(4, message.getId());
            preparedStatement.executeUpdate();
            return this.getMessage(message.getId());
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    public Message removeMessage(long id){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "DELETE FROM message WHERE message_id = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
            return new Message();
        } catch (SQLException ex) {
            Logger.getLogger(MessageService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
}
