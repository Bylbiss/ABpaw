/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

public class DetailResep {
    private int idDetail;
    private int idResep;
    private int idObat;
    private String takaran;
    private int jumlah;
    private String catatan; // opsional, bisa null

    public DetailResep() {}

    public DetailResep(int idDetail, int idResep, int idObat, String takaran, int jumlah, String catatan) {
        this.idDetail = idDetail;
        this.idResep = idResep;
        this.idObat = idObat;
        this.takaran = takaran;
        this.jumlah = jumlah;
        this.catatan = catatan;
    }

    // Getter dan Setter
    public int getIdDetail() { return idDetail; }
    public void setIdDetail(int idDetail) { this.idDetail = idDetail; }
    public int getIdResep() { return idResep; }
    public void setIdResep(int idResep) { this.idResep = idResep; }
    public int getIdObat() { return idObat; }
    public void setIdObat(int idObat) { this.idObat = idObat; }
    public String getTakaran() { return takaran; }
    public void setTakaran(String takaran) { this.takaran = takaran; }
    public int getJumlah() { return jumlah; }
    public void setJumlah(int jumlah) { this.jumlah = jumlah; }
    public String getCatatan() { return catatan; }
    public void setCatatan(String catatan) { this.catatan = catatan; }
}