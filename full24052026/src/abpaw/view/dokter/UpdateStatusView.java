/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.PemesananController;
import abpaw.controller.PemilikController;
import abpaw.controller.ObatController;
import abpaw.controller.PembelianObatController;
import abpaw.controller.ResepController;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Obat;
import abpaw.model.entity.PembelianObat;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import abpaw.model.entity.Resep;
import abpaw.model.entity.DetailResep;
import abpaw.model.dao.DetailResepDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.math.BigDecimal;

public class UpdateStatusView extends JPanel {

    private Dokter dokter;
    private PemesananController pemesananController;
    private PemilikController pemilikController;
    private JTable tablePemesanan;
    private DefaultTableModel model;
    private JComboBox<String> cbStatusBaru;
    private JButton btnUpdate;
    private JButton btnRefresh;
    private JTextField txtBiayaJasa;

    public UpdateStatusView(Dokter dokter) {
        this.dokter = dokter;
        this.pemesananController = new PemesananController();
        this.pemilikController = new PemilikController();
        initComponents();
        loadPemesanan();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel title = new JLabel("Update Status Pemesanan Pasien", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        add(title, BorderLayout.NORTH);

        String[] cols = {"No", "Tipe", "Kode Pemesanan", "Pemilik", "Tanggal", "Status", "ID"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablePemesanan = new JTable(model);
        styleTable(tablePemesanan);
        tablePemesanan.getSelectionModel().addListSelectionListener(e -> {
            int row = tablePemesanan.getSelectedRow();
            if (row != -1) {
                String statusSekarang = (String) model.getValueAt(row, 5);
                cbStatusBaru.setSelectedItem(statusSekarang);
            }
        });
        JScrollPane sp = new JScrollPane(tablePemesanan);
        add(sp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JLabel lblBiayaJasa = new JLabel("Biaya Jasa (Rp):");
        txtBiayaJasa = new JTextField(8);
        txtBiayaJasa.setToolTipText("Isi untuk pemesanan offline");

        bottomPanel.add(lblBiayaJasa);
        bottomPanel.add(txtBiayaJasa);
        bottomPanel.add(new JLabel("Ubah Status Menjadi:"));

        cbStatusBaru = new JComboBox<>(new String[]{"menunggu", "diproses", "selesai", "batal"});

        btnUpdate = new JButton("Update Status");
        btnUpdate.setBackground(new Color(0, 128, 0));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setOpaque(true);
        btnUpdate.setContentAreaFilled(true);
        btnUpdate.addActionListener(e -> updateStatus());

        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(0, 128, 0));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setOpaque(true);
        btnRefresh.setContentAreaFilled(true);
        btnRefresh.addActionListener(e -> loadPemesanan());
        
        bottomPanel.add(cbStatusBaru);
        bottomPanel.add(btnUpdate);
        bottomPanel.add(btnRefresh);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void loadPemesanan() {
        model.setRowCount(0);
        int counter = 1;

        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            String namaPemilik = "Pemilik ID " + po.getIdPemilik();
            Pemilik p = pemilikController.getPemilikById(po.getIdPemilik());
            if (p != null) {
                namaPemilik = p.getNamaPemilik();
            }
            model.addRow(new Object[]{
                counter++, "Online", po.getKodePemesanan(),
                namaPemilik, po.getTanggalKonsultasi(), po.getStatus(), po.getIdTransaksi()
            });
        }

        List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByDokter(dokter.getIdDokter());
        for (PemesananOffline poff : offlineList) {
            String namaPemilik = "Pemilik ID " + poff.getIdPemilik();
            Pemilik p = pemilikController.getPemilikById(poff.getIdPemilik());
            if (p != null) {
                namaPemilik = p.getNamaPemilik();
            }
            model.addRow(new Object[]{
                counter++, "Offline", poff.getNomorAntrean(),
                namaPemilik, poff.getTanggalAntrean(), poff.getStatus(), poff.getIdTransaksi()
            });
        }

        if (model.getRowCount() > 0) {
            tablePemesanan.getColumnModel().getColumn(6).setMinWidth(0);
            tablePemesanan.getColumnModel().getColumn(6).setMaxWidth(0);
            tablePemesanan.getColumnModel().getColumn(6).setWidth(0);
        }
    }

    private void updateStatus() {
        int row = tablePemesanan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pemesanan terlebih dahulu.");
            return;
        }
        int id = (int) model.getValueAt(row, 6);
        String tipe = (String) model.getValueAt(row, 1);
        String statusBaru = (String) cbStatusBaru.getSelectedItem();

        BigDecimal biayaJasa = null;
        if (tipe.equals("Offline")) {
            String biayaStr = txtBiayaJasa.getText().trim();
            if (!biayaStr.isEmpty()) {
                try {
                    biayaJasa = new BigDecimal(biayaStr);
                    if (biayaJasa.compareTo(BigDecimal.ZERO) < 0) {
                        JOptionPane.showMessageDialog(this, "Biaya jasa tidak boleh negatif.");
                        return;
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Biaya jasa harus berupa angka.");
                    return;
                }
            }
        }

        boolean success = false;
        if (tipe.equals("Online")) {
            success = pemesananController.updateStatusPemesananOnline(id, statusBaru);
        } else {
            if (biayaJasa != null && biayaJasa.compareTo(BigDecimal.ZERO) > 0) {
                success = pemesananController.updateBiayaJasaOffline(id, biayaJasa);
            }
            if (success || biayaJasa == null) {
                success = pemesananController.updateStatusPemesananOffline(id, statusBaru);
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi " + statusBaru);
            loadPemesanan();
            txtBiayaJasa.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengubah status.");
        }
    }

    private void styleTable(JTable table) {
        Color hijauTua = new Color(0, 128, 0);
        Color hijauMuda = new Color(220, 255, 220);

        table.getTableHeader().setBackground(hijauTua);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(hijauMuda);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(hijauTua);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }
}
