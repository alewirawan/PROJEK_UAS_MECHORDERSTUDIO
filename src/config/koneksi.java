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
    private static Connection koneksi;
    
    public static Connection getConnection() {
        try {
            String url = "jdbc:mysql://localhost:3306/db_merchorderstudio";
            String user = "root";
            String pass = "";
            
            Connection conn = DriverManager.getConnection(url, user, pass);
            return conn;
        } catch (Exception e) {
            System.out.println("Koneksi Gagal : " + e.getMessage());
            return null;
        }
    }
}
