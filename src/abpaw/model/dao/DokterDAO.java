/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Dokter;
import abpaw.interfaces.CRUDable;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DokterDAO implements CRUDable<Dokter> {

    @Override
    public boolean insert(Dokter dokter) {
        String sql = "INSERT INTO dokter (nama_depan, nama_belakang, username, password, email, alamat, no_hp, spesialisasi, biaya_konsultasi, spesies_hewan) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, dokter.getNamaDepan());
            stmt.setString(2, dokter.getNamaBelakang());
            stmt.setString(3, dokter.getUsername());
            stmt.setString(4, dokter.getPassword());
            stmt.setString(5, dokter.getEmail());
            stmt.setString(6, dokter.getAlamat());
            stmt.setString(7, dokter.getNoHp());
            stmt.setString(8, dokter.getSpesialisasi());
            stmt.setBigDecimal(9, dokter.getBiayaKonsultasi());
            stmt.setString(10, dokter.getSpesiesHewan());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) dokter.setIdDokter(rs.getInt(1));
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
    public boolean update(Dokter dokter) {
        String sql = "UPDATE dokter SET nama_depan=?, nama_belakang=?, username=?, password=?, email=?, alamat=?, no_hp=?, spesialisasi=?, biaya_konsultasi=?, spesies_hewan=? WHERE id_dokter=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dokter.getNamaDepan());
            stmt.setString(2, dokter.getNamaBelakang());
            stmt.setString(3, dokter.getUsername());
            stmt.setString(4, dokter.getPassword());
            stmt.setString(5, dokter.getEmail());
            stmt.setString(6, dokter.getAlamat());
            stmt.setString(7, dokter.getNoHp());
            stmt.setString(8, dokter.getSpesialisasi());
            stmt.setBigDecimal(9, dokter.getBiayaKonsultasi());
            stmt.setString(10, dokter.getSpesiesHewan());
            stmt.setInt(11, dokter.getIdDokter());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM dokter WHERE id_dokter=?";
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
    public Dokter getById(int id) {
        String sql = "SELECT * FROM dokter WHERE id_dokter=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractDokter(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Dokter> getAll() {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractDokter(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Dokter login(String username, String password) {
        String sql = "SELECT * FROM dokter WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractDokter(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Dokter> getBySpesialisasi(String spesialisasi) {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE spesialisasi LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + spesialisasi + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractDokter(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Dokter> getBySpesiesHewan(String spesies) {
        List<Dokter> list = new ArrayList<>();
        String sql = "SELECT * FROM dokter WHERE spesies_hewan LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + spesies + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractDokter(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Dokter extractDokter(ResultSet rs) throws SQLException {
        Dokter d = new Dokter();
        d.setIdDokter(rs.getInt("id_dokter"));
        d.setNamaDepan(rs.getString("nama_depan"));
        d.setNamaBelakang(rs.getString("nama_belakang"));
        d.setUsername(rs.getString("username"));
        d.setPassword(rs.getString("password"));
        d.setEmail(rs.getString("email"));
        d.setAlamat(rs.getString("alamat"));
        d.setNoHp(rs.getString("no_hp"));
        d.setSpesialisasi(rs.getString("spesialisasi"));
        d.setBiayaKonsultasi(rs.getBigDecimal("biaya_konsultasi"));
        d.setSpesiesHewan(rs.getString("spesies_hewan"));
        d.setCreatedAt(rs.getTimestamp("created_at"));
        d.setUpdatedAt(rs.getTimestamp("updated_at"));
        return d;
    }
}