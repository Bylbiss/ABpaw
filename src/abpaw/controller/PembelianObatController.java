/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PembelianObatDAO;
import abpaw.model.entity.PembelianObat;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

public class PembelianObatController {
    private PembelianObatDAO dao;

    public PembelianObatController() {
        dao = new PembelianObatDAO();
    }

    public PembelianObat catatPembelian(int idPemilik, int idObat, int jumlah, java.math.BigDecimal hargaSatuan, java.math.BigDecimal totalHarga) {
        PembelianObat pb = new PembelianObat();
        pb.setIdPemilik(idPemilik);
        pb.setIdObat(idObat);
        pb.setJumlah(jumlah);
        pb.setHargaSatuan(hargaSatuan);
        pb.setTotalHarga(totalHarga);
        pb.setTanggal(Timestamp.valueOf(LocalDateTime.now()));
        pb.setStatus("sukses");
        pb.setKodeTransaksi("INV-OBT-" + System.currentTimeMillis());
        return dao.insert(pb);
    }

    public List<PembelianObat> getPembelianByPemilik(int idPemilik) {
        return dao.getByPemilik(idPemilik);
    }
}