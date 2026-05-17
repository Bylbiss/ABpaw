/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Pets {
    private int idPet;
    private int idPemilik;
    private String namaPet;
    private String jenisKelamin;   // 'jantan', 'betina', 'tidak_diketahui'
    private String jenisHewan;     // enum dari database
    private String ras;
    private Date tanggalLahir;
    private Integer usia;          // bisa null, dihitung otomatis atau input manual
    private BigDecimal berat;
    private String sterilisasi;    // 'sudah', 'belum'
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Pets() {}

    public Pets(int idPet, int idPemilik, String namaPet, String jenisKelamin,
                String jenisHewan, String ras, Date tanggalLahir, Integer usia,
                BigDecimal berat, String sterilisasi, Timestamp createdAt, Timestamp updatedAt) {
        this.idPet = idPet;
        this.idPemilik = idPemilik;
        this.namaPet = namaPet;
        this.jenisKelamin = jenisKelamin;
        this.jenisHewan = jenisHewan;
        this.ras = ras;
        this.tanggalLahir = tanggalLahir;
        this.usia = usia;
        this.berat = berat;
        this.sterilisasi = sterilisasi;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getter & Setter
    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public int getIdPemilik() {
        return idPemilik;
    }

    public void setIdPemilik(int idPemilik) {
        this.idPemilik = idPemilik;
    }

    public String getNamaPet() {
        return namaPet;
    }

    public void setNamaPet(String namaPet) {
        this.namaPet = namaPet;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getJenisHewan() {
        return jenisHewan;
    }

    public void setJenisHewan(String jenisHewan) {
        this.jenisHewan = jenisHewan;
    }

    public String getRas() {
        return ras;
    }

    public void setRas(String ras) {
        this.ras = ras;
    }

    public Date getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(Date tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public Integer getUsia() {
        return usia;
    }

    public void setUsia(Integer usia) {
        this.usia = usia;
    }

    public BigDecimal getBerat() {
        return berat;
    }

    public void setBerat(BigDecimal berat) {
        this.berat = berat;
    }

    public String getSterilisasi() {
        return sterilisasi;
    }

    public void setSterilisasi(String sterilisasi) {
        this.sterilisasi = sterilisasi;
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
    public String toString() {
        return namaPet + " (" + jenisHewan + ")";
    }
}