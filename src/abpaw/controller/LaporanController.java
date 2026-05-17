/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PemesananDAO;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import abpaw.model.db.DatabaseConnection;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class LaporanController {
    private PemesananDAO pemesananDAO;

    public LaporanController() {
        pemesananDAO = new PemesananDAO();
    }

    public List<PemesananOnline> getPemesananOnlineByDateRange(LocalDate start, LocalDate end) {
        return pemesananDAO.getAllOnline().stream()
                .filter(p -> !p.getTanggalKonsultasi().isBefore(start) && !p.getTanggalKonsultasi().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<PemesananOffline> getPemesananOfflineByDateRange(LocalDate start, LocalDate end) {
        return pemesananDAO.getAllOffline().stream()
                .filter(p -> !p.getTanggalAntrean().isBefore(start) && !p.getTanggalAntrean().isAfter(end))
                .collect(Collectors.toList());
    }

    public List<Object[]> getPendapatanPerDokter(LocalDate start, LocalDate end) {
        List<PemesananOnline> onlineList = getPemesananOnlineByDateRange(start, end);
        return onlineList.stream()
                .collect(Collectors.groupingBy(
                    PemesananOnline::getIdDokter,
                    Collectors.reducing(BigDecimal.ZERO, PemesananOnline::getTotalBiaya, BigDecimal::add)
                ))
                .entrySet().stream()
                .map(e -> new Object[]{e.getKey(), "Dokter ID " + e.getKey(), "-", "Rp " + e.getValue()})
                .collect(Collectors.toList());
    }

    // Laporan ONLINE dengan NAMA (tanpa jam)
    public List<Object[]> getLaporanOnlineWithNames(LocalDate start, LocalDate end) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT po.id_pemesanan, po.kode_pemesanan, " +
                     "p.nama_pemilik, " +
                     "CONCAT(d.nama_depan, ' ', d.nama_belakang) as nama_dokter, " +
                     "pet.nama_pet, " +
                     "po.tanggal_konsultasi, " +
                     "po.total_biaya, po.status_pemesanan " +
                     "FROM pemesanan_online po " +
                     "JOIN pemilik p ON po.id_pemilik = p.id_pemilik " +
                     "JOIN dokter d ON po.id_dokter = d.id_dokter " +
                     "JOIN pets pet ON po.id_pet = pet.id_pet " +
                     "WHERE po.tanggal_konsultasi BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[8];
                row[0] = rs.getInt("id_pemesanan");
                row[1] = rs.getString("kode_pemesanan");
                row[2] = rs.getString("nama_pemilik");
                row[3] = rs.getString("nama_dokter");
                row[4] = rs.getString("nama_pet");
                row[5] = rs.getDate("tanggal_konsultasi").toLocalDate();
                row[6] = rs.getBigDecimal("total_biaya");
                row[7] = rs.getString("status_pemesanan");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Laporan OFFLINE dengan NAMA (tanpa jam)
    public List<Object[]> getLaporanOfflineWithNames(LocalDate start, LocalDate end) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT poff.id_antrean, poff.nomor_antrean, " +
                     "p.nama_pemilik, " +
                     "CONCAT(d.nama_depan, ' ', d.nama_belakang) as nama_dokter, " +
                     "pet.nama_pet, " +
                     "poff.tanggal_antrean, poff.status_antrean " +
                     "FROM pemesanan_offline poff " +
                     "JOIN pemilik p ON poff.id_pemilik = p.id_pemilik " +
                     "JOIN dokter d ON poff.id_dokter = d.id_dokter " +
                     "JOIN pets pet ON poff.id_pet = pet.id_pet " +
                     "WHERE poff.tanggal_antrean BETWEEN ? AND ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[7];
                row[0] = rs.getInt("id_antrean");
                row[1] = rs.getString("nomor_antrean");
                row[2] = rs.getString("nama_pemilik");
                row[3] = rs.getString("nama_dokter");
                row[4] = rs.getString("nama_pet");
                row[5] = rs.getDate("tanggal_antrean").toLocalDate();
                row[6] = rs.getString("status_antrean");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Pendapatan per dokter dengan NAMA
    public List<Object[]> getPendapatanPerDokterWithNames(LocalDate start, LocalDate end) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT d.id_dokter, CONCAT(d.nama_depan, ' ', d.nama_belakang) as nama_dokter, " +
                     "COUNT(po.id_pemesanan) as jumlah_konsultasi, " +
                     "COALESCE(SUM(po.total_biaya), 0) as total_pendapatan " +
                     "FROM dokter d " +
                     "LEFT JOIN pemesanan_online po ON d.id_dokter = po.id_dokter " +
                     "AND po.tanggal_konsultasi BETWEEN ? AND ? " +
                     "GROUP BY d.id_dokter, d.nama_depan, d.nama_belakang";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2, Date.valueOf(end));
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[4];
                row[0] = rs.getInt("id_dokter");
                row[1] = rs.getString("nama_dokter");
                row[2] = rs.getInt("jumlah_konsultasi");
                row[3] = rs.getBigDecimal("total_pendapatan");
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}