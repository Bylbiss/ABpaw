/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Pemilik;
import abpaw.interfaces.CRUDable;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemilikDAO implements CRUDable<Pemilik> {

    @Override
    public boolean insert(Pemilik pemilik) {
        String sql = "INSERT INTO pemilik (nama_pemilik, username, password, no_hp, email, alamat) VALUES (?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pemilik.getNamaPemilik());
            stmt.setString(2, pemilik.getUsername());
            stmt.setString(3, pemilik.getPassword());
            stmt.setString(4, pemilik.getNoHp());
            stmt.setString(5, pemilik.getEmail());
            stmt.setString(6, pemilik.getAlamat());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pemilik.setIdPemilik(rs.getInt(1));
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
    public boolean update(Pemilik pemilik) {
        String sql = "UPDATE pemilik SET nama_pemilik=?, username=?, password=?, no_hp=?, email=?, alamat=? WHERE id_pemilik=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pemilik.getNamaPemilik());
            stmt.setString(2, pemilik.getUsername());
            stmt.setString(3, pemilik.getPassword());
            stmt.setString(4, pemilik.getNoHp());
            stmt.setString(5, pemilik.getEmail());
            stmt.setString(6, pemilik.getAlamat());
            stmt.setInt(7, pemilik.getIdPemilik());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM pemilik WHERE id_pemilik=?";
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
    public Pemilik getById(int id) {
        String sql = "SELECT * FROM pemilik WHERE id_pemilik=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractPemilik(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Pemilik> getAll() {
        List<Pemilik> list = new ArrayList<>();
        String sql = "SELECT * FROM pemilik";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractPemilik(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Pemilik login(String username, String password) {
        String sql = "SELECT * FROM pemilik WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return extractPemilik(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isUsernameExist(String username) {
        String sql = "SELECT 1 FROM pemilik WHERE username=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isEmailExist(String email) {
        String sql = "SELECT 1 FROM pemilik WHERE email=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Pemilik extractPemilik(ResultSet rs) throws SQLException {
        Pemilik p = new Pemilik();
        p.setIdPemilik(rs.getInt("id_pemilik"));
        p.setNamaPemilik(rs.getString("nama_pemilik"));
        p.setUsername(rs.getString("username"));
        p.setPassword(rs.getString("password"));
        p.setNoHp(rs.getString("no_hp"));
        p.setEmail(rs.getString("email"));
        p.setAlamat(rs.getString("alamat"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
    
    public Pemilik getByUsername(String username) {
        String sql = "SELECT * FROM pemilik WHERE username=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractPemilik(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}