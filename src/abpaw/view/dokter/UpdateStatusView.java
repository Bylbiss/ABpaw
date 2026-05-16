/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.PemesananController;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class UpdateStatusView extends JPanel {
    private Dokter dokter;
    private PemesananController pemesananController;
    private JTable tablePemesanan;
    private DefaultTableModel model;
    private JComboBox<String> cbStatusBaru;
    private JButton btnUpdate;

    public UpdateStatusView(Dokter dokter) {
        this.dokter = dokter;
        this.pemesananController = new PemesananController();
        initComponents();
        loadPemesanan();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Update Status Pemesanan Pasien", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        String[] cols = {"ID", "Tipe", "Kode/Nomor", "Pemilik", "Tanggal", "Status"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePemesanan = new JTable(model);
        tablePemesanan.getSelectionModel().addListSelectionListener(e -> {
            int row = tablePemesanan.getSelectedRow();
            if (row != -1) {
                String statusSekarang = (String) model.getValueAt(row, 5);
                // Set combo status yang sesuai
                cbStatusBaru.setSelectedItem(statusSekarang);
            }
        });
        JScrollPane sp = new JScrollPane(tablePemesanan);
        add(sp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomPanel.add(new JLabel("Ubah Status Menjadi:"));
        cbStatusBaru = new JComboBox<>(new String[]{"menunggu", "diproses", "selesai", "batal"});
        btnUpdate = new JButton("Update Status");
        btnUpdate.setBackground(new Color(0, 102, 204));
        btnUpdate.setForeground(Color.BLUE);
        btnUpdate.addActionListener(e -> updateStatus());
        bottomPanel.add(cbStatusBaru);
        bottomPanel.add(btnUpdate);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPemesanan() {
        model.setRowCount(0);
        // Pemesanan online
        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            model.addRow(new Object[]{
                    po.getIdTransaksi(), "Online", po.getKodePemesanan(),
                    "Pemilik ID " + po.getIdPemilik(), po.getTanggalKonsultasi(), po.getStatus()
            });
        }
        // Pemesanan offline
        List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByDokter(dokter.getIdDokter());
        for (PemesananOffline poff : offlineList) {
            model.addRow(new Object[]{
                    poff.getIdTransaksi(), "Offline", poff.getNomorAntrean(),
                    "Pemilik ID " + poff.getIdPemilik(), poff.getTanggalAntrean(), poff.getStatus()
            });
        }
    }

    private void updateStatus() {
        int row = tablePemesanan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pemesanan terlebih dahulu.");
            return;
        }
        int id = (int) model.getValueAt(row, 0);
        String tipe = (String) model.getValueAt(row, 1);
        String statusBaru = (String) cbStatusBaru.getSelectedItem();

        boolean success = false;
        if (tipe.equals("Online")) {
            success = pemesananController.updateStatusPemesananOnline(id, statusBaru);
        } else {
            success = pemesananController.updateStatusPemesananOffline(id, statusBaru);
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi " + statusBaru);
            loadPemesanan(); // refresh tabel
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status.");
        }
    }
}