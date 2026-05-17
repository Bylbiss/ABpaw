/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class PemesananOnline extends Transaksi {
    private String kodePemesanan;   // sama dengan kodeTransaksi dari parent
    private LocalDate tanggalKonsultasi;
    private String waktuKonsultasi; // format "HH:MM"
    private Timestamp waktuPemesanan;

    public PemesananOnline() {}

    public PemesananOnline(int idPemesanan, int idPemilik, int idDokter, int idPet,
                           String kodePemesanan, LocalDate tanggalKonsultasi, String waktuKonsultasi,
                           String keluhan, BigDecimal biayaKonsultasi, String kuponDigunakan,
                           BigDecimal jumlahDiskon, BigDecimal totalBiaya, String statusPemesanan,
                           Timestamp waktuPemesanan, Timestamp createdAt, Timestamp updatedAt) {
        this.idTransaksi = idPemesanan;
        this.idPemilik = idPemilik;
        this.idDokter = idDokter;
        this.idPet = idPet;
        this.kodeTransaksi = kodePemesanan;
        this.keluhan = keluhan;
        this.biayaKonsultasi = biayaKonsultasi;
        this.kuponDigunakan = kuponDigunakan;
        this.jumlahDiskon = jumlahDiskon;
        this.totalBiaya = totalBiaya;
        this.status = statusPemesanan;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
        this.kodePemesanan = kodePemesanan;
        this.tanggalKonsultasi = tanggalKonsultasi;
        this.waktuKonsultasi = waktuKonsultasi;
        this.waktuPemesanan = waktuPemesanan;
    }

    // Getter & Setter spesifik
    public String getKodePemesanan() {
        return kodePemesanan;
    }

    public void setKodePemesanan(String kodePemesanan) {
        this.kodePemesanan = kodePemesanan;
        this.kodeTransaksi = kodePemesanan;
    }

    public LocalDate getTanggalKonsultasi() {
        return tanggalKonsultasi;
    }

    public void setTanggalKonsultasi(LocalDate tanggalKonsultasi) {
        this.tanggalKonsultasi = tanggalKonsultasi;
    }

    public String getWaktuKonsultasi() {
        return waktuKonsultasi;
    }

    public void setWaktuKonsultasi(String waktuKonsultasi) {
        this.waktuKonsultasi = waktuKonsultasi;
    }

    public Timestamp getWaktuPemesanan() {
        return waktuPemesanan;
    }

    public void setWaktuPemesanan(Timestamp waktuPemesanan) {
        this.waktuPemesanan = waktuPemesanan;
    }

    @Override
    public String getTipePemesanan() {
        return "ONLINE";
    }
}