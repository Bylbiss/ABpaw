/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.sql.Timestamp;

// Kelas Admin turunan dari User (Inheritance)
public class Admin extends User {

    // Encapsulation: atribut private
    private int idAdmin;
    private String nama;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Admin() {
    }

    public Admin(int idAdmin, String username, String password, String email,
            String nama, Timestamp createdAt, Timestamp updatedAt) {
        super(username, password, email);    // memanggil constructor superclass (User)
        this.idAdmin = idAdmin;
        this.nama = nama;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter & Setter (encapsulation)
    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
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

    // Polymorphism: override method getRole() dari class User
    @Override
    public String getRole() {
        return "ADMIN";
    }

    @Override
    public int getId() {
        return idAdmin;
    }
}
