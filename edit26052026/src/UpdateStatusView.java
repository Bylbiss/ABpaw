/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.PemesananController;
import abpaw.controller.PemilikController;
import abpaw.model.entity.*;
import abpaw.model.entity.DetailBiayaOffline;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class UpdateStatusView extends JPanel {

    private Dokter dokter;
    private PemesananController pemesananController;
    private PemilikController pemilikController;
    private JTable tablePemesanan;
    private DefaultTableModel model;
    private JComboBox<String> cbStatusBaru;
    private JButton btnUpdate;
    private JButton btnRefresh;
    
    // Komponen untuk biaya tambahan offline
    private JPanel biayaTambahanPanel;
    private JTable tableBiayaTambahan;
    private DefaultTableModel modelBiaya;
    private JTextField txtNamaBiaya, txtHargaSatuan, txtJumlah;
    private JButton btnTambahBiaya, btnHapusBiaya;
    private JLabel lblBiayaJasaDasar, lblTotalBiaya;
    private int selectedAntreanId = -1;
    private BigDecimal biayaJasaDasar = BigDecimal.ZERO;

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

        // Split pane: kiri tabel pemesanan, kanan panel biaya tambahan
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        
        // ========== PANEL KIRI: TABEL PEMESANAN ==========
        JPanel leftPanel = new JPanel(new BorderLayout());
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
                
                // Load biaya tambahan jika offline
                String tipe = (String) model.getValueAt(row, 1);
                if ("Offline".equals(tipe)) {
                    selectedAntreanId = (int) model.getValueAt(row, 6);
                    loadBiayaTambahan(selectedAntreanId);
                    biayaTambahanPanel.setVisible(true);
                } else {
                    biayaTambahanPanel.setVisible(false);
                }
            }
        });
        JScrollPane sp = new JScrollPane(tablePemesanan);
        leftPanel.add(sp, BorderLayout.CENTER);
        
        // Panel bawah kiri (update status)
        JPanel bottomLeftPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bottomLeftPanel.add(new JLabel("Ubah Status Menjadi:"));
        cbStatusBaru = new JComboBox<>(new String[]{"menunggu", "diproses", "selesai", "batal"});
        btnUpdate = new JButton("Update Status");
        btnUpdate.setBackground(new Color(0, 128, 0));
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.addActionListener(e -> updateStatus());
        btnRefresh = new JButton("Refresh");
        btnRefresh.setBackground(new Color(0, 128, 0));
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.addActionListener(e -> loadPemesanan());
        bottomLeftPanel.add(cbStatusBaru);
        bottomLeftPanel.add(btnUpdate);
        bottomLeftPanel.add(btnRefresh);
        leftPanel.add(bottomLeftPanel, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(leftPanel);
        
        // ========== PANEL KANAN: BIAYA TAMBAHAN OFFLINE ==========
        biayaTambahanPanel = new JPanel(new BorderLayout(5, 5));
        biayaTambahanPanel.setBorder(BorderFactory.createTitledBorder("Biaya Penanganan Tambahan (Offline)"));
        biayaTambahanPanel.setVisible(false);
        
        // Info biaya jasa dasar
        JPanel infoPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        infoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        infoPanel.add(new JLabel("Biaya Jasa Dasar:"));
        lblBiayaJasaDasar = new JLabel("Rp 0");
        lblBiayaJasaDasar.setForeground(new Color(0, 100, 0));
        infoPanel.add(lblBiayaJasaDasar);
        infoPanel.add(new JLabel("Total Biaya:"));
        lblTotalBiaya = new JLabel("Rp 0");
        lblTotalBiaya.setFont(new Font("Arial", Font.BOLD, 14));
        lblTotalBiaya.setForeground(new Color(0, 150, 0));
        infoPanel.add(lblTotalBiaya);
        biayaTambahanPanel.add(infoPanel, BorderLayout.NORTH);
        
        // Tabel daftar biaya tambahan
        String[] colsBiaya = {"ID", "Nama Biaya", "Jumlah", "Harga Satuan", "Subtotal"};
        modelBiaya = new DefaultTableModel(colsBiaya, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableBiayaTambahan = new JTable(modelBiaya);
        tableBiayaTambahan.getColumnModel().getColumn(0).setMinWidth(0);
        tableBiayaTambahan.getColumnModel().getColumn(0).setMaxWidth(0);
        tableBiayaTambahan.getColumnModel().getColumn(0).setWidth(0);
        JScrollPane spBiaya = new JScrollPane(tableBiayaTambahan);
        spBiaya.setPreferredSize(new Dimension(300, 150));
        biayaTambahanPanel.add(spBiaya, BorderLayout.CENTER);
        
        // Panel input biaya tambahan
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 5, 5));
        inputPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        inputPanel.add(new JLabel("Nama Biaya:"));
        txtNamaBiaya = new JTextField();
        inputPanel.add(txtNamaBiaya);
        inputPanel.add(new JLabel("Jumlah:"));
        txtJumlah = new JTextField("1");
        inputPanel.add(txtJumlah);
        inputPanel.add(new JLabel("Harga Satuan (Rp):"));
        txtHargaSatuan = new JTextField();
        inputPanel.add(txtHargaSatuan);
        
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        btnTambahBiaya = new JButton("+ Tambah Biaya");
        btnTambahBiaya.setBackground(new Color(0, 102, 204));
        btnTambahBiaya.setForeground(Color.WHITE);
        btnTambahBiaya.addActionListener(e -> tambahBiayaTambahan());
        btnHapusBiaya = new JButton("Hapus Biaya Terpilih");
        btnHapusBiaya.setBackground(new Color(220, 20, 60));
        btnHapusBiaya.setForeground(Color.WHITE);
        btnHapusBiaya.addActionListener(e -> hapusBiayaTambahan());
        btnPanel.add(btnTambahBiaya);
        btnPanel.add(btnHapusBiaya);
        
        biayaTambahanPanel.add(inputPanel, BorderLayout.SOUTH);
        biayaTambahanPanel.add(btnPanel, BorderLayout.NORTH);
        
        splitPane.setRightComponent(biayaTambahanPanel);
        
        add(splitPane, BorderLayout.CENTER);
    }

    public void loadPemesanan() {
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

    private void loadBiayaTambahan(int idAntrean) {
        modelBiaya.setRowCount(0);
        
        // Ambil data pemesanan offline untuk mendapatkan biaya jasa dasar
        PemesananOffline poff = pemesananController.getPemesananOfflineById(idAntrean);
        if (poff != null) {
            biayaJasaDasar = poff.getBiayaJasa() != null ? poff.getBiayaJasa() : BigDecimal.ZERO;
            lblBiayaJasaDasar.setText("Rp " + biayaJasaDasar.toString());
        }
        
        List<DetailBiayaOffline> daftar = pemesananController.getBiayaTambahan(idAntrean);
        BigDecimal totalTambahan = BigDecimal.ZERO;
        for (DetailBiayaOffline d : daftar) {
            modelBiaya.addRow(new Object[]{
                d.getIdDetail(),
                d.getNamaBiaya(),
                d.getJumlah(),
                "Rp " + d.getHargaSatuan(),
                "Rp " + d.getSubtotal()
            });
            totalTambahan = totalTambahan.add(d.getSubtotal());
        }
        
        BigDecimal total = biayaJasaDasar.add(totalTambahan);
        lblTotalBiaya.setText("Rp " + total.toString());
    }

    private void tambahBiayaTambahan() {
        if (selectedAntreanId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pemesanan offline terlebih dahulu.");
            return;
        }
        
        String namaBiaya = txtNamaBiaya.getText().trim();
        if (namaBiaya.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama biaya harus diisi.");
            return;
        }
        
        int jumlah;
        try {
            jumlah = Integer.parseInt(txtJumlah.getText().trim());
            if (jumlah <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah harus angka positif.");
            return;
        }
        
        BigDecimal hargaSatuan;
        try {
            hargaSatuan = new BigDecimal(txtHargaSatuan.getText().trim());
            if (hargaSatuan.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Harga satuan harus angka positif.");
            return;
        }
        
        boolean success = pemesananController.tambahBiayaTambahan(selectedAntreanId, namaBiaya, jumlah, hargaSatuan);
        if (success) {
            JOptionPane.showMessageDialog(this, "Biaya tambahan berhasil ditambahkan.");
            txtNamaBiaya.setText("");
            txtJumlah.setText("1");
            txtHargaSatuan.setText("");
            loadBiayaTambahan(selectedAntreanId);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menambahkan biaya.");
        }
    }

    private void hapusBiayaTambahan() {
        int row = tableBiayaTambahan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih biaya yang akan dihapus.");
            return;
        }
        
        int idDetail = (int) modelBiaya.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus biaya ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = pemesananController.hapusBiayaTambahan(idDetail, selectedAntreanId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Biaya berhasil dihapus.");
                loadBiayaTambahan(selectedAntreanId);
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus biaya.");
            }
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

        boolean success = false;
        if (tipe.equals("Online")) {
            success = pemesananController.updateStatusPemesananOnline(id, statusBaru);
        } else {
            success = pemesananController.updateStatusPemesananOffline(id, statusBaru);
            // Jika status menjadi selesai, set status_pembayaran = lunas
            if ("selesai".equalsIgnoreCase(statusBaru)) {
                pemesananController.updateStatusPembayaranOffline(id, "lunas");
            }
        }

        if (success) {
            JOptionPane.showMessageDialog(this, "Status berhasil diubah menjadi " + statusBaru);
            loadPemesanan();
            if (tipe.equals("Offline") && selectedAntreanId == id) {
                loadBiayaTambahan(selectedAntreanId);
            }
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