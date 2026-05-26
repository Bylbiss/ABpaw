/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PemesananDAO;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import abpaw.model.dao.DetailBiayaOfflineDAO;
import abpaw.model.entity.DetailBiayaOffline;
import java.math.BigDecimal; 
import java.time.LocalDate;      
import java.time.LocalDateTime;
import java.time.LocalTime; 
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author LOQ
 */
public class PemesananController {
    private PemesananDAO pemesananDAO;
    private DetailBiayaOfflineDAO detailBiayaOfflineDAO;

    public PemesananController() {
        pemesananDAO = new PemesananDAO();
        detailBiayaOfflineDAO = new DetailBiayaOfflineDAO();
    }

    // Online
    public boolean createPemesananOnline(PemesananOnline pesan) {
        return pemesananDAO.insertOnline(pesan);
    }

    public boolean updatePemesananOnline(PemesananOnline pesan) {
        return pemesananDAO.updateOnline(pesan);
    }

    public boolean deletePemesananOnline(int id) {
        return pemesananDAO.deleteOnline(id);
    }

    public PemesananOnline getPemesananOnlineById(int id) {
        return pemesananDAO.getOnlineById(id);
    }

    public List<PemesananOnline> getAllPemesananOnline() {
        return pemesananDAO.getAllOnline();
    }

    public List<PemesananOnline> getPemesananOnlineByPemilik(int idPemilik) {
        return pemesananDAO.getOnlineByPemilik(idPemilik);
    }

    public List<PemesananOnline> getPemesananOnlineByDokter(int idDokter) {
        List<PemesananOnline> all = pemesananDAO.getAllOnline();
        if (all == null) return new ArrayList<>();
        return all.stream()
                  .filter(p -> p.getIdDokter() == idDokter)
                  .collect(Collectors.toList());
    }

    public boolean updateStatusPemesananOnline(int id, String statusBaru) {
        PemesananOnline po = getPemesananOnlineById(id);
        if (po == null) return false;
        po.setStatus(statusBaru);
        return pemesananDAO.updateOnline(po);
    }

    // Offline
    public boolean createPemesananOffline(PemesananOffline pesan) {
        return pemesananDAO.insertOffline(pesan);
    }

    public boolean updatePemesananOffline(PemesananOffline pesan) {
        return pemesananDAO.updateOffline(pesan);
    }

    public boolean deletePemesananOffline(int id) {
        return pemesananDAO.deleteOffline(id);
    }

    public PemesananOffline getPemesananOfflineById(int id) {
        return pemesananDAO.getOfflineById(id);
    }

    public List<PemesananOffline> getAllPemesananOffline() {
        return pemesananDAO.getAllOffline();
    }

    public List<PemesananOffline> getPemesananOfflineByPemilik(int idPemilik) {
        return pemesananDAO.getOfflineByPemilik(idPemilik);
    }

    public List<PemesananOffline> getPemesananOfflineByDokter(int idDokter) {
        List<PemesananOffline> all = pemesananDAO.getAllOffline();
        all.removeIf(p -> p.getIdDokter() != idDokter);
        return all;
    }
    
    public boolean updateStatusPemesananOffline(int id, String statusBaru) {
        PemesananOffline poff = getPemesananOfflineById(id);
        if (poff == null) return false;
        poff.setStatus(statusBaru);
        if ("selesai".equalsIgnoreCase(statusBaru)) {
            poff.setStatusPembayaran("lunas");
            System.out.println("=== AUTO SET LUNAS UNTUK ID " + id);
            System.out.println("=== updateStatusPemesananOffline: id=" + id + ", statusBaru=" + statusBaru);
        }
        return pemesananDAO.updateOffline(poff);
    }
    
    public boolean updateBiayaJasaOffline(int idAntrean, BigDecimal biayaJasa) {
        PemesananOffline poff = getPemesananOfflineById(idAntrean);
        if (poff == null) return false;
        poff.setBiayaJasa(biayaJasa);
        poff.setTotalBiaya(biayaJasa);
        // Update juga status_pembayaran jika biaya diisi
        if (biayaJasa != null && biayaJasa.compareTo(BigDecimal.ZERO) > 0) {
            poff.setStatusPembayaran("belum_bayar");
        }
        return pemesananDAO.updateOffline(poff);
    }

    // Method untuk update status pembayaran offline
    public boolean updateStatusPembayaranOffline(int idAntrean, String statusPembayaran) {
        PemesananOffline poff = getPemesananOfflineById(idAntrean);
        if (poff == null) return false;
        poff.setStatusPembayaran(statusPembayaran);
        if ("lunas".equalsIgnoreCase(statusPembayaran)) {
            poff.setStatus("selesai");
        }
        return pemesananDAO.updateOffline(poff);
    }
    
