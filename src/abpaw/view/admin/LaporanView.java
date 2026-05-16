/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.LaporanController;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LaporanView extends JPanel {
    private LaporanController laporanController;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbJenisLaporan;
    private JTextField txtStartDate, txtEndDate;
    private JButton btnGenerate;
    private JLabel totalLabel;

    public LaporanView() {
        laporanController = new LaporanController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Laporan"));
        cbJenisLaporan = new JComboBox<>(new String[]{"Pemesanan Online", "Pemesanan Offline", "Semua Transaksi", "Pendapatan per Dokter"});
        txtStartDate = new JTextField(LocalDate.now().minusDays(30).toString(), 10);
        txtEndDate = new JTextField(LocalDate.now().toString(), 10);
        btnGenerate = new JButton("Tampilkan");
        btnGenerate.addActionListener(e -> generateLaporan());
        filterPanel.add(new JLabel("Jenis:"));
        filterPanel.add(cbJenisLaporan);
        filterPanel.add(new JLabel("Dari Tgl:"));
        filterPanel.add(txtStartDate);
        filterPanel.add(new JLabel("Sampai Tgl:"));
        filterPanel.add(txtEndDate);
        filterPanel.add(btnGenerate);
        add(filterPanel, BorderLayout.NORTH);

        // Tabel laporan
        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(new JLabel("Total: "));
        totalLabel = new JLabel("Rp 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerPanel.add(totalLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void generateLaporan() {
        LocalDate start, end;
        try {
            start = LocalDate.parse(txtStartDate.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            end = LocalDate.parse(txtEndDate.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD");
            return;
        }
        String jenis = (String) cbJenisLaporan.getSelectedItem();
        BigDecimal total = BigDecimal.ZERO;
        switch (jenis) {
            case "Pemesanan Online":
                List<PemesananOnline> onlineList = laporanController.getPemesananOnlineByDateRange(start, end);
                displayOnline(onlineList);
                total = onlineList.stream().map(PemesananOnline::getTotalBiaya).reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            case "Pemesanan Offline":
                List<PemesananOffline> offlineList = laporanController.getPemesananOfflineByDateRange(start, end);
                displayOffline(offlineList);
                total = BigDecimal.ZERO;
                break;
            case "Semua Transaksi":
                List<PemesananOnline> allOnline = laporanController.getPemesananOnlineByDateRange(start, end);
                List<PemesananOffline> allOffline = laporanController.getPemesananOfflineByDateRange(start, end);
                displayAll(allOnline, allOffline);
                total = allOnline.stream().map(PemesananOnline::getTotalBiaya).reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            case "Pendapatan per Dokter":
                displayPendapatanPerDokter(start, end);
                break;
        }
    
        if (totalLabel != null) {
            totalLabel.setText("Rp " + total.toString());
        }
    }   

    private void displayOnline(List<PemesananOnline> list) {
        String[] cols = {"ID", "Kode", "Pemilik", "Dokter", "Tanggal", "Total", "Status"};
        model.setDataVector(new Object[0][0], cols);
        for (PemesananOnline po : list) {
            model.addRow(new Object[]{
                po.getIdTransaksi(), po.getKodePemesanan(),
                po.getIdPemilik(), po.getIdDokter(),
                po.getTanggalKonsultasi(), "Rp " + po.getTotalBiaya(), po.getStatus()
            });
        }
    }

    private void displayOffline(List<PemesananOffline> list) {
        String[] cols = {"ID", "Nomor Antrean", "Pemilik", "Dokter", "Tanggal", "Status"};
        model.setDataVector(new Object[0][0], cols);
        for (PemesananOffline poff : list) {
            model.addRow(new Object[]{
                poff.getIdTransaksi(), poff.getNomorAntrean(),
                poff.getIdPemilik(), poff.getIdDokter(),
                poff.getTanggalAntrean(), poff.getStatus()
            });
        }
    }

    private void displayAll(List<PemesananOnline> online, List<PemesananOffline> offline) {
        String[] cols = {"ID", "Tipe", "Kode/Nomor", "Pemilik", "Dokter", "Tanggal", "Total/Status"};
        model.setDataVector(new Object[0][0], cols);
        for (PemesananOnline po : online) {
            model.addRow(new Object[]{
                po.getIdTransaksi(), "Online", po.getKodePemesanan(),
                po.getIdPemilik(), po.getIdDokter(),
                po.getTanggalKonsultasi(), "Rp " + po.getTotalBiaya()
            });
        }
        for (PemesananOffline poff : offline) {
            model.addRow(new Object[]{
                poff.getIdTransaksi(), "Offline", poff.getNomorAntrean(),
                poff.getIdPemilik(), poff.getIdDokter(),
                poff.getTanggalAntrean(), poff.getStatus()
            });
        }
    }

    private void displayPendapatanPerDokter(LocalDate start, LocalDate end) {
        String[] cols = {"ID Dokter", "Nama Dokter", "Jumlah Konsultasi", "Total Pendapatan"};
        model.setDataVector(new Object[0][0], cols);
        List<Object[]> data = laporanController.getPendapatanPerDokter(start, end);
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
}