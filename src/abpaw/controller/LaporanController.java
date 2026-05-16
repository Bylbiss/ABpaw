/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.PemesananDAO;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
}