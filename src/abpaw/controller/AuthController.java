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
import org.mindrot.jbcrypt.BCrypt;

public class AuthController {
    private AdminDAO adminDAO;
    private DokterDAO dokterDAO;
    private PemilikDAO pemilikDAO;

    public AuthController() {
        adminDAO = new AdminDAO();
        dokterDAO = new DokterDAO();
        pemilikDAO = new PemilikDAO();
    }

    // Hash password
    private String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
    
    // Verifikasi password
    private boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
    
    // Login dengan verifikasi hash
    public User login(String username, String password) {
        // Cek admin
        Admin admin = adminDAO.getByUsername(username);
        if (admin != null && checkPassword(password, admin.getPassword())) {
            SessionManager.getInstance().setCurrentUser(admin);
            return admin;
        }
        // Cek dokter
        Dokter dokter = dokterDAO.getByUsername(username);
        if (dokter != null && checkPassword(password, dokter.getPassword())) {
            SessionManager.getInstance().setCurrentUser(dokter);
            return dokter;
        }
        // Cek pemilik
        Pemilik pemilik = pemilikDAO.getByUsername(username);
        if (pemilik != null && checkPassword(password, pemilik.getPassword())) {
            SessionManager.getInstance().setCurrentUser(pemilik);
            return pemilik;
        }
        return null;
    }

    public boolean registerPemilik(String namaLengkap, String username, String password,
            String noHp, String email, String alamat) {
        // Normalisasi username dan email ke lowercase (opsional)
        username = username.toLowerCase();
        email = email.toLowerCase();

        if (pemilikDAO.isUsernameExist(username)) {
            return false;
        }
        if (pemilikDAO.isEmailExist(email)) {
            return false;
        }
        Pemilik pemilik = new Pemilik();
        pemilik.setNamaPemilik(namaLengkap);
        pemilik.setUsername(username);
        pemilik.setPassword(hashPassword(password));
        pemilik.setNoHp(noHp);
        pemilik.setEmail(email);
        pemilik.setAlamat(alamat);
        return pemilikDAO.insert(pemilik);
    }
    
    // Update password (untuk profil) dengan hash
    public boolean updatePassword(Pemilik pemilik, String newPassword) {
        pemilik.setPassword(hashPassword(newPassword));
        return pemilikDAO.update(pemilik);
    }
    
    public String validatePemilikData(String namaDepan, String namaBelakang,
            String username, String password,
            String noHp, String email, String alamat,
            boolean isUpdate) {
        // Nama Depan
        if (namaDepan == null || namaDepan.trim().isEmpty()) {
            return "Nama Depan wajib diisi!";
        }
        if (!Character.isUpperCase(namaDepan.charAt(0))) {
            return "Huruf pertama Nama Depan harus kapital!";
        }
        
        // Nama Belakang (opsional)
        if (namaBelakang != null && !namaBelakang.trim().isEmpty()) {
            if (!Character.isUpperCase(namaBelakang.charAt(0))) {
                return "Huruf pertama Nama Belakang harus kapital!";
            }
        }
        
        // Username
        if (username == null || username.trim().isEmpty()) {
            return "Username wajib diisi!";
        }
        
        // Password (hanya divalidasi jika password baru diisi atau saat registrasi)
        if (password != null && !password.isEmpty()) {
            String passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{6,}$";
            if (!password.matches(passwordRegex)) {
                return "Password harus minimal 6 karakter, mengandung huruf besar, huruf kecil, angka, dan simbol!";
            }
        } else if (!isUpdate) {
            // Registrasi wajib password
            return "Password wajib diisi!";
        }
        
        // No HP
        if (noHp == null || noHp.trim().isEmpty()) {
            return "No HP wajib diisi!";
        }
        if (!noHp.matches("\\d+")) {
            return "No HP harus berupa angka!";
        }
        if (noHp.length() < 11) {
            return "No HP minimal 11 angka!";
        }
        
        // Email
        if (email == null || email.trim().isEmpty()) {
            return "Email wajib diisi!";
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            return "Format email tidak valid! Contoh: nama@gmail.com";
        }
        // Alamat
        if (alamat == null || alamat.trim().isEmpty()) {
            return "Alamat wajib diisi!";
        }
        return null; // valid
    }
    
    public String validateUpdatePemilikData(String namaDepan, String namaBelakang,
            String username, String password, String noHp, String email, String alamat,
            int idPemilik) {
        // Validasi nama dan format lainnya (sama seperti validatePemilikData)
        String error = validatePemilikData(namaDepan, namaBelakang, username,
                password, noHp, email, alamat, true);
        if (error != null) {
            return error;
        }

        // Cek duplikat username selain dirinya sendiri
        Pemilik existingByUsername = pemilikDAO.getByUsername(username);
        if (existingByUsername != null && existingByUsername.getIdPemilik() != idPemilik) {
            return "Username sudah digunakan oleh pemilik lain!";
        }

        // Cek duplikat email selain dirinya sendiri
        if (pemilikDAO.isEmailExistExcept(email, idPemilik)) {
            return "Email sudah digunakan oleh pemilik lain!";
        }

        return null;
    }
    
    public boolean updatePemilikProfil(Pemilik pemilik, String newPassword) {
        if (newPassword != null && !newPassword.isEmpty()) {
            pemilik.setPassword(hashPassword(newPassword));
        }
        return pemilikDAO.update(pemilik);
    }
    
    public void logout() {
        SessionManager.getInstance().clearSession();
    }

    public Pemilik getPemilikByUsername(String username) {
        return pemilikDAO.getByUsername(username);
    }
    
    public static void migrateAllPasswords() {
        // Migrasi pemilik
        PemilikDAO pemilikDAO = new PemilikDAO();
        for (Pemilik p : pemilikDAO.getAll()) {
            String pass = p.getPassword();
            if (pass != null && !pass.startsWith("$2")) { // belum ter-hash
                p.setPassword(BCrypt.hashpw(pass, BCrypt.gensalt()));
                pemilikDAO.update(p);
                System.out.println("Migrated pemilik: " + p.getUsername());
            }
        }

        // Migrasi dokter
        DokterDAO dokterDAO = new DokterDAO();
        for (Dokter d : dokterDAO.getAll()) {
            String pass = d.getPassword();
            if (pass != null && !pass.startsWith("$2")) {
                d.setPassword(BCrypt.hashpw(pass, BCrypt.gensalt()));
                dokterDAO.update(d);
                System.out.println("Migrated dokter: " + d.getUsername());
            }
        }

        // Migrasi admin
        AdminDAO adminDAO = new AdminDAO();
        for (Admin a : adminDAO.getAll()) {
            String pass = a.getPassword();
            if (pass != null && !pass.startsWith("$2")) {
                a.setPassword(BCrypt.hashpw(pass, BCrypt.gensalt()));
                adminDAO.update(a);
                System.out.println("Migrated admin: " + a.getUsername());
            }
        }
    }
}