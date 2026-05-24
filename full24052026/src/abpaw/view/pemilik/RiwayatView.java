/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.DokterController;
import abpaw.controller.ObatController;
import abpaw.controller.PembelianObatController;
import abpaw.controller.PemesananController;
import abpaw.controller.PetsController;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Obat;
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
import java.math.BigDecimal;

public class RiwayatView extends JPanel {

    private Pemilik pemilik;
    private PemesananController pemesananController;
    private DokterController dokterController;
    private PetsController petsController;
    private JTable tableRiwayat;
    private DefaultTableModel model;
    private JComboBox<String> filterTipe;

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
        topPanel.add(new JLabel("Filter Tipe:"));
        filterTipe = new JComboBox<>(new String[]{"Semua", "Online", "Offline", "Pembelian Obat"});
        filterTipe.addActionListener(e -> loadRiwayat());
        topPanel.add(filterTipe);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Tipe", "Kode", "Dokter", "Hewan", "Tanggal", "Total", "Status", "idDokter"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        tableRiwayat = new JTable(model);

        tableRiwayat.getColumnModel().getColumn(0).setMinWidth(0);
        tableRiwayat.getColumnModel().getColumn(0).setMaxWidth(0);
        tableRiwayat.getColumnModel().getColumn(0).setWidth(0);

        tableRiwayat.getColumnModel().getColumn(8).setMinWidth(0);
        tableRiwayat.getColumnModel().getColumn(8).setMaxWidth(0);
        tableRiwayat.getColumnModel().getColumn(8).setWidth(0);

        styleTable(tableRiwayat);

        JScrollPane sp = new JScrollPane(tableRiwayat);
        add(sp, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnDetail = new JButton("Detail / Struk");
        JButton btnChat = new JButton("Chat Dokter");

        Color biru = new Color(0, 102, 204);
        btnDetail.setBackground(biru);
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setFocusPainted(false);
        btnDetail.setFont(new Font("Arial", Font.BOLD, 12));

        btnChat.setBackground(biru);
        btnChat.setForeground(Color.WHITE);
        btnChat.setFocusPainted(false);
        btnChat.setFont(new Font("Arial", Font.BOLD, 12));

        btnDetail.addActionListener(e -> showDetail());
        btnChat.addActionListener(e -> chatFromRiwayat());

        bottomPanel.add(btnDetail);
        bottomPanel.add(btnChat);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleTable(JTable table) {
        Color biruTua = new Color(0, 102, 204);
        Color biruMuda = new Color(173, 216, 230);

        table.getTableHeader().setBackground(biruTua);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(biruTua);
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Arial", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(biruMuda);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(biruTua);
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }

    private void loadRiwayat() {
        model.setRowCount(0);
        String selectedTipe = (String) filterTipe.getSelectedItem();

        if (selectedTipe.equals("Semua") || selectedTipe.equals("Online")) {
            List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByPemilik(pemilik.getIdPemilik());
            for (PemesananOnline po : onlineList) {
                String namaDokter = "Dokter ID " + po.getIdDokter();
                String namaHewan = "Pet ID " + po.getIdPet();
                try {
                    Dokter d = dokterController.getDokterById(po.getIdDokter());
                    if (d != null) {
                        namaDokter = d.getNamaLengkap();
                    }
                    Pets p = petsController.getPetsById(po.getIdPet());
                    if (p != null) {
                        namaHewan = p.getNamaPet();
                    }
                } catch (Exception ex) {
                }
                model.addRow(new Object[]{
                    po.getIdTransaksi(), "Online", po.getKodePemesanan(),
                    namaDokter, namaHewan,
                    po.getTanggalKonsultasi(), po.getTotalBiaya(), po.getStatus(),
                    po.getIdDokter()
                });
            }
        }

        if (selectedTipe.equals("Semua") || selectedTipe.equals("Offline")) {
            List<PemesananOffline> offlineList = pemesananController.getPemesananOfflineByPemilik(pemilik.getIdPemilik());
            for (PemesananOffline poff : offlineList) {
                String namaDokter = "Dokter ID " + poff.getIdDokter();
                String namaHewan = "Pet ID " + poff.getIdPet();
                try {
                    Dokter d = dokterController.getDokterById(poff.getIdDokter());
                    if (d != null) {
                        namaDokter = d.getNamaLengkap();
                    }
                    Pets p = petsController.getPetsById(poff.getIdPet());
                    if (p != null) {
                        namaHewan = p.getNamaPet();
                    }
                } catch (Exception ex) {
                }
                String biayaJasa = (poff.getBiayaJasa() != null && poff.getBiayaJasa().compareTo(BigDecimal.ZERO) > 0)
                        ? "Rp " + poff.getBiayaJasa()
                        : "-";
                model.addRow(new Object[]{
                    poff.getIdTransaksi(), "Offline", poff.getNomorAntrean(),
                    namaDokter, namaHewan,
                    poff.getTanggalAntrean(), biayaJasa, poff.getStatus(),
                    poff.getIdDokter()
                });
            }
        }

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
        if (row == -1) {
            return;
        }
        String tipe = (String) model.getValueAt(row, 1);
        int id = (int) model.getValueAt(row, 0);
        if (tipe.equals("Online")) {
            PemesananOnline po = pemesananController.getPemesananOnlineById(id);
            if (po != null) {
                new StrukView((JFrame) SwingUtilities.getWindowAncestor(this), po);
            }
        } else if (tipe.equals("Offline")) {
            PemesananOffline poff = pemesananController.getPemesananOfflineById(id);
            if (poff != null) {
                new StrukView((JFrame) SwingUtilities.getWindowAncestor(this), poff);
            }
        } else if (tipe.equals("Pembelian Obat")) {
            PembelianObatController pbController = new PembelianObatController();
            PembelianObat pb = pbController.getPembelianById(id);
            if (pb != null) {
                ObatController obatController = new ObatController();
                Obat o = obatController.getObatById(pb.getIdObat());
                String namaObat = (o != null) ? o.getNamaObat() : "Obat tidak diketahui";
                new StrukView((JFrame) SwingUtilities.getWindowAncestor(this), pb, namaObat, "-");
            } else {
                JOptionPane.showMessageDialog(this, "Data pembelian obat tidak ditemukan.");
            }
        }
    }

    private void chatFromRiwayat() {
        int row = tableRiwayat.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih riwayat terlebih dahulu.");
            return;
        }

        String tipe = (String) model.getValueAt(row, 1);

        // Hanya pemesanan online yang bisa chat
        if (!"Online".equalsIgnoreCase(tipe)) {
            JOptionPane.showMessageDialog(this,
                    "Chat dengan dokter hanya tersedia untuk pemesanan ONLINE.\n"
                    + "Untuk tipe " + tipe + ", silakan hubungi klinik melalui telepon atau datang langsung.",
                    "Chat Tidak Tersedia",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int idDokter = (int) model.getValueAt(row, 8);
        String status = (String) model.getValueAt(row, 7);

        // Validasi status 
        if ("menunggu".equalsIgnoreCase(status)) {
            JOptionPane.showMessageDialog(this,
                    "Pembayaran belum selesai. Silakan selesaikan pembayaran terlebih dahulu sebelum chat dengan dokter.",
                    "Chat Belum Tersedia",
                    JOptionPane.WARNING_MESSAGE);
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
