/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.AlergiDAO;
import abpaw.model.entity.Alergi;
import java.util.List;
import java.util.stream.Collectors;

public class AlergiController {
    private AlergiDAO alergiDAO;
    
    public AlergiController() {
        alergiDAO = new AlergiDAO();
    }
    
    public List<Alergi> getAlergiByPet(int idPet) {
        return alergiDAO.getAlergiByPet(idPet);
    }
    
    public boolean addAlergi(int idPet, String namaAlergi, int createdById, String createdByType) {
        Alergi alergi = new Alergi();
        alergi.setIdPet(idPet);
        alergi.setIdObat(0); // 0 menandakan null di database
        alergi.setNamaAlergi(namaAlergi);
        alergi.setCreatedByType(createdByType);
        alergi.setCreatedById(createdById);
        alergi.setStatus("usulan");
        return alergiDAO.insert(alergi);
    }
    
    public boolean updateAlergiForPet(int idPet, List<String> alergiList, int createdById, String createdByType) {
        if (!alergiDAO.deleteByPet(idPet)) {
            return false;
        }
        for (String nama : alergiList) {
            if (nama != null && !nama.trim().isEmpty()) {
                Alergi alergi = new Alergi();
                alergi.setIdPet(idPet);
                alergi.setNamaAlergi(nama.trim());
                alergi.setCreatedByType(createdByType);
                alergi.setCreatedById(createdById);
                alergi.setStatus("usulan");
                alergi.setIdObat(0);
                if (!alergiDAO.insert(alergi)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getAlergiNamesByPet(int idPet) {
        List<Alergi> list = alergiDAO.getAlergiByPet(idPet);
        return list.stream()
                .map(Alergi::getNamaAlergi)
                .collect(Collectors.joining(", "));
    }
}