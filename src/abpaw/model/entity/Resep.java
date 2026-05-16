/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Resep {
    private int idResep;
    private int idPemesanan;        // bisa dari online atau offline (id_pemesanan atau id_antrean)
    private String tipePemesanan;   // 'online' atau 'offline'
    private int idDokter;
    private Integer idApoteker;     // bisa null
    private Date tanggalResep;
    private String status;          // 'belum_diproses', 'diproses', 'selesai'
    private Timestamp createdAt;
    
    // Relasi one-to-many dengan detail resep (tidak disimpan langsung, untuk logika bisnis)
    private List<DetailResep> detailResepList;

    public Resep() {
        detailResepList = new ArrayList<>();
    }

    public Resep(int idResep, int idPemesanan, String tipePemesanan, int idDokter,
                 Integer idApoteker, Date tanggalResep, String status, Timestamp createdAt) {
        this.idResep = idResep;
        this.idPemesanan = idPemesanan;
        this.tipePemesanan = tipePemesanan;
        this.idDokter = idDokter;
        this.idApoteker = idApoteker;
        this.tanggalResep = tanggalResep;
        this.status = status;
        this.createdAt = createdAt;
        this.detailResepList = new ArrayList<>();
    }

    // Helper
    public void addDetailResep(DetailResep detail) {
        detailResepList.add(detail);
    }

    // Getter & Setter
    public int getIdResep() {
        return idResep;
    }

    public void setIdResep(int idResep) {
        this.idResep = idResep;
    }

    public int getIdPemesanan() {
        return idPemesanan;
    }

    public void setIdPemesanan(int idPemesanan) {
        this.idPemesanan = idPemesanan;
    }

    public String getTipePemesanan() {
        return tipePemesanan;
    }

    public void setTipePemesanan(String tipePemesanan) {
        this.tipePemesanan = tipePemesanan;
    }

    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    public Integer getIdApoteker() {
        return idApoteker;
    }

    public void setIdApoteker(Integer idApoteker) {
        this.idApoteker = idApoteker;
    }

    public Date getTanggalResep() {
        return tanggalResep;
    }

    public void setTanggalResep(Date tanggalResep) {
        this.tanggalResep = tanggalResep;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public List<DetailResep> getDetailResepList() {
        return detailResepList;
    }

    public void setDetailResepList(List<DetailResep> detailResepList) {
        this.detailResepList = detailResepList;
    }

    public int getIdPemesanan() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}