     // Tambahkan method ini di PemesananController.java
    //cek kuota booking 2/jam
    public int getJumlahBookingByJam(LocalDate tanggal, LocalTime jam, int idDokter) {
        List<PemesananOffline> semua = pemesananDAO.getAllOffline();
        int count = 0;
        for (PemesananOffline p : semua) {
            if (p.getTanggalAntrean().equals(tanggal) && 
                p.getWaktuAntrean().equals(jam) &&
                p.getIdDokter() == idDokter && 
                !"batal".equalsIgnoreCase(p.getStatus())) {
                count++;
            }
        }
        return count;
    }
    
    public void updateStatusOtomatis() {
        LocalDateTime now = LocalDateTime.now();

        List<PemesananOnline> onlineList = pemesananDAO.getAllOnline();
        for (PemesananOnline po : onlineList) {
            String status = po.getStatus();
            // Hanya yang sedang diproses atau sudah bayar
            if ("diproses".equalsIgnoreCase(status) || "sudah bayar".equalsIgnoreCase(status)) {
                LocalDateTime konsultasi = LocalDateTime.of(po.getTanggalKonsultasi(),
                        LocalTime.parse(po.getWaktuKonsultasi()));
                if (konsultasi.plusHours(1).isBefore(now)) {
                    po.setStatus("selesai");
                    pemesananDAO.updateOnline(po);
                    System.out.println("Update status online ID " + po.getIdTransaksi() + " menjadi selesai");
                }
            }
        }

        List<PemesananOffline> offlineList = pemesananDAO.getAllOffline();
        for (PemesananOffline poff : offlineList) {
            String status = poff.getStatus();
            if ("menunggu".equalsIgnoreCase(status) || "diproses".equalsIgnoreCase(status)) {
                LocalDateTime antrean = LocalDateTime.of(poff.getTanggalAntrean(), poff.getWaktuAntrean());
                if (antrean.plusHours(1).isBefore(now)) {
                    poff.setStatus("selesai");
                    pemesananDAO.updateOffline(poff);
                    System.out.println("Update status offline ID " + poff.getIdTransaksi() + " menjadi selesai");
                }
            }
        }
    }

    // Method untuk menambah biaya tambahan
    public boolean tambahBiayaTambahan(int idAntrean, String namaBiaya, int jumlah, BigDecimal hargaSatuan) {
        DetailBiayaOffline detail = new DetailBiayaOffline();
        detail.setIdAntrean(idAntrean);
        detail.setNamaBiaya(namaBiaya);
        detail.setJumlah(jumlah);
        detail.setHargaSatuan(hargaSatuan);
        BigDecimal subtotal = hargaSatuan.multiply(BigDecimal.valueOf(jumlah));
        detail.setSubtotal(subtotal);

        boolean success = detailBiayaOfflineDAO.insert(detail);
        if (success) {
            // Update total_biaya di pemesanan_offline
            updateTotalBiayaOffline(idAntrean);
        }
        return success;
    }

    // Method untuk menghapus biaya tambahan
    public boolean hapusBiayaTambahan(int idDetail, int idAntrean) {
        boolean success = detailBiayaOfflineDAO.delete(idDetail);
        if (success) {
            updateTotalBiayaOffline(idAntrean);
        }
        return success;
    }

    // Method untuk mendapatkan semua biaya tambahan
    public List<DetailBiayaOffline> getBiayaTambahan(int idAntrean) {
        return detailBiayaOfflineDAO.getByAntrean(idAntrean);
    }

    // Method untuk menghitung dan update total_biaya offline
    private void updateTotalBiayaOffline(int idAntrean) {
        PemesananOffline poff = pemesananDAO.getOfflineById(idAntrean);
        if (poff == null) return;

        BigDecimal biayaJasa = poff.getBiayaJasa() != null ? poff.getBiayaJasa() : BigDecimal.ZERO;
        List<DetailBiayaOffline> daftarBiaya = detailBiayaOfflineDAO.getByAntrean(idAntrean);

        BigDecimal totalTambahan = BigDecimal.ZERO;
        for (DetailBiayaOffline d : daftarBiaya) {
            totalTambahan = totalTambahan.add(d.getSubtotal());
        }

        BigDecimal total = biayaJasa.add(totalTambahan);
        poff.setTotalBiaya(total);
        pemesananDAO.updateOffline(poff);
    }

}