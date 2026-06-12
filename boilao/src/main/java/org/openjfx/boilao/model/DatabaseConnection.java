/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.openjfx.boilao.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fabioas
 */
public class DatabaseConnection {
    private static final String DATABASE = "postgres";
    private static final String HOST = "aws-1-sa-east-1.pooler.supabase.com:6543";
    private static final String DRIVER = "org.postgresql.Driver";
    private static final String URL = "jdbc:postgresql://"+HOST+"/"+DATABASE;
    private static final String USR = "postgres.lvklhmbbfrqrqkhmkoqz";
    private static final String PWD = "OfBqgdDSljik1Ijz";
    
    private static DatabaseConnection instance;
    private Connection connection;

    public DatabaseConnection() {
        try {
            Class.forName(DRIVER);
            connection = DriverManager.getConnection(URL, USR, PWD);
        }
        catch (ClassNotFoundException | SQLException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
    
    public Connection getConnection() {
        return connection;
    }
    
    public void Disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexão encerrada com sucesso");
            }
        }
        catch (SQLException e) {
            System.err.println("ERRO: " + e.getMessage());
        }
    }
}
