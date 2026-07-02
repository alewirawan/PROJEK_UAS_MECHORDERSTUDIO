/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.dao;
import config.koneksi;
import merchorderstudio.model.keranjang_item;
import merchorderstudio.model.produk;
 
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
/**
 *
 * @author Mahesa
 */
public class KeranjangDAO {
 
    /**
     * Tambah produk ke keranjang.
     * Kalau produk + ukuran + desain yang sama sudah ada di keranjang user itu,
     * jumlahnya di-UPDATE (nambah), bukan bikin baris baru.
     *
     * @return true kalau berhasil
     */
    public boolean tambahItem(int idUser, int idProduk, int jumlah,
                               String ukuran, String uploadDesain, String catatan) {
 
        String cekSql = "SELECT id_keranjang, jumlah FROM keranjang "
                + "WHERE id_user = ? AND id_produk = ? "
                + "AND ukuran <=> ? AND upload_desain <=> ?";
        // <=> = null-safe equals di MySQL/MariaDB, biar ukuran/desain NULL juga match
 
        try (Connection conn = koneksi.getConnection()) {
 
            try (PreparedStatement cek = conn.prepareStatement(cekSql)) {
                cek.setInt(1, idUser);
                cek.setInt(2, idProduk);
                cek.setString(3, ukuran);
                cek.setString(4, uploadDesain);
 
                try (ResultSet rs = cek.executeQuery()) {
                    if (rs.next()) {
                        // sudah ada -> update jumlah
                        int idKeranjang = rs.getInt("id_keranjang");
                        int jumlahLama = rs.getInt("jumlah");
                        return updateJumlah(idKeranjang, jumlahLama + jumlah);
                    }
                }
            }
 
            // belum ada -> insert baris baru
            String insertSql = "INSERT INTO keranjang "
                    + "(id_user, id_produk, jumlah, ukuran, upload_desain, catatan) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
 
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, idUser);
                ps.setInt(2, idProduk);
                ps.setInt(3, jumlah);
                ps.setString(4, ukuran);
                ps.setString(5, uploadDesain);
                ps.setString(6, catatan);
                return ps.executeUpdate() > 0;
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // Ambil semua isi keranjang milik satu user, sudah lengkap dengan data produknya. 
    public List<keranjang_item> getKeranjangByUser(int idUser) {
        List<keranjang_item> list = new ArrayList<>();
 
        String sql = "SELECT k.id_keranjang, k.id_user, k.jumlah, k.ukuran, "
                + "k.upload_desain, k.catatan, "
                + "p.id_produk, p.nama_produk, p.kategori, p.harga, p.stok, "
                + "p.ukuran AS ukuran_produk, p.deskripsi, p.foto_produk "
                + "FROM keranjang k "
                + "JOIN produk p ON k.id_produk = p.id_produk "
                + "WHERE k.id_user = ? "
                + "ORDER BY k.id_keranjang DESC";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idUser);
 
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    keranjang_item item = new keranjang_item();
                    item.setIdKeranjang(rs.getInt("id_keranjang"));
                    item.setIdUser(rs.getInt("id_user"));
                    item.setJumlah(rs.getInt("jumlah"));
                    item.setUkuran(rs.getString("ukuran"));
                    item.setUploadDesain(rs.getString("upload_desain"));
                    item.setCatatan(rs.getString("catatan"));
 
                    produk p = new produk();
                    p.setIdProduk(rs.getInt("id_produk"));
                    p.setNamaProduk(rs.getString("nama_produk"));
                    p.setKategori(rs.getString("kategori"));
                    p.setHarga(rs.getDouble("harga"));
                    p.setStok(rs.getInt("stok"));
                    p.setUkuran(rs.getString("ukuran_produk"));
                    p.setDeskripsi(rs.getString("deskripsi"));
                    p.setFotoProduk(rs.getBytes("foto_produk"));
                    item.setProduk(p);
 
                    list.add(item);
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
 
    // Total baris di keranjang milik satu user (buat badge "Keranjang: N item").
    public int getJumlahItem(int idUser) {
        String sql = "SELECT COUNT(*) AS total FROM keranjang WHERE id_user = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idUser);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total");
                }
            }
 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
 
    public boolean updateJumlah(int idKeranjang, int jumlahBaru) {
        String sql = "UPDATE keranjang SET jumlah = ? WHERE id_keranjang = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, jumlahBaru);
            ps.setInt(2, idKeranjang);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    public boolean hapusItem(int idKeranjang) {
        String sql = "DELETE FROM keranjang WHERE id_keranjang = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idKeranjang);
            return ps.executeUpdate() > 0;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
 
    // Kosongkan keranjang user, biasanya dipanggil setelah checkout sukses.
    public boolean kosongkanKeranjang(int idUser) {
        String sql = "DELETE FROM keranjang WHERE id_user = ?";
 
        try (Connection conn = koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, idUser);
            ps.executeUpdate();
            return true;
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}