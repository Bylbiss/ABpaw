/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.DokterController;
import abpaw.controller.PembelianObatController;
import abpaw.controller.PemesananController;
import abpaw.controller.PetsController;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.PembelianObat;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Pets;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class RiwayatView extends JPanel {
    private Pemilik pemilik;
    private PemesananController pemesananController;
    private DokterController dokterController;
    private PetsController petsController;
    private JTable tableRiwayat;
    private DefaultTableModel model;
    private JComboBox<String> filterTipe; // ubah dari filterStatus
    
    public RiwayatView(Pemilik pemilik) {
        this.pemilik = pemilik;
        this.pemesananController = new PemesananController();
        this.dokterController = new DokterController();
        this.petsController = new PetsController();
        initComponents();
        loadRiwayat();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("Filter Tipe:")); // ubah label
        filterTipe = new JComboBox<>(new String[]{"Semua", "Online", "Offline", "Pembelian Obat"});
        filterTipe.addActionListener(e -> loadRiwayat());
        topPanel.add(filterTipe);
        add(topPanel, BorderLayout.NORTH);
        
        String[] cols = {"ID", "Tipe", "Kode", "Dokter", "Hewan", "Tanggal", "Total", "Status", "idDokter"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        
        tableRiwayat = new JTable(model);
        
        // Sembunyikan kolom ID (indeks 0)
        tableRiwayat.getColumnModel().getColumn(0).setMinWidth(0);
        tableRiwayat.getColumnModel().getColumn(0).setMaxWidth(0);
        tableRiwayat.getColumnModel().getColumn(0).setWidth(0);
        
        // Sembunyikan kolom idDokter (indeks 8)
        tableRiwayat.getColumnModel().getColumn(8).setMinWidth(0);
        tableRiwayat.getColumnModel().getColumn(8).setMaxWidth(0);
        tableRiwayat.getColumnModel().getColumn(8).setWidth(0);
        
        JScrollPane sp = new JScrollPane(tableRiwayat);
        add(sp, BorderLayout.CENTER);
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDetail = new JButton("Detail / Struk");
        btnDetail.addActionListener(e -> showDetail());
        JButton btnChat = new JButton("Chat Dokter");
        btnChat.addActionListener(e -> chatFromRiwayat());
        bottomPanel.add(btnDetail);
        bottomPanel.add(btnChat);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private void loadRiwayat() {
        model.setRowCount(0);
        String selectedTipe = (String) filterTipe.getSelectedItem();
        
        // Tampilkan Online jika filter = "Semua" atau "Online"
        if (selectedTipe.equals("Semua") || selectedTipe.equals("Online")) {
            List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByPemilik(pemilik.getIdPemilik());
            for (PemesananOnline po : onlineList) {
                String namaDokter = "Dokter ID " + po.getIdDokter();
                String namaHewan = "Pet ID " + po.getIdPet();
                try {
                    Dokter d = dokterController.getDokterById(po.getIdDokter());
                    if (d != null) namaDokter = d.getNamaLengkap();
                    Pets p = petsController.getPetsById(po.getIdPet());
                    if (p != null) namaHewan = p.getNamaPet();
                } catch (Exception ex) { }
                model.addRow(new Object[]{
                    po.getIdTransaksi(), "Online", po.getKodePemesanan(),
                    namaDokter, namaHewan,
                    po.getTanggalKonsultasi(), po.getTotalBiaya(), po.getStatus(),
                    po.getIdDokter()
                });
            }
        }
        
        // Tampilkan Offline jika filter = "Semua" atau "Offline"
        if (selectedTipe.equals("Semua") || selectedTipe.equals("Offline")) {
            List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByPemilik(pemilik.getIdPemilik());
            for (PemesananOffline poff : offlineList) {
                String namaDokter = "Dokter ID " + poff.getIdDokter();
                String namaHewan = "Pet ID " + poff.getIdPet();
                try {
                    Dokter d = dokterController.getDokterById(poff.getIdDokter());
                    if (d != null) namaDokter = d.getNamaLengkap();
                    Pets p = petsController.getPetsById(poff.getIdPet());
                    if (p != null) namaHewan = p.getNamaPet();
                } catch (Exception ex) { }
                model.addRow(new Object[]{
                    poff.getIdTransaksi(), "Offline", poff.getNomorAntrean(),
                    namaDokter, namaHewan,
                    poff.getTanggalAntrean(), "-", poff.getStatus(),
                    poff.getIdDokter()
                });
            }
        }
        
        // Tampilkan Pembelian Obat jika filter = "Semua" atau "Pembelian Obat"
        if (selectedTipe.equals("Semua") || selectedTipe.equals("Pembelian Obat")) {
            PembelianObatController pembelianController = new PembelianObatController();
            List<PembelianObat> beliList = pembelianController.getPembelianByPemilik(pemilik.getIdPemilik());
            for (PembelianObat b : beliList) {
                model.addRow(new Object[]{
                    b.getIdPembelian(), "Pembelian Obat", b.getKodeTransaksi(),
                    "-", "-", b.getTanggal(), "Rp " + b.getTotalHarga(), b.getStatus(), null
                });
            }
        }
    }
    
    private void showDetail() {
        int row = tableRiwayat.getSelectedRow();
        if (row == -1) return;
        String tipe = (String) model.getValueAt(row, 1);
        int id = (int) model.getValueAt(row, 0);
        if (tipe.equals("Online")) {
            PemesananOnline po = pemesananController.getPemesananOnlineById(id);
            if (po != null) new StrukView((JFrame) SwingUtilities.getWindowAncestor(this), po);
        } else if (tipe.equals("Offline")) {
            PemesananOffline poff = pemesananController.getPemesananOfflineById(id);
            if (poff != null) new StrukView((JFrame) SwingUtilities.getWindowAncestor(this), poff);
        } else if (tipe.equals("Pembelian Obat")) {
            JOptionPane.showMessageDialog(this, "Detail pembelian obat dapat dilihat saat transaksi.");
        }
    }
    
    private void chatFromRiwayat() {
        int row = tableRiwayat.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih riwayat terlebih dahulu.");
            return;
        }
        int idDokter = (int) model.getValueAt(row, 8);
        String tipe = (String) model.getValueAt(row, 1);
        if (tipe.equals("Online")) {
            String status = (String) model.getValueAt(row, 7);
            if (status.equals("menunggu")) {
                JOptionPane.showMessageDialog(this, "Pembayaran belum selesai. Silakan selesaikan pembayaran terlebih dahulu.");
                return;
            }
        } else if (!tipe.equals("Online") && !tipe.equals("Offline")) {
            JOptionPane.showMessageDialog(this, "Chat hanya bisa dengan dokter dari pemesanan.");
            return;
        }
        DokterController dokterController = new DokterController();
        Dokter dokter = dokterController.getDokterById(idDokter);
        if (dokter != null) {
            new ChatDetailView(pemilik, dokter);
        } else {
            JOptionPane.showMessageDialog(this, "Data dokter tidak ditemukan.");
        }
    }
}