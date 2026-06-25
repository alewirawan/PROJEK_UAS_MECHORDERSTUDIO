/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utility;

import config.koneksi;
import java.util.regex.Pattern;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author alewi
 */
public class validasi {
    public static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    
    public static boolean isValidEmail(String email){
        if (email == null){
            return false;
        }
        
        return Pattern.matches(EMAIL_REGEX, email.trim());
    }
    
    public static boolean isEmailExists(String email){
        if (email == null){
            return false;
        }
                
        String sql = "SELECT 1 FROM users WHERE email = ?";
        
        Connection conn = koneksi.getConnection();
        
        try (PreparedStatement pst = conn.prepareStatement(sql)){
            pst.setString(1, email);
            ResultSet rs = pst.executeQuery();
            return rs.next();
        } catch(Exception e){
                System.out.println(e.getMessage());
                return false;
        }
    }
    
    public static boolean isEmpty(String value){
        return value == null || value.trim().isEmpty();
    }
}
