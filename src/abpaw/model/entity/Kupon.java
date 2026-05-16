/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.math.RoundingMode;

public class Kupon {
    private int idKupon;
    private String kode;
    private String deskripsi;
    private BigDecimal diskonPersen;
    private BigDecimal diskonMaks;
    private BigDecimal minimalPembelian;
    private Date berlakuHingga;
    private boolean aktif;
    private Timestamp createdAt;

    public Kupon() {}

    public Kupon(int idKupon, String kode, String deskripsi,
                 BigDecimal diskonPersen, BigDecimal diskonMaks,
                 BigDecimal minimalPembelian, Date berlakuHingga,
                 boolean aktif, Timestamp createdAt) {
        this.idKupon = idKupon;
        this.kode = kode;
        this.deskripsi = deskripsi;
        this.diskonPersen = diskonPersen;
        this.diskonMaks = diskonMaks;
        this.minimalPembelian = minimalPembelian;
        this.berlakuHingga = berlakuHingga;
        this.aktif = aktif;
        this.createdAt = createdAt;
    }

    // Polymorphism: hitung potongan berdasarkan total belanja
    public BigDecimal hitungPotongan(BigDecimal totalSebelumDiskon) {
        if (!isValid()) {
        System.out.println("Kupon tidak aktif atau sudah kadaluarsa");
        return BigDecimal.ZERO;
    }
    
    // Cek minimal pembelian
        if (totalSebelumDiskon.compareTo(minimalPembelian) < 0) {
            System.out.println("Minimal belanja Rp " + minimalPembelian + " belum terpenuhi");
            return BigDecimal.ZERO;
        }
    
    // HITUNG POTONGAN = total × (persen / 100)
        BigDecimal seratus = new BigDecimal("100");
        BigDecimal potongan = totalSebelumDiskon
            .multiply(diskonPersen)
            .divide(seratus, 2, java.math.RoundingMode.HALF_UP);
    
    // Batasi dengan maksimal diskon
        if (potongan.compareTo(diskonMaks) > 0) {
        potongan = diskonMaks;
        }   
    
    // Untuk debugging - bisa dihapus nanti
        System.out.println("Total: " + totalSebelumDiskon);
        System.out.println("Diskon %: " + diskonPersen);
        System.out.println("Potongan: " + potongan);
    
        return potongan;
}

    public boolean isValid() {
        if (!aktif) return false;
        if (berlakuHingga == null) return true;
        return berlakuHingga.toLocalDate().isAfter(java.time.LocalDate.now()) ||
               berlakuHingga.toLocalDate().isEqual(java.time.LocalDate.now());
    }

    // Getter & Setter (Encapsulation)
    public int getIdKupon() { return idKupon; }
    public void setIdKupon(int idKupon) { this.idKupon = idKupon; }
    public String getKode() { return kode; }
    public void setKode(String kode) { this.kode = kode; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public BigDecimal getDiskonPersen() { return diskonPersen; }
    public void setDiskonPersen(BigDecimal diskonPersen) { this.diskonPersen = diskonPersen; }
    public BigDecimal getDiskonMaks() { return diskonMaks; }
    public void setDiskonMaks(BigDecimal diskonMaks) { this.diskonMaks = diskonMaks; }
    public BigDecimal getMinimalPembelian() { return minimalPembelian; }
    public void setMinimalPembelian(BigDecimal minimalPembelian) { this.minimalPembelian = minimalPembelian; }
    public Date getBerlakuHingga() { return berlakuHingga; }
    public void setBerlakuHingga(Date berlakuHingga) { this.berlakuHingga = berlakuHingga; }
    public boolean isAktif() { return aktif; }
    public void setAktif(boolean aktif) { this.aktif = aktif; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    @Override
    public String toString() {
        return kode + " - Diskon " + diskonPersen + "% (Maks Rp " + diskonMaks + ")";
    }
}