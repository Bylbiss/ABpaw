/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.entity;

import java.sql.Timestamp;

public class Chat {
    private int idChat;
    private Integer idDokter;
    private Integer idPemilik;
    private String pesan;
    private Timestamp waktu;
    private String statusBaca; // "unread" atau "read"

    public Chat() {}

    public Chat(int idChat, Integer idDokter, Integer idPemilik,
                String pesan, Timestamp waktu, String statusBaca) {
        this.idChat = idChat;
        this.idDokter = idDokter;
        this.idPemilik = idPemilik;
        this.pesan = pesan;
        this.waktu = waktu;
        this.statusBaca = statusBaca;
    }

    public boolean isFromDokter() {
        return idDokter != null && idPemilik == null;
    }

    public boolean isFromPemilik() {
        return idPemilik != null && idDokter == null;
    }

    // Getter & Setter
    public int getIdChat() { 
        return idChat; 
    }
    
    public void setIdChat(int idChat) { 
        this.idChat = idChat; 
    }
    
    public Integer getIdDokter() { 
        return idDokter; 
    }
    
    public void setIdDokter(Integer idDokter) { 
        this.idDokter = idDokter; 
    }
    
    public Integer getIdPemilik() { 
        return idPemilik; 
    }
    
    public void setIdPemilik(Integer idPemilik) { 
        this.idPemilik = idPemilik; 
    }
    
    public String getPesan() { 
        return pesan; 
    }
    
    public void setPesan(String pesan) { 
        this.pesan = pesan; 
    }
    
    public Timestamp getWaktu() { 
        return waktu; 
    }
    
    public void setWaktu(Timestamp waktu) { 
        this.waktu = waktu; 
    }
    
    public String getStatusBaca() { 
        return statusBaca; 
    }
    
    public void setStatusBaca(String statusBaca) { 
        this.statusBaca = statusBaca; 
    }
}