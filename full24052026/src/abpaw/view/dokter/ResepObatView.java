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
import javax.swing.border.TitledBorder;
import javax.swing.table.JTableHeader;

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

    private final Color GREEN_COLOR = new Color(0, 128, 0);
    private final Color GREEN_DARK = new Color(0, 100, 0);
    private final Color GREEN_LIGHT = new Color(200, 230, 200);

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
        tabbedPane.setForeground(GREEN_COLOR);

        JPanel buatPanel = new JPanel(new BorderLayout(5, 5));

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnRefreshPemesanan = createStyledButton("Refresh Daftar Pemesanan", GREEN_COLOR);

        btnRefreshPemesanan.addActionListener(e -> loadPemesananSelesai());
        topPanel.add(btnRefreshPemesanan);
        buatPanel.add(topPanel, BorderLayout.NORTH);

        String[] colsPesan = {"No", "Tipe Pemesanan", "Pemilik", "Hewan", "Tanggal", "ID"};
        modelPemesanan = new DefaultTableModel(colsPesan, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablePemesanan = new JTable(modelPemesanan);
        styleTable(tablePemesanan);
        JScrollPane spPesan = new JScrollPane(tablePemesanan);
        btnBuatResep = createStyledButton("Buat Resep untuk Pemesanan Terpilih", GREEN_COLOR);
        btnBuatResep.addActionListener(e -> buatResep());
        buatPanel.add(spPesan, BorderLayout.CENTER);
        buatPanel.add(btnBuatResep, BorderLayout.SOUTH);
        tabbedPane.addTab("Buat Resep", buatPanel);

        JPanel daftarPanel = new JPanel(new BorderLayout(5, 5));
        String[] colsResep = {"No", "Tipe Pemesanan", "Tanggal", "Status", "ID"};
        modelResep = new DefaultTableModel(colsResep, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tableResep = new JTable(modelResep);
        styleTable(tableResep);
        JScrollPane spResep = new JScrollPane(tableResep);
        btnDetailResep = createStyledButton("Detail Resep", GREEN_COLOR);
        btnDetailResep.addActionListener(e -> detailResep());
        daftarPanel.add(spResep, BorderLayout.CENTER);
        daftarPanel.add(btnDetailResep, BorderLayout.SOUTH);
        tabbedPane.addTab("Resep Saya", daftarPanel);

        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();

            if (selectedIndex == 1) {
                loadResepDokter();
            }
        });

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(GREEN_DARK);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
        return btn;
    }

    private void styleTable(JTable table) {
        table.setForeground(Color.BLACK);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);

        table.setSelectionBackground(GREEN_COLOR);
        table.setSelectionForeground(Color.WHITE);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(GREEN_LIGHT);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(GREEN_COLOR);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });

        table.getTableHeader().setBackground(GREEN_COLOR);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
    }

    private void loadPemesananSelesai() {
        modelPemesanan.setRowCount(0);
        int counter = 1;

        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            if ("diproses".equalsIgnoreCase(po.getStatus())) {
                Pemilik p = pemilikController.getPemilikById(po.getIdPemilik());
                String namaPemilik = (p != null) ? p.getNamaPemilik() : "ID: " + po.getIdPemilik();
                Pets pet = petsController.getPetsById(po.getIdPet());
                String namaPet = (pet != null) ? pet.getNamaPet() : "ID: " + po.getIdPet();

                modelPemesanan.addRow(new Object[]{
                    counter++, 
                    "Online",
                    namaPemilik,
                    namaPet,
                    po.getTanggalKonsultasi(),
                    po.getIdTransaksi()
                });
            }
        }


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

        if (modelPemesanan.getRowCount() == 0) {
            modelPemesanan.addRow(new Object[]{"-", "-", "Tidak ada pemesanan", "-", "-", null});
        }

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
                counter++, 
                r.getTipePemesanan(),
                r.getTanggalResep(),
                r.getStatus(),
                r.getIdResep() 
            });
        }

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

        int idPet = -1;
        String namaPet = "";
        if (tipe.equals("Online")) {
            PemesananOnline po = pemesananController.getPemesananOnlineById(idPemesanan);
            if (po != null) {
                idPet = po.getIdPet();
                Pets pet = petsController.getPetsById(idPet);
                if (pet != null) {
                    namaPet = pet.getNamaPet();
                }
            }
        } else {
            PemesananOffline poff = pemesananController.getPemesananOfflineById(idPemesanan);
            if (poff != null) {
                idPet = poff.getIdPet();
                Pets pet = petsController.getPetsById(idPet);
                if (pet != null) {
                    namaPet = pet.getNamaPet();
                }
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

        DetailResepDAO detailDao = new DetailResepDAO();
        List<DetailResep> detailList = detailDao.getByResep(idResep);

        JDialog detailDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(this), "Detail Resep", true);
        detailDialog.setLayout(new BorderLayout());
        detailDialog.setSize(550, 450);
        detailDialog.setLocationRelativeTo(this);

        
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        infoPanel.add(new JLabel("ID Resep:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(String.valueOf(idResep)), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        infoPanel.add(new JLabel("Tipe Pemesanan:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(tipe), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        infoPanel.add(new JLabel("Tanggal Resep:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(tanggal.toString()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        infoPanel.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1;
        infoPanel.add(new JLabel(status), gbc);

        
        String[] cols = {"Nama Obat", "Takaran", "Jumlah"};
        DefaultTableModel modelObat = new DefaultTableModel(cols, 0);
        JTable tableObat = new JTable(modelObat);
        JScrollPane spObat = new JScrollPane(tableObat);
        spObat.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(GREEN_COLOR), "Daftar Obat yang Diresepkan",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12), GREEN_COLOR));

       
        for (DetailResep d : detailList) {
            Obat o = obatController.getObatById(d.getIdObat());
            String namaObat = (o != null) ? o.getNamaObat() : "Obat ID " + d.getIdObat();
            modelObat.addRow(new Object[]{namaObat, d.getTakaran(), d.getJumlah()});
        }

        JPanel bottomPanel = new JPanel();
        JButton btnOK = new JButton("OK");
        btnOK.setBackground(GREEN_COLOR);
        btnOK.setForeground(Color.WHITE);
        btnOK.setFocusPainted(false);
        btnOK.addActionListener(e -> detailDialog.dispose());
        bottomPanel.add(btnOK);

        detailDialog.add(infoPanel, BorderLayout.NORTH);
        detailDialog.add(spObat, BorderLayout.CENTER);
        detailDialog.add(bottomPanel, BorderLayout.SOUTH);
        detailDialog.setVisible(true);
    }

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

            JPanel leftPanel = new JPanel(new BorderLayout(5, 5));

            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.add(new JLabel("Cari Obat:"));
            searchField = new JTextField(20);
            JButton searchBtn = createStyledButton("Cari", GREEN_COLOR);
            searchBtn.addActionListener(e -> filterObat());
            searchField.addActionListener(e -> filterObat());
            searchPanel.add(searchField);
            searchPanel.add(searchBtn);
            leftPanel.add(searchPanel, BorderLayout.NORTH);

            String[] colsObat = {"ID", "Nama Obat", "Bentuk", "Harga"};
            modelObatTersedia = new DefaultTableModel(colsObat, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };
            refreshTabelObat("");

            tableObatTersedia = new JTable(modelObatTersedia);
            tableObatTersedia.getColumnModel().getColumn(0).setMinWidth(0);
            tableObatTersedia.getColumnModel().getColumn(0).setMaxWidth(0);
            tableObatTersedia.getColumnModel().getColumn(0).setWidth(0);
            styleTable(tableObatTersedia);

            JScrollPane spObat = new JScrollPane(tableObatTersedia);
            spObat.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(GREEN_COLOR), "Pilih Obat",
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Arial", Font.BOLD, 12), GREEN_COLOR));
            leftPanel.add(spObat, BorderLayout.CENTER);

            JButton btnTambah = createStyledButton(">> Tambah Obat ke Resep", GREEN_COLOR);
            btnTambah.addActionListener(e -> tambahObat());
            leftPanel.add(btnTambah, BorderLayout.SOUTH);

            String[] colsDetail = {"ID Obat", "Nama Obat", "Takaran", "Jumlah"};
            modelDetail = new DefaultTableModel(colsDetail, 0);
            JTable tableDetail = new JTable(modelDetail);
            tableDetail.getColumnModel().getColumn(0).setMinWidth(0);
            tableDetail.getColumnModel().getColumn(0).setMaxWidth(0);
            tableDetail.getColumnModel().getColumn(0).setWidth(0);
            JScrollPane spDetail = new JScrollPane(tableDetail);
            spDetail.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(GREEN_COLOR), "Detail Resep",
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Arial", Font.BOLD, 12), GREEN_COLOR));

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, spDetail);
            splitPane.setResizeWeight(0.5);
            splitPane.setDividerLocation(450);

            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            JButton btnSimpan = createStyledButton("Simpan Resep", GREEN_COLOR);
            btnSimpan.addActionListener(e -> simpanResep());
            bottomPanel.add(btnSimpan);

            JPanel infoPetPanel = new JPanel(new GridBagLayout());
            infoPetPanel.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(GREEN_COLOR), "Informasi Hewan & Alergi",
                    TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                    new Font("Arial", Font.BOLD, 12), GREEN_COLOR));
            infoPetPanel.setBackground(Color.WHITE);
            GridBagConstraints gbcInfo = new GridBagConstraints();
            gbcInfo.insets = new Insets(5, 5, 5, 5);
            gbcInfo.fill = GridBagConstraints.HORIZONTAL;
            gbcInfo.anchor = GridBagConstraints.WEST;

            gbcInfo.gridx = 0;
            gbcInfo.gridy = 0;
            infoPetPanel.add(new JLabel("Nama Hewan:"), gbcInfo);
            gbcInfo.gridx = 1;
            infoPetPanel.add(new JLabel(namaPet), gbcInfo);

            gbcInfo.gridx = 0;
            gbcInfo.gridy = 1;
            infoPetPanel.add(new JLabel("Alergi yang tercatat:"), gbcInfo);
            gbcInfo.gridx = 1;
            JTextArea txtAlergi = new JTextArea(3, 20);
            txtAlergi.setEditable(false);
            txtAlergi.setLineWrap(true);
            txtAlergi.setWrapStyleWord(true);
            txtAlergi.setBackground(infoPetPanel.getBackground());

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

            if (isObatAlergi(idObat)) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "⚠️ PERINGATAN ⚠️\n\nHewan " + namaPet + " memiliki riwayat alergi terhadap obat:\n" + namaObat + "\n\nTetap tambahkan ke resep?",
                        "Konfirmasi Alergi",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm != JOptionPane.YES_OPTION) {
                    return;
                }
            }

            String takaran = "";
            while (true) {
                takaran = JOptionPane.showInputDialog(this,
                        "Masukkan takaran (contoh: 2x1 sehari atau 3x1):");
                if (takaran == null) {
                    return; 
                }
                takaran = takaran.trim();
                if (takaran.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Takaran tidak boleh kosong!");
                    continue;
                }
                
                if (takaran.matches("^\\d+x\\d+(\\s+sehari)?$")) {
                    break;
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Format takaran salah! Gunakan format seperti: 2x1 sehari atau 3x1");
                }
            }

            String jumlahStr = JOptionPane.showInputDialog(this, "Jumlah yang diberikan:");
            if (jumlahStr == null) {
                return;
            }
            int jumlah;
            try {
                jumlah = Integer.parseInt(jumlahStr.trim());
                if (jumlah <= 0) {
                    throw new NumberFormatException();
                }
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
