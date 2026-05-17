/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Dokter extends User {
    private int idDokter;
    private String namaDepan;
    private String namaBelakang;
    private String alamat;
    private String noHp;
    private String spesialisasi;
    private BigDecimal biayaKonsultasi;
    private String spesiesHewan;      // Simpan sebagai String, bisa diparse nanti
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Dokter() {}

    public Dokter(int idDokter, String username, String password, String email,
                  String namaDepan, String namaBelakang, String alamat, String noHp,
                  String spesialisasi, BigDecimal biayaKonsultasi, String spesiesHewan,
                  Timestamp createdAt, Timestamp updatedAt) {
        super(username, password, email);
        this.idDokter = idDokter;
        this.namaDepan = namaDepan;
        this.namaBelakang = namaBelakang;
        this.alamat = alamat;
        this.noHp = noHp;
        this.spesialisasi = spesialisasi;
        this.biayaKonsultasi = biayaKonsultasi;
        this.spesiesHewan = spesiesHewan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Helper method
    public String getNamaLengkap() {
        return (namaDepan != null ? namaDepan : "") + 
               (namaBelakang != null ? " " + namaBelakang : "");
    }

    // Getter & Setter
    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    public String getNamaDepan() {
        return namaDepan;
    }

    public void setNamaDepan(String namaDepan) {
        this.namaDepan = namaDepan;
    }

    public String getNamaBelakang() {
        return namaBelakang;
    }

    public void setNamaBelakang(String namaBelakang) {
        this.namaBelakang = namaBelakang;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getSpesialisasi() {
        return spesialisasi;
    }

    public void setSpesialisasi(String spesialisasi) {
        this.spesialisasi = spesialisasi;
    }

    public BigDecimal getBiayaKonsultasi() {
        return biayaKonsultasi;
    }

    public void setBiayaKonsultasi(BigDecimal biayaKonsultasi) {
        this.biayaKonsultasi = biayaKonsultasi;
    }

    public String getSpesiesHewan() {
        return spesiesHewan;
    }

    public void setSpesiesHewan(String spesiesHewan) {
        this.spesiesHewan = spesiesHewan;
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
        return "DOKTER";
    }

    @Override
    public int getId() {
        return idDokter;
    }
}