/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.ObatDAO;
import abpaw.model.entity.Obat;
import java.util.List;

public class ObatController {
    private ObatDAO obatDAO;

    public ObatController() {
        obatDAO = new ObatDAO();
    }

    public List<Obat> getAllObat() {
        return obatDAO.getAll();
    }

    public Obat getObatById(int id) {
        return obatDAO.getById(id);
    }

    public boolean beliObat(int idObat, int jumlah, int idPemilik) {
        Obat o = obatDAO.getById(idObat);
        if (o == null || o.getStok() < jumlah) return false;
        return obatDAO.kurangiStok(idObat, jumlah);
    }
}