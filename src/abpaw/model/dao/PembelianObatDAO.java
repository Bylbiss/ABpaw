/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.PembelianObat;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PembelianObatDAO {

    public PembelianObat insert(PembelianObat pb) {
        String sql = "INSERT INTO pembelian_obat (id_pemilik, id_obat, jumlah, harga_satuan, total_harga, tanggal, status, kode_transaksi) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pb.getIdPemilik());
            stmt.setInt(2, pb.getIdObat());
            stmt.setInt(3, pb.getJumlah());
            stmt.setBigDecimal(4, pb.getHargaSatuan());
            stmt.setBigDecimal(5, pb.getTotalHarga());
            stmt.setTimestamp(6, pb.getTanggal());
            stmt.setString(7, pb.getStatus());
            stmt.setString(8, pb.getKodeTransaksi());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pb.setIdPembelian(rs.getInt(1));
                }
                return pb;
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<PembelianObat> getByPemilik(int idPemilik) {
        List<PembelianObat> list = new ArrayList<>();
        String sql = "SELECT * FROM pembelian_obat WHERE id_pemilik=? ORDER BY tanggal DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                PembelianObat pb = new PembelianObat();
                pb.setIdPembelian(rs.getInt("id_pembelian"));
                pb.setIdPemilik(rs.getInt("id_pemilik"));
                pb.setIdObat(rs.getInt("id_obat"));
                pb.setJumlah(rs.getInt("jumlah"));
                pb.setHargaSatuan(rs.getBigDecimal("harga_satuan"));
                pb.setTotalHarga(rs.getBigDecimal("total_harga"));
                pb.setTanggal(rs.getTimestamp("tanggal"));
                pb.setStatus(rs.getString("status"));
                pb.setKodeTransaksi(rs.getString("kode_transaksi"));
                list.add(pb);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}