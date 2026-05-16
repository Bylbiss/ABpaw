/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.DokterDAO;
import abpaw.model.entity.Dokter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DokterController {
    private DokterDAO dokterDAO;

    public DokterController() {
        dokterDAO = new DokterDAO();
    }

    public List<Dokter> getAllDokter() {
        return dokterDAO.getAll();
    }

    public Dokter getDokterById(int id) {
        return dokterDAO.getById(id);
    }

    public List<Dokter> getDokterBySpesialisasi(String spesialisasi) {
        return dokterDAO.getBySpesialisasi(spesialisasi);
    }

    public List<Dokter> getDokterBySpesiesHewan(String spesies) {
        return dokterDAO.getBySpesiesHewan(spesies);
    }

    public List<Dokter> searchDokter(String keyword) {
        List<Dokter> all = dokterDAO.getAll();
        return all.stream()
                .filter(d -> d.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()) ||
                             d.getSpesialisasi().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean insertDokter(Dokter dokter) {
        return dokterDAO.insert(dokter);
    }

    public boolean updateDokter(Dokter dokter) {
        return dokterDAO.update(dokter);
    }

    public boolean deleteDokter(int id) {
        return dokterDAO.delete(id);
    }

    public List<Dokter> getDokterBySpesiesHewanList(List<String> spesiesList) {
        List<Dokter> all = dokterDAO.getAll();
        if (spesiesList == null || spesiesList.isEmpty()) return new ArrayList<>();
        return all.stream()
            .filter(d -> {
                String spesiesDokter = d.getSpesiesHewan();
                if (spesiesDokter == null || spesiesDokter.trim().isEmpty()) return false;
                for (String s : spesiesList) {
                    if (spesiesDokter.toLowerCase().contains(s.toLowerCase())) return true;
                }
                return false;
            })
            .collect(Collectors.toList());
    }
}