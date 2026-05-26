/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
/**
 *
 * @author LOQ
 */
public class DetailBiayaOffline {
    private int idDetail;
    private int idAntrean;
    private String namaBiaya;
    private int jumlah;
    private BigDecimal hargaSatuan;
    private BigDecimal subtotal;
    private Timestamp createdAt;
    
    public DetailBiayaOffline() {}
    
    // Getter dan Setter
    public int getIdDetail() {
        return idDetail;
    }
    
    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }
    
    public int getIdAntrean() {
        return idAntrean;
    }
    
    public void setIdAntrean(int idAntrean) {
        this.idAntrean = idAntrean;
    }
    
    public String getNamaBiaya() {
        return namaBiaya;
    }
    
    public void setNamaBiaya(String namaBiaya) {
        this.namaBiaya = namaBiaya;
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
    
    public BigDecimal getSubtotal() {
        return subtotal;
    }
    
    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
