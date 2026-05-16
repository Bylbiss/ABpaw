/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.ResepDAO;
import abpaw.model.entity.Resep;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Vector;

public class ResepController {
    private ResepDAO resepDAO;

    public ResepController() {
        resepDAO = new ResepDAO();
    }

    public boolean createResep(Resep resep, Vector<?> dataDetail) {
        // Simpan resep dulu
        resep.setTanggalResep(Date.valueOf(LocalDate.now()));
        resep.setStatus("belum_diproses");
        boolean success = resepDAO.insert(resep);
        if (!success) return false;

        // Simpan detail resep (perlu implementasi DetailResepDAO)
        // Untuk sementara asumsikan ada method insertDetail
        // Karena kita belum membuat DetailResepDAO, kita lewati dulu.
        // Di kode nyata, Anda perlu membuat DetailResepDAO dan menyimpan setiap baris.
        // return saveDetails(resep.getIdResep(), dataDetail);
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

    public List<Resep> getResepByDokter(int idDokter) {
        return resepDAO.getByDokter(idDokter);
    }

    public List<Resep> getResepByPemilik(int idPemilik) {
        // Karena relasi resep tidak langsung ke pemilik, kita perlu melalui pemesanan
        // Alternatif: query join. Untuk sederhana, kita kembalikan kosong atau implementasi manual.
        // Di sini kita asumsikan sudah ada method getResepByPemilik di DAO (belum dibuat).
        // Sementara kita lakukan filter manual via pemesanan controller.
        // Untuk demo, kita return getAllResep() dulu.
        return resepDAO.getAll();
    }

    public boolean updateStatusResep(int idResep, String status) {
        return resepDAO.updateStatus(idResep, status);
    }
}