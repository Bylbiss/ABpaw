/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.DetailResepDAO;
import abpaw.model.dao.ResepDAO;
import abpaw.model.entity.DetailResep;
import abpaw.model.entity.Resep;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class ResepController {
    private ResepDAO resepDAO;
    private DetailResepDAO detailResepDAO;

    public ResepController() {
        resepDAO = new ResepDAO();
        detailResepDAO = new DetailResepDAO();
    }

    public boolean createResep(Resep resep, Vector<?> dataDetail) {
        // Simpan resep utama
        resep.setTanggalResep(Date.valueOf(LocalDate.now()));
        resep.setStatus("belum_diproses");
        boolean success = resepDAO.insert(resep);
        if (!success) return false;

        // Setelah resep tersimpan, idResep sudah terisi
        int idResep = resep.getIdResep();

        // Loop dataDetail (Vector berisi baris-baris object array)
        for (Object rowObj : dataDetail) {
            Vector<?> row = (Vector<?>) rowObj;
            int idObat = (int) row.get(0);
            String takaran = (String) row.get(2);
            int jumlah = (int) row.get(3);

            DetailResep detail = new DetailResep();
            detail.setIdResep(idResep);
            detail.setIdObat(idObat);
            detail.setTakaran(takaran);
            detail.setJumlah(jumlah);
            detailResepDAO.insert(detail);
        }
        return true;
    }

    public boolean updateResep(Resep resep) {
        return resepDAO.update(resep);
    }

    public boolean deleteResep(int id) {
        return resepDAO.delete(id);
    }

    public Resep getResepById(int id) {
        return resepDAO.getById(id);
    }

    public List<Resep> getAllResep() {
        return resepDAO.getAll();
    }
    
    public List<Resep> getResepByPemilik(int idPemilik) {
        return resepDAO.getResepByPemilik(idPemilik);
    }

    public List<Resep> getResepByDokter(int idDokter) {
        return resepDAO.getByDokter(idDokter);
    }

    public boolean updateStatusResep(int idResep, String status) {
        return resepDAO.updateStatus(idResep, status);
    }
}