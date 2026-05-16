/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.sql.Timestamp;

public class Pemilik extends User {
    private int idPemilik;
    private String namaPemilik;
    private String noHp;
    private String alamat;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Pemilik() {}

    public Pemilik(int idPemilik, String username, String password, String email,
                   String namaPemilik, String noHp, String alamat,
                   Timestamp createdAt, Timestamp updatedAt) {
        super(username, password, email);
        this.idPemilik = idPemilik;
        this.namaPemilik = namaPemilik;
        this.noHp = noHp;
        this.alamat = alamat;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter & Setter
    public int getIdPemilik() {
        return idPemilik;
    }

    public void setIdPemilik(int idPemilik) {
        this.idPemilik = idPemilik;
    }

    public String getNamaPemilik() {
        return namaPemilik;
    }

    public void setNamaPemilik(String namaPemilik) {
        this.namaPemilik = namaPemilik;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String getRole() {
        return "PEMILIK";
    }
    
    @Override
    public int getId() {
        return idPemilik;
    }
}