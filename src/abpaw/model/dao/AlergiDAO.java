/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Alergi;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlergiDAO {
    // Hapus semua alergi berdasarkan id_pet (untuk update)
    public boolean deleteByPet(int idPet) {
        String sql = "DELETE FROM alergi_pet WHERE id_pet = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPet);
            return stmt.executeUpdate() >= 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Ambil semua alergi untuk satu pet
    public List<Alergi> getAlergiByPet(int idPet) {
        List<Alergi> list = new ArrayList<>();
        String sql = "SELECT * FROM alergi_pet WHERE id_pet = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPet);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Alergi a = new Alergi();
                a.setIdAlergi(rs.getInt("id_alergi"));
                a.setIdPet(rs.getInt("id_pet"));
                a.setIdObat(rs.getInt("id_obat"));
                a.setNamaAlergi(rs.getString("nama_alergi"));
                a.setCreatedByType(rs.getString("created_by_type"));
                a.setCreatedById(rs.getInt("created_by_id"));
                a.setStatus(rs.getString("status"));
                a.setVerifiedBy(rs.getInt("verified_by"));
                a.setVerifiedAt(rs.getTimestamp("verified_at"));
                a.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(a);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // Insert alergi dengan id_obat boleh null
    public boolean insert(Alergi alergi) {
        String sql = "INSERT INTO alergi_pet (id_pet, id_obat, nama_alergi, created_by_type, created_by_id, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, alergi.getIdPet());
            if (alergi.getIdObat() != 0) {
                stmt.setInt(2, alergi.getIdObat());
            } else {
                stmt.setNull(2, Types.INTEGER);
            }
            stmt.setString(3, alergi.getNamaAlergi());
            stmt.setString(4, alergi.getCreatedByType());
            stmt.setInt(5, alergi.getCreatedById());
            stmt.setString(6, alergi.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        alergi.setIdAlergi(rs.getInt(1));
                    }
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}