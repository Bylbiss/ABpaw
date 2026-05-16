/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PetsDAO;
import abpaw.model.entity.Pets;
import java.util.List;

public class PetsController {
    private PetsDAO petsDAO;
    public Pets getPetsById(int id) {
        return petsDAO.getById(id);
    }

    public PetsController() {
        petsDAO = new PetsDAO();
    }
    
    public boolean updatePets(Pets pet) { 
        return petsDAO.update(pet); 
    }
    
    public boolean deletePets(int id) { 
        return petsDAO.delete(id); 
    }

    public List<Pets> getPetsByPemilik(int idPemilik) {
        return petsDAO.getByPemilik(idPemilik);
    }
    
    public boolean insertPets(Pets pet) {
        return petsDAO.insert(pet);
    }
}