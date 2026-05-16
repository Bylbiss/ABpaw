/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class PemesananOffline extends Transaksi {
    private String nomorAntrean;      // sama dengan kodeTransaksi dari parent
    private LocalDate tanggalAntrean;
    private LocalTime waktuAntrean;
    private LocalTime estimasiWaktu;
    private String keluhan;           // sudah ada di parent, tapi di tabel sendiri

    public PemesananOffline() {}

    public PemesananOffline(int idAntrean, int idPemilik, int idDokter, int idPet,
                            String nomorAntrean, LocalDate tanggalAntrean, LocalTime waktuAntrean,
                            String keluhan, String statusAntrean, LocalTime estimasiWaktu,
                            Timestamp createdAt, Timestamp updatedAt) {
        this.idTransaksi = idAntrean;
        this.idPemilik = idPemilik;
        this.idDokter = idDokter;
        this.idPet = idPet;
        this.kodeTransaksi = nomorAntrean;
        this.keluhan = keluhan;
        this.status = statusAntrean;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        
        this.nomorAntrean = nomorAntrean;
        this.tanggalAntrean = tanggalAntrean;
        this.waktuAntrean = waktuAntrean;
        this.estimasiWaktu = estimasiWaktu;
    }

    // Getter & Setter spesifik
    public String getNomorAntrean() {
        return nomorAntrean;
    }

    public void setNomorAntrean(String nomorAntrean) {
        this.nomorAntrean = nomorAntrean;
        this.kodeTransaksi = nomorAntrean;
    }

    public LocalDate getTanggalAntrean() {
        return tanggalAntrean;
    }

    public void setTanggalAntrean(LocalDate tanggalAntrean) {
        this.tanggalAntrean = tanggalAntrean;
    }

    public LocalTime getWaktuAntrean() {
        return waktuAntrean;
    }

    public void setWaktuAntrean(LocalTime waktuAntrean) {
        this.waktuAntrean = waktuAntrean;
    }

    public LocalTime getEstimasiWaktu() {
        return estimasiWaktu;
    }

    public void setEstimasiWaktu(LocalTime estimasiWaktu) {
        this.estimasiWaktu = estimasiWaktu;
    }

    @Override
    public String getTipePemesanan() {
        return "OFFLINE";
    }
}