/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Obat;
import abpaw.interfaces.CRUDable;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ObatDAO implements CRUDable<Obat> {

    @Override
    public boolean insert(Obat obat) {
        String sql = "INSERT INTO obat (nama_obat, deskripsi_obat, bentuk_obat, harga, stok, stok_minimal, tgl_kadaluarsa, perlu_resep) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, obat.getNamaObat());
            stmt.setString(2, obat.getDeskripsiObat());
            stmt.setString(3, obat.getBentukObat());
            stmt.setBigDecimal(4, obat.getHarga());
            stmt.setInt(5, obat.getStok());
            stmt.setInt(6, obat.getStokMinimal());
            stmt.setDate(7, obat.getTglKadaluarsa());
            stmt.setBoolean(8, obat.isPerluResep());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) obat.setIdObat(rs.getInt(1));
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
    public boolean update(Obat obat) {
        String sql = "UPDATE obat SET nama_obat=?, deskripsi_obat=?, bentuk_obat=?, harga=?, stok=?, stok_minimal=?, tgl_kadaluarsa=?, perlu_resep=? WHERE id_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, obat.getNamaObat());
            stmt.setString(2, obat.getDeskripsiObat());
            stmt.setString(3, obat.getBentukObat());
            stmt.setBigDecimal(4, obat.getHarga());
            stmt.setInt(5, obat.getStok());
            stmt.setInt(6, obat.getStokMinimal());
            stmt.setDate(7, obat.getTglKadaluarsa());
            stmt.setBoolean(8, obat.isPerluResep());
            stmt.setInt(9, obat.getIdObat());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM obat WHERE id_obat=?";
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
    public Obat getById(int id) {
        String sql = "SELECT * FROM obat WHERE id_obat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractObat(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Obat> getAll() {
        List<Obat> list = new ArrayList<>();
        String sql = "SELECT * FROM obat";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractObat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean kurangiStok(int idObat, int jumlah) {
        String sql = "UPDATE obat SET stok = stok - ? WHERE id_obat = ? AND stok >= ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, jumlah);
            stmt.setInt(2, idObat);
            stmt.setInt(3, jumlah);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Obat> getStokHampirHabis() {
        List<Obat> list = new ArrayList<>();
        String sql = "SELECT * FROM obat WHERE stok <= stok_minimal";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractObat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Obat extractObat(ResultSet rs) throws SQLException {
        Obat o = new Obat();
        o.setIdObat(rs.getInt("id_obat"));
        o.setNamaObat(rs.getString("nama_obat"));
        o.setDeskripsiObat(rs.getString("deskripsi_obat"));
        o.setBentukObat(rs.getString("bentuk_obat"));
        o.setHarga(rs.getBigDecimal("harga"));
        o.setStok(rs.getInt("stok"));
        o.setStokMinimal(rs.getInt("stok_minimal"));
        o.setTglKadaluarsa(rs.getDate("tgl_kadaluarsa"));
        o.setPerluResep(rs.getBoolean("perlu_resep"));
        o.setCreatedAt(rs.getTimestamp("created_at"));
        o.setUpdatedAt(rs.getTimestamp("updated_at"));
        return o;
    }
}