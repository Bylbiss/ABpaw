/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PemilikDAO;
import abpaw.model.entity.Pemilik;
import java.util.List;
import java.util.stream.Collectors;

public class PemilikController {
    private PemilikDAO pemilikDAO;

    public PemilikController() {
        pemilikDAO = new PemilikDAO();
    }

    public List<Pemilik> getAllPemilik() {
        return pemilikDAO.getAll();
    }

    public Pemilik getPemilikById(int id) {
        return pemilikDAO.getById(id);
    }

    public boolean updatePemilik(Pemilik pemilik) {
        return pemilikDAO.update(pemilik);
    }

    public boolean deletePemilik(int id) {
        return pemilikDAO.delete(id);
    }

    public List<Pemilik> searchPemilik(String keyword) {
        return pemilikDAO.getAll().stream()
                .filter(p -> p.getNamaPemilik().toLowerCase().contains(keyword.toLowerCase()) ||
                             p.getUsername().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }
}