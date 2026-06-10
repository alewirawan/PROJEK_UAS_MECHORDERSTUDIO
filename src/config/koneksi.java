/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author alewi
 */
public class koneksi {
    private static Connection conn;
    
    public static Connection getConnection() {
        try {
            if (conn == null || conn.isClosed()){
                conn = DriverManager.getConnection(
                        "jdbc:mysql://localhost:3306/db_merchorderstudio",
                        "root",
                        "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
