/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.dao;

import config.koneksi;
import merchorderstudio.model.User;
 
import java.sql.*;
 

/**
 *
 * @author Mahesa
 */
public class UserDAO {
 
    /** Ambil data satu user langsung dari database (dipakai buat isi form profile_account). */
    public User getUserById(int idUser) {
        String sql = "SELECT id_user, nama, email, password, no_telp, alamat, role "
                + "FROM users WHERE id_user = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idUser);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User u = new User();
                    u.setIdUser(rs.getInt("id_user"));
                    u.setNama(rs.getString("nama"));
                    u.setEmail(rs.getString("email"));
                    u.setPassword(rs.getString("password"));
                    u.setNoTelp(rs.getString("no_telp"));
                    u.setAlamat(rs.getString("alamat"));
                    u.setRole(rs.getString("role"));
                    return u;
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    /** Update data profile. Semua kolom dikirim ulang, jadi field yang tidak diubah tetap sama nilainya. */
    public boolean updateUser(int idUser, String nama, String alamat, String noTelp, String email, String password) {
        String sql = "UPDATE users SET nama = ?, alamat = ?, no_telp = ?, email = ?, password = ? "
                + "WHERE id_user = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, nama);
            ps.setString(2, alamat);
            ps.setString(3, noTelp);
            ps.setString(4, email);
            ps.setString(5, password);
            ps.setInt(6, idUser);
 
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
 
