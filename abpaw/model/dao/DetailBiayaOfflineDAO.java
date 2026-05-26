/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.DetailBiayaOffline;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author LOQ
 */
public class DetailBiayaOfflineDAO {
    
    // INSERT biaya tambahan
    public boolean insert(DetailBiayaOffline detail) {
        String sql = "INSERT INTO detail_biaya_offline (id_antrean, nama_biaya, jumlah, harga_satuan, subtotal) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getIdAntrean());
            stmt.setString(2, detail.getNamaBiaya());
            stmt.setInt(3, detail.getJumlah());
            stmt.setBigDecimal(4, detail.getHargaSatuan());
            stmt.setBigDecimal(5, detail.getSubtotal());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        detail.setIdDetail(rs.getInt(1));
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
    
    // GET semua biaya tambahan berdasarkan id_antrean
    public List<DetailBiayaOffline> getByAntrean(int idAntrean) {
        List<DetailBiayaOffline> list = new ArrayList<>();
        String sql = "SELECT * FROM detail_biaya_offline WHERE id_antrean = ? ORDER BY id_detail";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAntrean);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetailBiayaOffline d = new DetailBiayaOffline();
                d.setIdDetail(rs.getInt("id_detail"));
                d.setIdAntrean(rs.getInt("id_antrean"));
                d.setNamaBiaya(rs.getString("nama_biaya"));
                d.setJumlah(rs.getInt("jumlah"));
                d.setHargaSatuan(rs.getBigDecimal("harga_satuan"));
                d.setSubtotal(rs.getBigDecimal("subtotal"));
                d.setCreatedAt(rs.getTimestamp("created_at"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    // DELETE biaya tambahan berdasarkan id_detail
    public boolean delete(int idDetail) {
        String sql = "DELETE FROM detail_biaya_offline WHERE id_detail = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDetail);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // DELETE semua biaya tambahan berdasarkan id_antrean
    public boolean deleteByAntrean(int idAntrean) {
        String sql = "DELETE FROM detail_biaya_offline WHERE id_antrean = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idAntrean);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
