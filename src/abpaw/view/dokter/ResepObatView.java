/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.ObatController;
import abpaw.controller.PemesananController;
import abpaw.controller.PemilikController;
import abpaw.controller.PetsController;
import abpaw.controller.ResepController;
import abpaw.model.entity.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder; 
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ResepObatView extends JPanel {
    private Dokter dokter;
    private ResepController resepController;
    private ObatController obatController;
    private PemesananController pemesananController;
    private PemilikController pemilikController;
    private PetsController petsController;
    private JTable tablePemesanan;
    private DefaultTableModel modelPemesanan;
    private JTable tableResep;
    private DefaultTableModel modelResep;
    private JButton btnBuatResep, btnDetailResep;

    public ResepObatView(Dokter dokter) {
        this.dokter = dokter;
        this.resepController = new ResepController();
        this.obatController = new ObatController();
        this.pemesananController = new PemesananController();
        this.pemilikController = new PemilikController();  
        this.petsController = new PetsController();
        initComponents();
        loadPemesananSelesai();
        loadResepDokter();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JTabbedPane tabbedPane = new JTabbedPane();

        // Tab 1: Buat Resep dari Pemesanan Selesai
        JPanel buatPanel = new JPanel(new BorderLayout(5, 5));
        String[] colsPesan = {"ID Pemesanan", "Tipe", "Pemilik", "Pet", "Tanggal"};
        modelPemesanan = new DefaultTableModel(colsPesan, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tablePemesanan = new JTable(modelPemesanan);
        JScrollPane spPesan = new JScrollPane(tablePemesanan);
        btnBuatResep = new JButton("Buat Resep untuk Pemesanan Terpilih");
        btnBuatResep.addActionListener(e -> buatResep());
        buatPanel.add(spPesan, BorderLayout.CENTER);
        buatPanel.add(btnBuatResep, BorderLayout.SOUTH);
        tabbedPane.addTab("Buat Resep", buatPanel);

        // Tab 2: Daftar Resep yang Sudah Dibuat
        JPanel daftarPanel = new JPanel(new BorderLayout(5, 5));
        String[] colsResep = {"ID Resep", "Tipe Pemesanan", "Tanggal", "Status"};
        modelResep = new DefaultTableModel(colsResep, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableResep = new JTable(modelResep);
        JScrollPane spResep = new JScrollPane(tableResep);
        btnDetailResep = new JButton("Detail / Edit Resep");
        btnDetailResep.addActionListener(e -> detailResep());
        daftarPanel.add(spResep, BorderLayout.CENTER);
        daftarPanel.add(btnDetailResep, BorderLayout.SOUTH);
        tabbedPane.addTab("Resep Saya", daftarPanel);

        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadPemesananSelesai() {
        modelPemesanan.setRowCount(0);
        
        // Pemesanan Online
        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            if (!"batal".equalsIgnoreCase(po.getStatus())) {
                // Ambil nama pemilik
                Pemilik p = pemilikController.getPemilikById(po.getIdPemilik());
                String namaPemilik = (p != null) ? p.getNamaPemilik() : "ID: " + po.getIdPemilik();
                // Ambil nama hewan
                Pets pet = petsController.getPetsById(po.getIdPet());
                String namaPet = (pet != null) ? pet.getNamaPet() : "ID: " + po.getIdPet();

                modelPemesanan.addRow(new Object[]{
                    po.getIdTransaksi(), "Online",
                    namaPemilik, namaPet,
                    po.getTanggalKonsultasi()
                });
            }
        }

        // Pemesanan Offline
        List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByDokter(dokter.getIdDokter());
        for (PemesananOffline poff : offlineList) {
            if (!"batal".equalsIgnoreCase(poff.getStatus())) {
                Pemilik p = pemilikController.getPemilikById(poff.getIdPemilik());
                String namaPemilik = (p != null) ? p.getNamaPemilik() : "ID: " + poff.getIdPemilik();
                Pets pet = petsController.getPetsById(poff.getIdPet());
                String namaPet = (pet != null) ? pet.getNamaPet() : "ID: " + poff.getIdPet();

                modelPemesanan.addRow(new Object[]{
                    poff.getIdTransaksi(), "Offline",
                    namaPemilik, namaPet,
                    poff.getTanggalAntrean()
                });
            }
    }
    
    // Jika tabel masih kosong, tampilkan pesan
    if (modelPemesanan.getRowCount() == 0) {
        modelPemesanan.addRow(new Object[]{"-", "-", "Tidak ada pemesanan", "-", "-"});
    }
    }

    private void loadResepDokter() {
        modelResep.setRowCount(0);
        List<Resep> resepList = resepController.getResepByDokter(dokter.getIdDokter());
        for (Resep r : resepList) {
            modelResep.addRow(new Object[]{
                    r.getIdResep(), r.getTipePemesanan(), r.getTanggalResep(), r.getStatus()
            });
        }
    }

    private void buatResep() {
        int row = tablePemesanan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pemesanan terlebih dahulu.");
            return;
        }
        int idPemesanan = (int) modelPemesanan.getValueAt(row, 0);
        String tipe = (String) modelPemesanan.getValueAt(row, 1);

        // Buka dialog pembuatan resep
        BuatResepDialog dialog = new BuatResepDialog((JFrame) SwingUtilities.getWindowAncestor(this), dokter, idPemesanan, tipe);
        dialog.setVisible(true);
        // Refresh setelah dialog ditutup
        loadResepDokter();
    }

    private void detailResep() {
        int row = tableResep.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih resep terlebih dahulu.");
            return;
        }
        int idResep = (int) modelResep.getValueAt(row, 0);
        // Buka dialog detail resep (bisa edit status, tambah obat, dll)
        JOptionPane.showMessageDialog(this, "Detail resep ID " + idResep + "\nFitur edit akan segera hadir.");
    }

    // Inner class dialog buat resep
    private class BuatResepDialog extends JDialog {
        private int idPemesanan;
        private String tipePemesanan;
        private DefaultTableModel modelDetail;
        private JTable tableObatTersedia;
        private DefaultTableModel modelObatTersedia;
        private List<Obat> semuaObat;
        private JTextField searchField;

        public BuatResepDialog(JFrame parent, Dokter dokter, int idPemesanan, String tipe) {
            super(parent, "Buat Resep", true);
            this.idPemesanan = idPemesanan;
            this.tipePemesanan = tipe;
            this.semuaObat = obatController.getAllObat();
            initComponents();
            setSize(950, 650);  // Diperbesar
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            setLayout(new BorderLayout(5, 5));
            JPanel mainPanel = new JPanel(new BorderLayout());
            mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

            // ========== PANEL KIRI: Pilih Obat ==========
            JPanel leftPanel = new JPanel(new BorderLayout(5, 5));

            // Panel pencarian
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(new JLabel("Cari Obat:"));
            searchField = new JTextField(20);
            JButton searchBtn = new JButton("Cari");
            searchBtn.addActionListener(e -> filterObat());
            searchField.addActionListener(e -> filterObat());
            searchPanel.add(searchField);
            searchPanel.add(searchBtn);
            leftPanel.add(searchPanel, BorderLayout.NORTH);

            // Tabel obat (kolom ID disembunyikan, tanpa kolom Perlu Resep)
            String[] colsObat = {"ID", "Nama Obat", "Bentuk", "Harga"};
            modelObatTersedia = new DefaultTableModel(colsObat, 0) {
                @Override
                public boolean isCellEditable(int row, int col) { return false; }
            };
            refreshTabelObat(""); // isi semua obat

            tableObatTersedia = new JTable(modelObatTersedia);
            tableObatTersedia.getColumnModel().getColumn(0).setMinWidth(0);
            tableObatTersedia.getColumnModel().getColumn(0).setMaxWidth(0);
            tableObatTersedia.getColumnModel().getColumn(0).setWidth(0);

            JScrollPane spObat = new JScrollPane(tableObatTersedia);
            spObat.setBorder(BorderFactory.createTitledBorder("Pilih Obat"));
            leftPanel.add(spObat, BorderLayout.CENTER);

            // Tombol tambah ke resep
            JButton btnTambah = new JButton(">> Tambah Obat ke Resep");
            btnTambah.addActionListener(e -> tambahObat());
            leftPanel.add(btnTambah, BorderLayout.SOUTH);

            // ========== PANEL KANAN: Detail Resep ==========
            String[] colsDetail = {"ID Obat", "Nama Obat", "Takaran", "Jumlah"};
            modelDetail = new DefaultTableModel(colsDetail, 0);
            JTable tableDetail = new JTable(modelDetail);
            tableDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tableDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tableDetail.getColumnModel().getColumn(0).setWidth(0);
            JScrollPane spDetail = new JScrollPane(tableDetail);
            spDetail.setBorder(BorderFactory.createTitledBorder("Detail Resep"));

            // Panel tengah dengan dua sisi
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, spDetail);
            splitPane.setResizeWeight(0.5);
            splitPane.setDividerLocation(450);

            // ========== BOTTOM PANEL ==========
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSimpan = new JButton("Simpan Resep");
            btnSimpan.addActionListener(e -> simpanResep());
            bottomPanel.add(btnSimpan);

            mainPanel.add(splitPane, BorderLayout.CENTER);
            mainPanel.add(bottomPanel, BorderLayout.SOUTH);
            add(mainPanel);
        }

        private void filterObat() {
            String keyword = searchField.getText().trim().toLowerCase();
            refreshTabelObat(keyword);
        }

        private void refreshTabelObat(String keyword) {
            modelObatTersedia.setRowCount(0);
            for (Obat o : semuaObat) {
                if (keyword.isEmpty() || o.getNamaObat().toLowerCase().contains(keyword)) {
                    modelObatTersedia.addRow(new Object[]{
                        o.getIdObat(),
                        o.getNamaObat(),
                        o.getBentukObat(),
                        "Rp " + o.getHarga()
                    });
                }
            }
        }

        private void tambahObat() {
            int row = tableObatTersedia.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Pilih obat terlebih dahulu.");
                return;
            }
            int idObat = (int) modelObatTersedia.getValueAt(row, 0);
            String namaObat = (String) modelObatTersedia.getValueAt(row, 1);
            String takaran = JOptionPane.showInputDialog(this, "Takaran (contoh: 2x1 sehari):");
            if (takaran == null || takaran.trim().isEmpty()) return;
            String jumlahStr = JOptionPane.showInputDialog(this, "Jumlah yang diberikan:");
            if (jumlahStr == null) return;
            int jumlah;
            try {
                jumlah = Integer.parseInt(jumlahStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus angka.");
                return;
            }
            modelDetail.addRow(new Object[]{idObat, namaObat, takaran, jumlah});
        }

        private void simpanResep() {
            if (modelDetail.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Minimal tambah satu obat.");
                return;
            }
            Resep resep = new Resep();
            resep.setIdPemesanan(idPemesanan);
            resep.setTipePemesanan(tipePemesanan);
            resep.setIdDokter(dokter.getIdDokter());
            resep.setTanggalResep(Date.valueOf(LocalDate.now()));
            resep.setStatus("belum_diproses");

            boolean success = resepController.createResep(resep, modelDetail.getDataVector());
            if (success) {
                JOptionPane.showMessageDialog(this, "Resep berhasil disimpan.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan resep.");
            }
        }
    }  
}