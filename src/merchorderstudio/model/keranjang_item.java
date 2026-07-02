/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.model;

/**
 *
 * @author Mahesa
 */
public class keranjang_item {
 
    private int idKeranjang;
    private int idUser;
    private int jumlah;
    private String ukuran;
    private String uploadDesain;
    private String catatan;
    private produk produk; // detail produknya, hasil JOIN
 
    public keranjang_item() {
    }
 
    public int getIdKeranjang() {
        return idKeranjang;
    }
 
    public void setIdKeranjang(int idKeranjang) {
        this.idKeranjang = idKeranjang;
    }
 
    public int getIdUser() {
        return idUser;
    }
 
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
 
    public int getJumlah() {
        return jumlah;
    }
 
    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }
 
    public String getUkuran() {
        return ukuran;
    }
 
    public void setUkuran(String ukuran) {
        this.ukuran = ukuran;
    }
 
    public String getUploadDesain() {
        return uploadDesain;
    }
 
    public void setUploadDesain(String uploadDesain) {
        this.uploadDesain = uploadDesain;
    }
 
    public String getCatatan() {
        return catatan;
    }
 
    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }
 
    public merchorderstudio.model.produk getProduk() {
        return produk;
    }
 
    public void setProduk(merchorderstudio.model.produk produk) {
        this.produk = produk;
    }
 
    /** Subtotal = harga produk x jumlah */
    public double getSubtotal() {
        if (produk == null) return 0;
        return produk.getHarga() * jumlah;
    }
}