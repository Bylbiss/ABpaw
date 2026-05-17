/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

public class Obat {
    private int idObat;
    private String namaObat;
    private String deskripsiObat;
    private String bentukObat;   // tablet, kapsul, suntik, salep, cair
    private BigDecimal harga;
    private int stok;
    private int stokMinimal;
    private Date tglKadaluarsa;
    private boolean perluResep;   // 1 = true, 0 = false
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Obat() {}

    public Obat(int idObat, String namaObat, String deskripsiObat, String bentukObat,
                BigDecimal harga, int stok, int stokMinimal, Date tglKadaluarsa,
                boolean perluResep, Timestamp createdAt, Timestamp updatedAt) {
        this.idObat = idObat;
        this.namaObat = namaObat;
        this.deskripsiObat = deskripsiObat;
        this.bentukObat = bentukObat;
        this.harga = harga;
        this.stok = stok;
        this.stokMinimal = stokMinimal;
        this.tglKadaluarsa = tglKadaluarsa;
        this.perluResep = perluResep;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Helper
    public boolean isStokHampirHabis() {
        return stok <= stokMinimal;
    }

    public boolean isKadaluarsa() {
        if (tglKadaluarsa == null) return false;
        return tglKadaluarsa.toLocalDate().isBefore(java.time.LocalDate.now());
    }

    // Getter & Setter
    public int getIdObat() {
        return idObat;
    }

    public void setIdObat(int idObat) {
        this.idObat = idObat;
    }

    public String getNamaObat() {
        return namaObat;
    }

    public void setNamaObat(String namaObat) {
        this.namaObat = namaObat;
    }

    public String getDeskripsiObat() {
        return deskripsiObat;
    }

    public void setDeskripsiObat(String deskripsiObat) {
        this.deskripsiObat = deskripsiObat;
    }

    public String getBentukObat() {
        return bentukObat;
    }

    public void setBentukObat(String bentukObat) {
        this.bentukObat = bentukObat;
    }

    public BigDecimal getHarga() {
        return harga;
    }

    public void setHarga(BigDecimal harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public int getStokMinimal() {
        return stokMinimal;
    }

    public void setStokMinimal(int stokMinimal) {
        this.stokMinimal = stokMinimal;
    }

    public Date getTglKadaluarsa() {
        return tglKadaluarsa;
    }

    public void setTglKadaluarsa(Date tglKadaluarsa) {
        this.tglKadaluarsa = tglKadaluarsa;
    }

    public boolean isPerluResep() {
        return perluResep;
    }

    public void setPerluResep(boolean perluResep) {
        this.perluResep = perluResep;
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
}