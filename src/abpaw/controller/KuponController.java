/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.KuponDAO;
import abpaw.model.entity.Kupon;
import java.util.List;

public class KuponController {
    private KuponDAO kuponDAO;

    public KuponController() {
        kuponDAO = new KuponDAO();
    }

    public boolean insertKupon(Kupon kupon) {
        return kuponDAO.insert(kupon);
    }

    public boolean updateKupon(Kupon kupon) {
        return kuponDAO.update(kupon);
    }

    public boolean deleteKupon(int id) {
        return kuponDAO.delete(id);
    }

    public Kupon getKuponById(int id) {
        return kuponDAO.getById(id);
    }

    public Kupon getKuponByKode(String kode) {
        return kuponDAO.getByKode(kode);
    }

    public List<Kupon> getAllKupon() {
        return kuponDAO.getAll();
    }

    public List<Kupon> getKuponAktif() {
        return kuponDAO.getAktif();
    }
}