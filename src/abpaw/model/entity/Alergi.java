/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.sql.Timestamp;

public class Alergi {
    private int idAlergi;
    private int idPet;
    private int idObat;
    private String namaAlergi;
    private String createdByType; // "pemilik" atau "dokter"
    private int createdById;
    private String status;        // "usulan" atau "terverifikasi"
    private Integer verifiedBy;   // id_admin
    private Timestamp verifiedAt;
    private Timestamp createdAt;

    public Alergi() {}

    public Alergi(int idAlergi, int idPet, int idObat, String namaAlergi,
                     String createdByType, int createdById, String status,
                     Integer verifiedBy, Timestamp verifiedAt, Timestamp createdAt) {
        this.idAlergi = idAlergi;
        this.idPet = idPet;
        this.idObat = idObat;
        this.namaAlergi = namaAlergi;
        this.createdByType = createdByType;
        this.createdById = createdById;
        this.status = status;
        this.verifiedBy = verifiedBy;
        this.verifiedAt = verifiedAt;
        this.createdAt = createdAt;
    }

    // Getter & Setter
    public int getIdAlergi() { return idAlergi; }
    public void setIdAlergi(int idAlergi) { this.idAlergi = idAlergi; }
    public int getIdPet() { return idPet; }
    public void setIdPet(int idPet) { this.idPet = idPet; }
    public int getIdObat() { return idObat; }
    public void setIdObat(int idObat) { this.idObat = idObat; }
    public String getNamaAlergi() { return namaAlergi; }
    public void setNamaAlergi(String namaAlergi) { this.namaAlergi = namaAlergi; }
    public String getCreatedByType() { return createdByType; }
    public void setCreatedByType(String createdByType) { this.createdByType = createdByType; }
    public int getCreatedById() { return createdById; }
    public void setCreatedById(int createdById) { this.createdById = createdById; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getVerifiedBy() { return verifiedBy; }
    public void setVerifiedBy(Integer verifiedBy) { this.verifiedBy = verifiedBy; }
    public Timestamp getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Timestamp verifiedAt) { this.verifiedAt = verifiedAt; }
    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}