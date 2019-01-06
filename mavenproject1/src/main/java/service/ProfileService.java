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
import model.Profile;

/**
 *
 * @author Maniek
 */
public class ProfileService {
    
    DatabaseClass databaseConnection = new DatabaseClass();
    
    public List<Profile> getAllProfiles(){
        Connection connection = databaseConnection.getConnection();
        try {
            List<Profile> list = new ArrayList<Profile>();
            Statement statement = null;
            ResultSet resultSet = null;
            statement=connection.createStatement();
            resultSet=statement.executeQuery("SELECT * FROM profile");
            while(resultSet.next()){
                long profile_id = 0;
                String profile_name = "";
                String profile_firstName = "";
                String profile_lastName = "";
                java.sql.Timestamp created = null;
                
                profile_id = resultSet.getLong("profile_id");
                profile_name = resultSet.getString("profile_name");
                profile_firstName = resultSet.getString("profile_firstName");
                profile_lastName = resultSet.getString("profile_lastName");
                created = resultSet.getTimestamp("created");
                
                Profile newProfile = new Profile(profile_id, profile_name, profile_firstName, profile_lastName);
                newProfile.setCreated(created);
                list.add(newProfile);
            }
            return list;
        } catch (SQLException ex) {
            Logger.getLogger(ProfileService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Profile getProfile(String name){
        List<Profile> list = this.getAllProfiles();
        for (Profile profile : list){
            if (profile.getProfileName().equals(name)){
                return profile;
            }
        }
        return null;
    }
    
    public Profile addProfile(Profile profile){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            if (this.doesProfileAlreadyExist(profile)){
                return null;
            }
            String sql = "INSERT INTO profile(profile_name,profile_firstName,profile_lastName,created) VALUES (?,?,?,?)";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, profile.getProfileName());
            preparedStatement.setString(2, profile.getFirstName());
            preparedStatement.setString(3, profile.getLastName());
            preparedStatement.setTimestamp(4, profile.getCreated());
            preparedStatement.executeUpdate();
            return this.getProfile(profile.getProfileName());
        } catch (SQLException ex) {
            Logger.getLogger(ProfileService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Profile updateProfile(Profile profile){
        Connection connection = databaseConnection.getConnection();
        try {
            if (profile.getProfileName().isEmpty()){
                return null;
            }
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "UPDATE profile SET profile_firstName = ?, profile_lastName = ?, created = ? WHERE profile_id = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, profile.getFirstName());
            preparedStatement.setString(2, profile.getLastName());
            preparedStatement.setTimestamp(3, profile.getCreated());
            preparedStatement.setLong(4, profile.getId());
            preparedStatement.executeUpdate();
            return this.getProfile(profile.getProfileName());
        } catch (SQLException ex) {
            Logger.getLogger(ProfileService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    
    public Profile removeProfile(String profileName){
        Connection connection = databaseConnection.getConnection();
        try {
            Statement statement = null;
            ResultSet resultSet = null;
            String sql = "DELETE FROM profile WHERE profile_name = ?";
            statement=connection.createStatement();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, profileName);
            preparedStatement.executeUpdate();
            return new Profile();
        } catch (SQLException ex) {
            Logger.getLogger(ProfileService.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
            return null;
        }
        finally{
            databaseConnection.closeConnection(connection);
        }
    }
    public boolean doesProfileAlreadyExist(Profile profile){
        List<Profile> list = this.getAllProfiles();
        boolean help = false;
        for (Profile listProfile : list){
            if (listProfile.getProfileName().equals(profile.getProfileName())){
                help = true;
            }
        }
        return help;
    }
}
