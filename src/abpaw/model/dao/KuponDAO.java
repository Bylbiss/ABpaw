/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Kupon;
import abpaw.interfaces.CRUDable;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class KuponDAO implements CRUDable<Kupon> {

    @Override
    public boolean insert(Kupon kupon) {
        String sql = "INSERT INTO kupon (kode, deskripsi, diskon_persen, diskon_maks, minimal_pembelian, berlaku_hingga, aktif) VALUES (?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, kupon.getKode());
            stmt.setString(2, kupon.getDeskripsi());
            stmt.setBigDecimal(3, kupon.getDiskonPersen());
            stmt.setBigDecimal(4, kupon.getDiskonMaks());
            stmt.setBigDecimal(5, kupon.getMinimalPembelian());
            stmt.setDate(6, kupon.getBerlakuHingga());
            stmt.setBoolean(7, kupon.isAktif());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) kupon.setIdKupon(rs.getInt(1));
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
    public boolean update(Kupon kupon) {
        String sql = "UPDATE kupon SET kode=?, deskripsi=?, diskon_persen=?, diskon_maks=?, minimal_pembelian=?, berlaku_hingga=?, aktif=? WHERE id_kupon=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kupon.getKode());
            stmt.setString(2, kupon.getDeskripsi());
            stmt.setBigDecimal(3, kupon.getDiskonPersen());
            stmt.setBigDecimal(4, kupon.getDiskonMaks());
            stmt.setBigDecimal(5, kupon.getMinimalPembelian());
            stmt.setDate(6, kupon.getBerlakuHingga());
            stmt.setBoolean(7, kupon.isAktif());
            stmt.setInt(8, kupon.getIdKupon());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM kupon WHERE id_kupon=?";
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
    public Kupon getById(int id) {
        String sql = "SELECT * FROM kupon WHERE id_kupon=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractKupon(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Kupon> getAll() {
        List<Kupon> list = new ArrayList<>();
        String sql = "SELECT * FROM kupon";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractKupon(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Kupon getByKode(String kode) {
        String sql = "SELECT * FROM kupon WHERE kode=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, kode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractKupon(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Kupon> getAktif() {
        List<Kupon> list = new ArrayList<>();
        String sql = "SELECT * FROM kupon WHERE aktif=1 AND (berlaku_hingga IS NULL OR berlaku_hingga >= CURDATE())";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractKupon(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Kupon extractKupon(ResultSet rs) throws SQLException {
        Kupon k = new Kupon();
        k.setIdKupon(rs.getInt("id_kupon"));
        k.setKode(rs.getString("kode"));
        k.setDeskripsi(rs.getString("deskripsi"));
        k.setDiskonPersen(rs.getBigDecimal("diskon_persen"));
        k.setDiskonMaks(rs.getBigDecimal("diskon_maks"));
        k.setMinimalPembelian(rs.getBigDecimal("minimal_pembelian"));
        k.setBerlakuHingga(rs.getDate("berlaku_hingga"));
        k.setAktif(rs.getBoolean("aktif"));
        k.setCreatedAt(rs.getTimestamp("created_at"));
        return k;
    }
}