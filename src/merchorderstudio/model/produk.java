/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.model;

/**
 *
 * @author Mahesa
 */
public class produk {
 
    private int idProduk;
    private String namaProduk;
    private String kategori;
    private double harga;
    private int stok;
    private String ukuran;
    private String deskripsi;
    private byte[] fotoProduk; // blob langsung dari kolom foto_produk
 
    public produk() {
    }
 
    public produk(int idProduk, String namaProduk, String kategori, double harga,
                  int stok, String ukuran, String deskripsi, byte[] fotoProduk) {
        this.idProduk = idProduk;
        this.namaProduk = namaProduk;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.ukuran = ukuran;
        this.deskripsi = deskripsi;
        this.fotoProduk = fotoProduk;
    }
 
    public int getIdProduk() {
        return idProduk;
    }
 
    public void setIdProduk(int idProduk) {
        this.idProduk = idProduk;
    }
 
    public String getNamaProduk() {
        return namaProduk;
    }
 
    public void setNamaProduk(String namaProduk) {
        this.namaProduk = namaProduk;
    }
 
    public String getKategori() {
        return kategori;
    }
 
    public void setKategori(String kategori) {
        this.kategori = kategori;
    }
 
    public double getHarga() {
        return harga;
    }
 
    public void setHarga(double harga) {
        this.harga = harga;
    }
 
    public int getStok() {
        return stok;
    }
 
    public void setStok(int stok) {
        this.stok = stok;
    }
 
    public String getUkuran() {
        return ukuran;
    }
 
    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }
 
    public String getDeskripsi() {
        return deskripsi;
    }
 
    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
 
    public byte[] getFotoProduk() {
        return fotoProduk;
    }
 
    public void setFotoProduk(byte[] fotoProduk) {
        this.fotoProduk = fotoProduk;
    }
 
    /** Format harga 
     * @return  */
    public String getHargaFormatted() {
        java.text.NumberFormat nf = java.text.NumberFormat.getInstance(new java.util.Locale("in", "ID"));
        return "Rp" + nf.format(harga);
    }
}
