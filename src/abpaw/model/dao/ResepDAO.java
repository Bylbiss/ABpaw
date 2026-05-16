/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Resep;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResepDAO {

    public boolean insert(Resep resep) {
        String sql = "INSERT INTO resep_obat (id_pemesanan, tipe_pemesanan, id_dokter, id_apoteker, tanggal_resep, status) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, resep.getIdPemesanan());
            stmt.setString(2, resep.getTipePemesanan());
            stmt.setInt(3, resep.getIdDokter());
            if (resep.getIdApoteker() != null) stmt.setInt(4, resep.getIdApoteker());
            else stmt.setNull(4, Types.INTEGER);
            stmt.setDate(5, resep.getTanggalResep());
            stmt.setString(6, resep.getStatus());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) resep.setIdResep(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Resep resep) {
        String sql = "UPDATE resep_obat SET id_pemesanan=?, tipe_pemesanan=?, id_dokter=?, id_apoteker=?, tanggal_resep=?, status=? WHERE id_resep=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resep.getIdPemesanan());
            stmt.setString(2, resep.getTipePemesanan());
            stmt.setInt(3, resep.getIdDokter());
            if (resep.getIdApoteker() != null) stmt.setInt(4, resep.getIdApoteker());
            else stmt.setNull(4, Types.INTEGER);
            stmt.setDate(5, resep.getTanggalResep());
            stmt.setString(6, resep.getStatus());
            stmt.setInt(7, resep.getIdResep());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM resep_obat WHERE id_resep=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Resep getById(int id) {
        String sql = "SELECT * FROM resep_obat WHERE id_resep=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractResep(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Resep> getAll() {
        List<Resep> list = new ArrayList<>();
        String sql = "SELECT * FROM resep_obat";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractResep(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Resep> getByDokter(int idDokter) {
        List<Resep> list = new ArrayList<>();
        String sql = "SELECT * FROM resep_obat WHERE id_dokter=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDokter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractResep(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Resep> getByPemesanan(int idPemesanan, String tipe) {
        List<Resep> list = new ArrayList<>();
        String sql = "SELECT * FROM resep_obat WHERE id_pemesanan=? AND tipe_pemesanan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemesanan);
            stmt.setString(2, tipe);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractResep(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean updateStatus(int idResep, String status) {
        String sql = "UPDATE resep_obat SET status=? WHERE id_resep=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, idResep);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Resep extractResep(ResultSet rs) throws SQLException {
        Resep r = new Resep();
        r.setIdResep(rs.getInt("id_resep"));
        r.setIdPemesanan(rs.getInt("id_pemesanan"));
        r.setTipePemesanan(rs.getString("tipe_pemesanan"));
        r.setIdDokter(rs.getInt("id_dokter"));
        int apotekerId = rs.getInt("id_apoteker");
        if (!rs.wasNull()) r.setIdApoteker(apotekerId);
        r.setTanggalResep(rs.getDate("tanggal_resep"));
        r.setStatus(rs.getString("status"));
        r.setCreatedAt(rs.getTimestamp("created_at"));
        return r;
    }
}