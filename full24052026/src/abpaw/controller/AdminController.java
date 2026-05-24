/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.AdminDAO;
import abpaw.model.entity.Admin;

public class AdminController {
    private AdminDAO adminDAO;

    public AdminController() {
        adminDAO = new AdminDAO();
    }

    public boolean updatePassword(int idAdmin, String newPassword) {
        Admin admin = adminDAO.getById(idAdmin);
        if (admin == null) return false;
        admin.setPassword(newPassword);
        return adminDAO.update(admin);
    }

    public Admin getAdminById(int id) {
        return adminDAO.getById(id);
    }
}