/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package merchorderstudio.model;

/**
 *
 * @author Mahesa
 */
public class User {
 
    private int idUser;
    private String nama;
    private String email;
    private String password;
    private String noTelp;
    private String alamat;
    private String role;
 
    public int getIdUser() {
        return idUser;
    }
 
    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }
 
    public String getNama() {
        return nama;
    }
 
    public void setNama(String nama) {
        this.nama = nama;
    }
 
    public String getEmail() {
        return email;
    }
 
    public void setEmail(String email) {
        this.email = email;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }
 
    public String getNoTelp() {
        return noTelp;
    }
 
    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }
 
    public String getAlamat() {
        return alamat;
    }
 
    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
 
    public String getRole() {
        return role;
    }
 
    public void setRole(String role) {
        this.role = role;
    }
}