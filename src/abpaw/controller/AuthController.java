/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.AdminDAO;
import abpaw.model.dao.DokterDAO;
import abpaw.model.dao.PemilikDAO;
import abpaw.model.entity.Admin;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.User;
import abpaw.utils.SessionManager;

public class AuthController {
    private AdminDAO adminDAO;
    private DokterDAO dokterDAO;
    private PemilikDAO pemilikDAO;

    public AuthController() {
        adminDAO = new AdminDAO();
        dokterDAO = new DokterDAO();
        pemilikDAO = new PemilikDAO();
    }

    // Login: otomatis mendeteksi role
    public User login(String username, String password) {
        // Cek admin dulu
        Admin admin = adminDAO.login(username, password);
        if (admin != null) {
            SessionManager.getInstance().setCurrentUser(admin);
            return admin;
        }
        // Cek dokter
        Dokter dokter = dokterDAO.login(username, password);
        if (dokter != null) {
            SessionManager.getInstance().setCurrentUser(dokter);
            return dokter;
        }
        // Cek pemilik
        Pemilik pemilik = pemilikDAO.login(username, password);
        if (pemilik != null) {
            SessionManager.getInstance().setCurrentUser(pemilik);
            return pemilik;
        }
        return null;
    }

    // Register hanya untuk pemilik
    public boolean registerPemilik(String namaLengkap, String username, String password, 
                                   String noHp, String email, String alamat) {
        // Cek username atau email sudah ada?
        if (pemilikDAO.isUsernameExist(username)) {
            return false;
        }
        if (pemilikDAO.isEmailExist(email)) {
            return false;
        }
        Pemilik pemilik = new Pemilik();
        pemilik.setNamaPemilik(namaLengkap);
        pemilik.setUsername(username);
        pemilik.setPassword(password);
        pemilik.setNoHp(noHp);
        pemilik.setEmail(email);
        pemilik.setAlamat(alamat);
        return pemilikDAO.insert(pemilik);
    }

    public void logout() {
        SessionManager.getInstance().clearSession();
    }
    
    public Pemilik getPemilikByUsername(String username) {
        return pemilikDAO.getByUsername(username); // perlu dibuat di PemilikDAO
    }
}