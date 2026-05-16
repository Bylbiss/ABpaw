/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.PemesananOnline;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PemesananDAO {

    // ==================== ONLINE ====================
    public boolean insertOnline(PemesananOnline pesan) {
        String sql = "INSERT INTO pemesanan_online (kode_pemesanan, id_pemilik, id_dokter, id_pet, tanggal_konsultasi, waktu_konsultasi, keluhan, biaya_konsultasi, kupon_digunakan, jumlah_diskon, total_biaya, status_pemesanan, waktu_pemesanan) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, pesan.getKodePemesanan());
            stmt.setInt(2, pesan.getIdPemilik());
            stmt.setInt(3, pesan.getIdDokter());
            stmt.setInt(4, pesan.getIdPet());
            stmt.setDate(5, Date.valueOf(pesan.getTanggalKonsultasi()));
            stmt.setString(6, pesan.getWaktuKonsultasi());
            stmt.setString(7, pesan.getKeluhan());
            stmt.setBigDecimal(8, pesan.getBiayaKonsultasi());
            stmt.setString(9, pesan.getKuponDigunakan());
            stmt.setBigDecimal(10, pesan.getJumlahDiskon());
            stmt.setBigDecimal(11, pesan.getTotalBiaya());
            stmt.setString(12, pesan.getStatus());
            stmt.setTimestamp(13, pesan.getWaktuPemesanan());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pesan.setIdTransaksi(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOnline(PemesananOnline pesan) {
        String sql = "UPDATE pemesanan_online SET kode_pemesanan=?, id_pemilik=?, id_dokter=?, id_pet=?, tanggal_konsultasi=?, waktu_konsultasi=?, keluhan=?, biaya_konsultasi=?, kupon_digunakan=?, jumlah_diskon=?, total_biaya=?, status_pemesanan=?, waktu_pemesanan=? WHERE id_pemesanan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pesan.getKodePemesanan());
            stmt.setInt(2, pesan.getIdPemilik());
            stmt.setInt(3, pesan.getIdDokter());
            stmt.setInt(4, pesan.getIdPet());
            stmt.setDate(5, Date.valueOf(pesan.getTanggalKonsultasi()));
            stmt.setString(6, pesan.getWaktuKonsultasi());
            stmt.setString(7, pesan.getKeluhan());
            stmt.setBigDecimal(8, pesan.getBiayaKonsultasi());
            stmt.setString(9, pesan.getKuponDigunakan());
            stmt.setBigDecimal(10, pesan.getJumlahDiskon());
            stmt.setBigDecimal(11, pesan.getTotalBiaya());
            stmt.setString(12, pesan.getStatus());
            stmt.setTimestamp(13, pesan.getWaktuPemesanan());
            stmt.setInt(14, pesan.getIdTransaksi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOnline(int id) {
        String sql = "DELETE FROM pemesanan_online WHERE id_pemesanan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PemesananOnline getOnlineById(int id) {
        String sql = "SELECT * FROM pemesanan_online WHERE id_pemesanan=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractOnline(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PemesananOnline> getAllOnline() {
        List<PemesananOnline> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan_online";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractOnline(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PemesananOnline> getOnlineByPemilik(int idPemilik) {
        List<PemesananOnline> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan_online WHERE id_pemilik=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractOnline(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // ==================== OFFLINE ====================
    public boolean insertOffline(PemesananOffline pesan) {
        String sql = "INSERT INTO pemesanan_offline (id_dokter, id_pemilik, id_pet, nomor_antrean, tanggal_antrean, waktu_antrean, keluhan, status_antrean, estimasi_waktu) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, pesan.getIdDokter());
            stmt.setInt(2, pesan.getIdPemilik());
            stmt.setInt(3, pesan.getIdPet());
            stmt.setString(4, pesan.getNomorAntrean());
            stmt.setDate(5, Date.valueOf(pesan.getTanggalAntrean()));
            stmt.setTime(6, Time.valueOf(pesan.getWaktuAntrean()));
            stmt.setString(7, pesan.getKeluhan());
            stmt.setString(8, pesan.getStatus());
            stmt.setTime(9, pesan.getEstimasiWaktu() != null ? Time.valueOf(pesan.getEstimasiWaktu()) : null);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) pesan.setIdTransaksi(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateOffline(PemesananOffline pesan) {
        String sql = "UPDATE pemesanan_offline SET id_dokter=?, id_pemilik=?, id_pet=?, nomor_antrean=?, tanggal_antrean=?, waktu_antrean=?, keluhan=?, status_antrean=?, estimasi_waktu=? WHERE id_antrean=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pesan.getIdDokter());
            stmt.setInt(2, pesan.getIdPemilik());
            stmt.setInt(3, pesan.getIdPet());
            stmt.setString(4, pesan.getNomorAntrean());
            stmt.setDate(5, Date.valueOf(pesan.getTanggalAntrean()));
            stmt.setTime(6, Time.valueOf(pesan.getWaktuAntrean()));
            stmt.setString(7, pesan.getKeluhan());
            stmt.setString(8, pesan.getStatus());
            stmt.setTime(9, pesan.getEstimasiWaktu() != null ? Time.valueOf(pesan.getEstimasiWaktu()) : null);
            stmt.setInt(10, pesan.getIdTransaksi());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteOffline(int id) {
        String sql = "DELETE FROM pemesanan_offline WHERE id_antrean=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PemesananOffline getOfflineById(int id) {
        String sql = "SELECT * FROM pemesanan_offline WHERE id_antrean=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractOffline(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<PemesananOffline> getAllOffline() {
        List<PemesananOffline> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan_offline";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractOffline(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<PemesananOffline> getOfflineByPemilik(int idPemilik) {
        List<PemesananOffline> list = new ArrayList<>();
        String sql = "SELECT * FROM pemesanan_offline WHERE id_pemilik=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractOffline(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private PemesananOnline extractOnline(ResultSet rs) throws SQLException {
        PemesananOnline po = new PemesananOnline();
        po.setIdTransaksi(rs.getInt("id_pemesanan"));
        po.setKodePemesanan(rs.getString("kode_pemesanan"));
        po.setIdPemilik(rs.getInt("id_pemilik"));
        po.setIdDokter(rs.getInt("id_dokter"));
        po.setIdPet(rs.getInt("id_pet"));
        po.setTanggalKonsultasi(rs.getDate("tanggal_konsultasi").toLocalDate());
        po.setWaktuKonsultasi(rs.getString("waktu_konsultasi"));
        po.setKeluhan(rs.getString("keluhan"));
        po.setBiayaKonsultasi(rs.getBigDecimal("biaya_konsultasi"));
        po.setKuponDigunakan(rs.getString("kupon_digunakan"));
        po.setJumlahDiskon(rs.getBigDecimal("jumlah_diskon"));
        po.setTotalBiaya(rs.getBigDecimal("total_biaya"));
        po.setStatus(rs.getString("status_pemesanan"));
        po.setWaktuPemesanan(rs.getTimestamp("waktu_pemesanan"));
        po.setCreatedAt(rs.getTimestamp("created_at"));
        po.setUpdatedAt(rs.getTimestamp("updated_at"));
        return po;
    }

    private PemesananOffline extractOffline(ResultSet rs) throws SQLException {
        PemesananOffline poff = new PemesananOffline();
        poff.setIdTransaksi(rs.getInt("id_antrean"));
        poff.setIdDokter(rs.getInt("id_dokter"));
        poff.setIdPemilik(rs.getInt("id_pemilik"));
        poff.setIdPet(rs.getInt("id_pet"));
        poff.setNomorAntrean(rs.getString("nomor_antrean"));
        poff.setTanggalAntrean(rs.getDate("tanggal_antrean").toLocalDate());
        poff.setWaktuAntrean(rs.getTime("waktu_antrean").toLocalTime());
        poff.setKeluhan(rs.getString("keluhan"));
        poff.setStatus(rs.getString("status_antrean"));
        Time estimasi = rs.getTime("estimasi_waktu");
        if (estimasi != null) poff.setEstimasiWaktu(estimasi.toLocalTime());
        poff.setCreatedAt(rs.getTimestamp("created_at"));
        poff.setUpdatedAt(rs.getTimestamp("updated_at"));
        return poff;
    }
}