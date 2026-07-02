/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.dao;
import config.koneksi; // package config, class name "koneksi" (huruf kecil, sesuai nama file)
import merchorderstudio.model.produk;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Mahesa
 */
public class ProdukDAO {
 
    /**
     * Ambil produk untuk ditampilkan di grid "Produk Terpopuler".
     * kategori = null atau "Semua" -> ambil semua produk.
     * limit = 0 -> tanpa batas (dipakai untuk "Lihat Semua").
     */
    public List<produk> getProduk(String kategori, int limit) {
        List<produk> list = new ArrayList<>();
        boolean semua = (kategori == null || kategori.equalsIgnoreCase("Semua"));
 
        String sql = "SELECT id_produk, nama_produk, kategori, harga, stok, ukuran, deskripsi, foto_produk "
                + "FROM produk "
                + (semua ? "" : "WHERE kategori = ? ")
                + "ORDER BY id_produk DESC"
                + (limit > 0 ? " LIMIT " + limit : "");
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            if (!semua) {
                ps.setString(1, kategori);
            }
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 
    // Dipakai untuk fitur search (btnSearch / txtSearch) 
    public List<produk> cariProduk(String keyword) {
        List<produk> list = new ArrayList<>();
        String sql = "SELECT id_produk, nama_produk, kategori, harga, stok, ukuran, deskripsi, foto_produk "
                + "FROM produk WHERE nama_produk LIKE ? ORDER BY nama_produk ASC";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, "%" + keyword + "%");
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 
    public produk getProdukById(int idProduk) {
        String sql = "SELECT id_produk, nama_produk, kategori, harga, stok, ukuran, deskripsi, foto_produk "
                + "FROM produk WHERE id_produk = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idProduk);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
 
    private produk mapRow(ResultSet rs) throws SQLException {
        produk p = new produk();
        p.setIdProduk(rs.getInt("id_produk"));
        p.setNamaProduk(rs.getString("nama_produk"));
        p.setKategori(rs.getString("kategori"));
        p.setHarga(rs.getDouble("harga"));
        p.setStok(rs.getInt("stok"));
        p.setUkuran(rs.getString("ukuran"));
        p.setDeskripsi(rs.getString("deskripsi"));
        p.setFotoProduk(rs.getBytes("foto_produk"));
        return p;
    }
}
