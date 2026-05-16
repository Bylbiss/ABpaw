/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PemesananDAO;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PemesananController {
    private PemesananDAO pemesananDAO;

    public PemesananController() {
        pemesananDAO = new PemesananDAO();
    }

    // Online
    public boolean createPemesananOnline(PemesananOnline pesan) {
        return pemesananDAO.insertOnline(pesan);
    }

    public boolean updatePemesananOnline(PemesananOnline pesan) {
        return pemesananDAO.updateOnline(pesan);
    }

    public boolean deletePemesananOnline(int id) {
        return pemesananDAO.deleteOnline(id);
    }

    public PemesananOnline getPemesananOnlineById(int id) {
        return pemesananDAO.getOnlineById(id);
    }

    public List<PemesananOnline> getAllPemesananOnline() {
        return pemesananDAO.getAllOnline();
    }

    public List<PemesananOnline> getPemesananOnlineByPemilik(int idPemilik) {
        return pemesananDAO.getOnlineByPemilik(idPemilik);
    }

    public List<PemesananOnline> getPemesananOnlineByDokter(int idDokter) {
        List<PemesananOnline> all = pemesananDAO.getAllOnline();
        if (all == null) return new ArrayList<>();
        return all.stream()
                  .filter(p -> p.getIdDokter() == idDokter)
                  .collect(Collectors.toList());
    }

    public boolean updateStatusPemesananOnline(int id, String statusBaru) {
        PemesananOnline po = getPemesananOnlineById(id);
        if (po == null) return false;
        po.setStatus(statusBaru);
        return pemesananDAO.updateOnline(po);
    }

    // Offline
    public boolean createPemesananOffline(PemesananOffline pesan) {
        return pemesananDAO.insertOffline(pesan);
    }

    public boolean updatePemesananOffline(PemesananOffline pesan) {
        return pemesananDAO.updateOffline(pesan);
    }

    public boolean deletePemesananOffline(int id) {
        return pemesananDAO.deleteOffline(id);
    }

    public PemesananOffline getPemesananOfflineById(int id) {
        return pemesananDAO.getOfflineById(id);
    }

    public List<PemesananOffline> getAllPemesananOffline() {
        return pemesananDAO.getAllOffline();
    }

    public List<PemesananOffline> getPemesananOfflineByPemilik(int idPemilik) {
        return pemesananDAO.getOfflineByPemilik(idPemilik);
    }

    public List<PemesananOffline> getPemesananOfflineByDokter(int idDokter) {
        List<PemesananOffline> all = pemesananDAO.getAllOffline();
        all.removeIf(p -> p.getIdDokter() != idDokter);
        return all;
    }

    public boolean updateStatusPemesananOffline(int id, String statusBaru) {
        PemesananOffline poff = getPemesananOfflineById(id);
        if (poff == null) return false;
        poff.setStatus(statusBaru);
        return pemesananDAO.updateOffline(poff);
    }
}