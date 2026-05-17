/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.AlergiController;
import abpaw.controller.ObatController;
import abpaw.controller.PemesananController;
import abpaw.controller.PemilikController;
import abpaw.controller.PetsController;
import abpaw.controller.ResepController;
import abpaw.model.dao.DetailResepDAO;
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
    private JButton btnRefreshPemesanan;

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
        
        // Panel atas untuk tombol refresh
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefreshPemesanan = new JButton("Refresh Daftar Pemesanan");
        btnRefreshPemesanan.addActionListener(e -> loadPemesananSelesai());
        topPanel.add(btnRefreshPemesanan);
        buatPanel.add(topPanel, BorderLayout.NORTH);
        
        String[] colsPesan = {"No", "Tipe Pemesanan", "Pemilik", "Hewan", "Tanggal", "ID"};
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
        String[] colsResep = {"No", "Tipe Pemesanan", "Tanggal", "Status", "ID"};
        modelResep = new DefaultTableModel(colsResep, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableResep = new JTable(modelResep);
        JScrollPane spResep = new JScrollPane(tableResep);
        btnDetailResep = new JButton("Detail Resep");
        btnDetailResep.addActionListener(e -> detailResep());
        daftarPanel.add(spResep, BorderLayout.CENTER);
        daftarPanel.add(btnDetailResep, BorderLayout.SOUTH);
        tabbedPane.addTab("Resep Saya", daftarPanel);

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            // Jika tab yang dipilih adalah "Resep Saya" (index 1)
            if (selectedIndex == 1) {
                loadResepDokter(); // refresh daftar resep
            }
        });
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void loadPemesananSelesai() {
        modelPemesanan.setRowCount(0);
        int counter = 1;
        
        // Pemesanan Online
        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            if ("diproses".equalsIgnoreCase(po.getStatus())) {
                Pemilik p = pemilikController.getPemilikById(po.getIdPemilik());
                String namaPemilik = (p != null) ? p.getNamaPemilik() : "ID: " + po.getIdPemilik();
                Pets pet = petsController.getPetsById(po.getIdPet());
                String namaPet = (pet != null) ? pet.getNamaPet() : "ID: " + po.getIdPet();

                modelPemesanan.addRow(new Object[]{
                    counter++,                  // Nomor urut
                    "Online",
                    namaPemilik,
                    namaPet,
                    po.getTanggalKonsultasi(),
                    po.getIdTransaksi()         // ID pemesanan (disembunyikan nanti)
                });
            }
        }

        // Pemesanan Offline
        List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByDokter(dokter.getIdDokter());
        for (PemesananOffline poff : offlineList) {
            if ("diproses".equalsIgnoreCase(poff.getStatus())) {
                Pemilik p = pemilikController.getPemilikById(poff.getIdPemilik());
                String namaPemilik = (p != null) ? p.getNamaPemilik() : "ID: " + poff.getIdPemilik();
                Pets pet = petsController.getPetsById(poff.getIdPet());
                String namaPet = (pet != null) ? pet.getNamaPet() : "ID: " + poff.getIdPet();

                modelPemesanan.addRow(new Object[]{
                    counter++,
                    "Offline",
                    namaPemilik,
                    namaPet,
                    poff.getTanggalAntrean(),
                    poff.getIdTransaksi()
                });
            }
        }

        // Jika kosong
        if (modelPemesanan.getRowCount() == 0) {
            modelPemesanan.addRow(new Object[]{"-", "-", "Tidak ada pemesanan", "-", "-", null});
        }

        // Sembunyikan kolom terakhir (ID pemesanan) - indeks 5
        tablePemesanan.getColumnModel().getColumn(5).setMinWidth(0);
        tablePemesanan.getColumnModel().getColumn(5).setMaxWidth(0);
        tablePemesanan.getColumnModel().getColumn(5).setWidth(0);
    }

    private void loadResepDokter() {
        modelResep.setRowCount(0);
        List<Resep> resepList = resepController.getResepByDokter(dokter.getIdDokter());
        int counter = 1;
        for (Resep r : resepList) {
            modelResep.addRow(new Object[]{
                counter++,                     // No urut
                r.getTipePemesanan(),
                r.getTanggalResep(),
                r.getStatus(),
                r.getIdResep()                 // ID (akan disembunyikan)
            });
        }
        // Sembunyikan kolom ID (indeks 4)
        if (modelResep.getRowCount() > 0) {
            tableResep.getColumnModel().getColumn(4).setMinWidth(0);
            tableResep.getColumnModel().getColumn(4).setMaxWidth(0);
            tableResep.getColumnModel().getColumn(4).setWidth(0);
        }
    }

    private void buatResep() {
        int row = tablePemesanan.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pemesanan terlebih dahulu.");
            return;
        }
        int idPemesanan = (int) modelPemesanan.getValueAt(row, 5);
        String tipe = (String) modelPemesanan.getValueAt(row, 1);

        // Ambil idPet dan namaPet dari database berdasarkan pemesanan
        int idPet = -1;
        String namaPet = "";
        if (tipe.equals("Online")) {
            PemesananOnline po = pemesananController.getPemesananOnlineById(idPemesanan);
            if (po != null) {
                idPet = po.getIdPet();
                Pets pet = petsController.getPetsById(idPet);
                if (pet != null) namaPet = pet.getNamaPet();
            }
        } else {
            PemesananOffline poff = pemesananController.getPemesananOfflineById(idPemesanan);
            if (poff != null) {
                idPet = poff.getIdPet();
                Pets pet = petsController.getPetsById(idPet);
                if (pet != null) namaPet = pet.getNamaPet();
            }
        }

        BuatResepDialog dialog = new BuatResepDialog((JFrame) SwingUtilities.getWindowAncestor(this), dokter, idPemesanan, tipe, idPet, namaPet);
        dialog.setVisible(true);
        loadResepDokter();
        loadPemesananSelesai();
    }

    private void detailResep() {
        int row = tableResep.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih resep terlebih dahulu.");
            return;
        }
        int idResep = (int) modelResep.getValueAt(row, 4);
        String tipe = (String) modelResep.getValueAt(row, 1);
        Date tanggal = (Date) modelResep.getValueAt(row, 2);
        String status = (String) modelResep.getValueAt(row, 3);

        // Ambil detail resep dari database
        DetailResepDAO detailDao = new DetailResepDAO();
        List<DetailResep> detailList = detailDao.getByResep(idResep);

        JDialog detailDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Detail Resep", true);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.setSize(550, 450);
        detailDialog.setLocationRelativeTo(this);

        // Panel informasi resep (sama seperti sebelumnya)
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        infoPanel.add(new JLabel("ID Resep:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.valueOf(idResep)), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        infoPanel.add(new JLabel("Tipe Pemesanan:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(tipe), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        infoPanel.add(new JLabel("Tanggal Resep:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(tanggal.toString()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        infoPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(status), gbc);

        // Tabel daftar obat
        String[] cols = {"Nama Obat", "Takaran", "Jumlah"};
        DefaultTableModel modelObat = new DefaultTableModel(cols, 0);
        JTable tableObat = new JTable(modelObat);
        JScrollPane spObat = new JScrollPane(tableObat);
        spObat.setBorder(BorderFactory.createTitledBorder("Daftar Obat yang Diresepkan"));

        // Isi tabel dari detailList
        for (DetailResep d : detailList) {
            // Ambil nama obat dari idObat
            Obat o = obatController.getObatById(d.getIdObat());
            String namaObat = (o != null) ? o.getNamaObat() : "Obat ID " + d.getIdObat();
            modelObat.addRow(new Object[]{namaObat, d.getTakaran(), d.getJumlah()});
        }

        JPanel bottomPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> detailDialog.dispose());
        bottomPanel.add(btnOK);

        detailDialog.add(infoPanel, BorderLayout.NORTH);
        detailDialog.add(spObat, BorderLayout.CENTER);
        detailDialog.add(bottomPanel, BorderLayout.SOUTH);

        detailDialog.setVisible(true);
    }

    // Inner class dialog buat resep
    private class BuatResepDialog extends JDialog {
        private int idPemesanan;
        private int idPet;
        private String namaPet;
        private AlergiController alergiController;
        private String tipePemesanan;
        private DefaultTableModel modelDetail;
        private JTable tableObatTersedia;
        private DefaultTableModel modelObatTersedia;
        private List<Obat> semuaObat;
        private JTextField searchField;

        public BuatResepDialog(JFrame parent, Dokter dokter, int idPemesanan, String tipe, int idPet, String namaPet) {
            super(parent, "Buat Resep", true);
            this.idPemesanan = idPemesanan;
            this.tipePemesanan = tipe;
            this.idPet = idPet;
            this.namaPet = namaPet;
            this.semuaObat = obatController.getAllObat();
            this.alergiController = new AlergiController();
            initComponents();
            setSize(950, 650);
            setLocationRelativeTo(parent);
        }
        
        private boolean isObatAlergi(int idObat) {
            List<Alergi> alergiList = alergiController.getAlergiByPet(idPet);
            for (Alergi a : alergiList) {
                if (a.getIdObat() == idObat) {
                    return true;
                }
            }
            return false;
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
            refreshTabelObat(""); 

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

            // Panel informasi hewan dan alergi
            JPanel infoPetPanel = new JPanel(new GridBagLayout());
            infoPetPanel.setBorder(BorderFactory.createTitledBorder("Informasi Hewan & Alergi"));
            infoPetPanel.setBackground(Color.WHITE);
            GridBagConstraints gbcInfo = new GridBagConstraints();
            gbcInfo.insets = new Insets(5, 5, 5, 5);
            gbcInfo.fill = GridBagConstraints.HORIZONTAL;
            gbcInfo.anchor = GridBagConstraints.WEST;

            gbcInfo.gridx = 0; gbcInfo.gridy = 0;
            infoPetPanel.add(new JLabel("Nama Hewan:"), gbcInfo);
            gbcInfo.gridx = 1;
            infoPetPanel.add(new JLabel(namaPet), gbcInfo);

            gbcInfo.gridx = 0; gbcInfo.gridy = 1;
            infoPetPanel.add(new JLabel("Alergi yang tercatat:"), gbcInfo);
            gbcInfo.gridx = 1;
            JTextArea txtAlergi = new JTextArea(3, 20);
            txtAlergi.setEditable(false);
            txtAlergi.setLineWrap(true);
            txtAlergi.setWrapStyleWord(true);
            txtAlergi.setBackground(infoPetPanel.getBackground());

            // Ambil data alergi dari database
            List<Alergi> alergiList = alergiController.getAlergiByPet(idPet);
            StringBuilder alergiText = new StringBuilder();
            if (alergiList.isEmpty()) {
                alergiText.append("Tidak ada alergi yang tercatat.");
            } else {
                for (Alergi a : alergiList) {
                    Obat o = obatController.getObatById(a.getIdObat());
                    alergiText.append(a.getNamaAlergi() != null ? a.getNamaAlergi() : "").append("\n");
                }
            }
            txtAlergi.setText(alergiText.toString());
            JScrollPane spAlergi = new JScrollPane(txtAlergi);
            spAlergi.setPreferredSize(new Dimension(300, 60));
            gbcInfo.gridx = 1;
            infoPetPanel.add(spAlergi, gbcInfo);

            // Tambahkan infoPetPanel ke bagian atas mainPanel
            mainPanel.add(infoPetPanel, BorderLayout.NORTH);

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

            // Cek alergi
            if (isObatAlergi(idObat)) {
                int confirm = JOptionPane.showConfirmDialog(this,
                    "⚠️ PERINGATAN ⚠️\n\nHewan " + namaPet + " memiliki riwayat alergi terhadap obat:\n" + namaObat + "\n\nTetap tambahkan ke resep?",
                    "Konfirmasi Alergi",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return; // Batal tambah obat
                }
            }
    
            // Input takaran dengan validasi
            String takaran = "";
            while (true) {
                takaran = JOptionPane.showInputDialog(this, 
                    "Masukkan takaran (contoh: 2x1 sehari atau 3x1):");
                if (takaran == null) return; // batal
                takaran = takaran.trim();
                if (takaran.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Takaran tidak boleh kosong!");
                    continue;
                }
                // Validasi format: angka x angka (opsional spasi dan 'sehari')
                if (takaran.matches("^\\d+x\\d+(\\s+sehari)?$")) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Format takaran salah! Gunakan format seperti: 2x1 sehari atau 3x1");
                }
            }

            String jumlahStr = JOptionPane.showInputDialog(this, "Jumlah yang diberikan:");
            if (jumlahStr == null) return;
            int jumlah;
            try {
                jumlah = Integer.parseInt(jumlahStr.trim());
                if (jumlah <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus angka positif.");
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