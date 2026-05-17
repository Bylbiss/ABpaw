/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PembelianObat {
    private int idPembelian;
    private int idPemilik;
    private int idObat;
    private int jumlah;
    private BigDecimal hargaSatuan;
    private BigDecimal totalHarga;
    private Timestamp tanggal;
    private String status;
    private String kodeTransaksi;

    public PembelianObat() {}

    // Getter dan Setter
    public int getIdPembelian() { 
        return idPembelian; 
    }
    
    public void setIdPembelian(int idPembelian) { 
        this.idPembelian = idPembelian; 
    }
    
    public int getIdPemilik() { 
        return idPemilik; 
    }
    
    public void setIdPemilik(int idPemilik) { 
        this.idPemilik = idPemilik; 
    }
    
    public int getIdObat() { 
        return idObat; 
    }
    
    public void setIdObat(int idObat) { 
        this.idObat = idObat; 
    }
    
    public int getJumlah() { 
        return jumlah; 
    }
    
    public void setJumlah(int jumlah) { 
        this.jumlah = jumlah; 
    }
    
    public BigDecimal getHargaSatuan() { 
        return hargaSatuan; 
    }
    
    public void setHargaSatuan(BigDecimal hargaSatuan) { 
        this.hargaSatuan = hargaSatuan; 
    }
    
    public BigDecimal getTotalHarga() { 
        return totalHarga; 
    }
    
    public void setTotalHarga(BigDecimal totalHarga) { 
        this.totalHarga = totalHarga; 
    }
    
    public Timestamp getTanggal() { 
        return tanggal; 
    }
    
    public void setTanggal(Timestamp tanggal) { 
        this.tanggal = tanggal; 
    }
    
    public String getStatus() { 
        return status; 
    }
    
    public void setStatus(String status) { 
        this.status = status; 
    }
    
    public String getKodeTransaksi() { 
        return kodeTransaksi; 
    }
    
    public void setKodeTransaksi(String kodeTransaksi) { 
        this.kodeTransaksi = kodeTransaksi; 
    }
}