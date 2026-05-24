/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PembelianObatDAO;
import abpaw.model.entity.PembelianObat;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PembelianObatController {
    private PembelianObatDAO dao;

    public PembelianObatController() {
        dao = new PembelianObatDAO();
    }

    public PembelianObat catatPembelian(int idPemilik, int idObat, int jumlah, BigDecimal hargaSatuan, BigDecimal totalHarga) {
         System.out.println("Catat pembelian: idPemilik=" + idPemilik + ", idObat=" + idObat + ", jumlah=" + jumlah);

        PembelianObat pb = new PembelianObat();
        pb.setIdPemilik(idPemilik);
        pb.setIdObat(idObat);
        pb.setJumlah(jumlah);
        pb.setHargaSatuan(hargaSatuan);
        pb.setTotalHarga(totalHarga);
        pb.setTanggal(Timestamp.valueOf(LocalDateTime.now()));
        pb.setStatus("sukses");
        pb.setKodeTransaksi("INV-OBT-" + System.currentTimeMillis());
        System.out.println("Kode Transaksi: " + pb.getKodeTransaksi());

        PembelianObat result = dao.insert(pb);
        System.out.println("Hasil insert: " + (result != null ? "SUKSES, id=" + result.getIdPembelian() : "GAGAL"));
        return result;
    }

    public List<PembelianObat> getPembelianByPemilik(int idPemilik) {
        return dao.getByPemilik(idPemilik);
    }
    
    public PembelianObat getPembelianById(int id) {
        return dao.getById(id);
    }
}