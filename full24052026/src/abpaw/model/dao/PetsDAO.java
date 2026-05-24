/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Pets;
import abpaw.interfaces.CRUDable;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PetsDAO implements CRUDable<Pets> {

    @Override
    public boolean insert(Pets pet) {
        String sql = "INSERT INTO pets (id_pemilik, nama_pet, jenis_kelamin, jenis_hewan, ras, tanggal_lahir, usia, berat, sterilisasi) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pet.getIdPemilik());
            stmt.setString(2, pet.getNamaPet());
            stmt.setString(3, pet.getJenisKelamin());
            stmt.setString(4, pet.getJenisHewan());
            stmt.setString(5, pet.getRas());
            stmt.setDate(6, pet.getTanggalLahir());
            stmt.setObject(7, pet.getUsia(), Types.INTEGER);
            stmt.setBigDecimal(8, pet.getBerat());
            stmt.setString(9, pet.getSterilisasi());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pet.setIdPet(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean update(Pets pet) {
        String sql = "UPDATE pets SET nama_pet=?, jenis_kelamin=?, jenis_hewan=?, ras=?, tanggal_lahir=?, usia=?, berat=?, sterilisasi=? WHERE id_pet=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNamaPet());
            stmt.setString(2, pet.getJenisKelamin());
            stmt.setString(3, pet.getJenisHewan());
            stmt.setString(4, pet.getRas());
            stmt.setDate(5, pet.getTanggalLahir());
            stmt.setObject(6, pet.getUsia(), Types.INTEGER);
            stmt.setBigDecimal(7, pet.getBerat());
            stmt.setString(8, pet.getSterilisasi());
            stmt.setInt(9, pet.getIdPet());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pets WHERE id_pet=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Pets getById(int id) {
        String sql = "SELECT * FROM pets WHERE id_pet=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractPets(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pets> getAll() {
        List<Pets> list = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractPets(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Pets> getByPemilik(int idPemilik) {
        List<Pets> list = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE id_pemilik=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractPets(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Pets extractPets(ResultSet rs) throws SQLException {
        Pets p = new Pets();
        p.setIdPet(rs.getInt("id_pet"));
        p.setIdPemilik(rs.getInt("id_pemilik"));
        p.setNamaPet(rs.getString("nama_pet"));
        p.setJenisKelamin(rs.getString("jenis_kelamin"));
        p.setJenisHewan(rs.getString("jenis_hewan"));
        p.setRas(rs.getString("ras"));
        p.setTanggalLahir(rs.getDate("tanggal_lahir"));
        p.setUsia(rs.getInt("usia"));
        p.setBerat(rs.getBigDecimal("berat"));
        p.setSterilisasi(rs.getString("sterilisasi"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}