/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public abstract class Transaksi {
    protected int idTransaksi;      // Bisa diisi dari id_pemesanan atau id_antrean
    protected int idPemilik;
    protected int idDokter;
    protected int idPet;
    protected String kodeTransaksi; // kode_pemesanan atau nomor_antrean
    protected String keluhan;
    protected BigDecimal biayaKonsultasi;
    protected String kuponDigunakan;
    protected BigDecimal jumlahDiskon;
    protected BigDecimal totalBiaya;
    protected String status;
    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    public Transaksi() {}

    // Getter dan Setter (Encapsulation)
    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public int getIdPemilik() {
        return idPemilik;
    }

    public void setIdPemilik(int idPemilik) {
        this.idPemilik = idPemilik;
    }

    public int getIdDokter() {
        return idDokter;
    }

    public void setIdDokter(int idDokter) {
        this.idDokter = idDokter;
    }

    public int getIdPet() {
        return idPet;
    }

    public void setIdPet(int idPet) {
        this.idPet = idPet;
    }

    public String getKodeTransaksi() {
        return kodeTransaksi;
    }

    public void setKodeTransaksi(String kodeTransaksi) {
        this.kodeTransaksi = kodeTransaksi;
    }

    public String getKeluhan() {
        return keluhan;
    }

    public void setKeluhan(String keluhan) {
        this.keluhan = keluhan;
    }

    public BigDecimal getBiayaKonsultasi() {
        return biayaKonsultasi;
    }

    public void setBiayaKonsultasi(BigDecimal biayaKonsultasi) {
        this.biayaKonsultasi = biayaKonsultasi;
    }

    public String getKuponDigunakan() {
        return kuponDigunakan;
    }

    public void setKuponDigunakan(String kuponDigunakan) {
        this.kuponDigunakan = kuponDigunakan;
    }

    public BigDecimal getJumlahDiskon() {
        return jumlahDiskon;
    }

    public void setJumlahDiskon(BigDecimal jumlahDiskon) {
        this.jumlahDiskon = jumlahDiskon;
    }

    public BigDecimal getTotalBiaya() {
        return totalBiaya;
    }

    public void setTotalBiaya(BigDecimal totalBiaya) {
        this.totalBiaya = totalBiaya;
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

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Method abstrak yang harus diimplementasikan subclass
    public abstract String getTipePemesanan();
}