/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Maniek
 */
public class DatabaseClass {
    static final String DB_URL =
      "jdbc:mysql://127.0.0.1:3306/test1";
    static final String DB_DRV =
      "com.mysql.jdbc.Driver";
    static final String DB_USER = "maniek";
    static final String DB_PASSWD = "maniek";
    
    public DatabaseClass(){
        
    }
    
    public Connection getConnection(){
        Connection connection = null;
        try{
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(DatabaseClass.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
            connection=DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWD);
            return connection;
            }catch(SQLException ex){
                ex.printStackTrace();
                return null;
            }
    }
    
    public void closeConnection(Connection conn){
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
