/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.model.entity.*;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class StrukView extends JDialog {

    private Transaksi transaksi;
    private boolean modeLangsung;
    private Dokter dokter;
    private Pemilik pemilik;
    private DashboardPemilikView parentDashboard;

    // Constructor untuk online 
    public StrukView(JFrame parent, Transaksi transaksi, boolean modeLangsung, Pemilik pemilik, Dokter dokter) {
        super(parent, "Struk Pemesanan", true);
        this.transaksi = transaksi;
        this.modeLangsung = modeLangsung;
        this.pemilik = pemilik;
        this.dokter = dokter;
        initComponents();
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // Constructor untuk offline
    public StrukView(JFrame parent, Transaksi transaksi) {
        super(parent, "Struk Pemesanan", true);
        this.transaksi = transaksi;
        this.modeLangsung = false;
        this.pemilik = null;
        this.dokter = null;
        initComponents();
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // Constructor untuk offline dengan parent dashboard
    public StrukView(JFrame parent, Transaksi transaksi, DashboardPemilikView parentDashboard) {
        super(parent, "Struk Pemesanan", true);
        this.transaksi = transaksi;
        this.modeLangsung = false;
        this.parentDashboard = parentDashboard;
        this.pemilik = null;
        this.dokter = null;
        initComponents();
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    // Constructor untuk pembelian obat
    public StrukView(JFrame parent, PembelianObat pb, String namaObat, String metodePembayaran) {
        super(parent, "Struk Pembelian Obat", true);
        initComponentsForObat(pb, namaObat, metodePembayaran);
        setSize(450, 450);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel("🐾 AB Paw Klinik Hewan 🐾");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel("STRUK PEMESANAN");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(15));

        if (transaksi instanceof PemesananOnline) {
            PemesananOnline po = (PemesananOnline) transaksi;
            addRow(mainPanel, "Kode Pemesanan:", po.getKodePemesanan());
            addRow(mainPanel, "Tanggal Konsultasi:", po.getTanggalKonsultasi().toString());
            addRow(mainPanel, "Waktu Konsultasi:", po.getWaktuKonsultasi());
            addRow(mainPanel, "Biaya Konsultasi:", "Rp " + po.getBiayaKonsultasi());
            addRow(mainPanel, "Diskon:", "Rp " + po.getJumlahDiskon());
            addRow(mainPanel, "Total Bayar:", "Rp " + po.getTotalBiaya());
            addRow(mainPanel, "Status:", po.getStatus());
            addRow(mainPanel, "Waktu Pemesanan:", po.getWaktuPemesanan().toString());

            JPanel reminderWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
            reminderWrapper.setOpaque(false);
            JLabel reminder = new JLabel("<html><center>Silakan chat dokter pada jadwal yang telah ditentukan:<br>"
                    + po.getTanggalKonsultasi() + " " + po.getWaktuKonsultasi() + "</center></html>");
            reminder.setForeground(Color.RED);
            reminder.setFont(new Font("Arial", Font.PLAIN, 12));
            reminderWrapper.add(reminder);
            mainPanel.add(reminderWrapper);

        } else if (transaksi instanceof PemesananOffline) {
            PemesananOffline poff = (PemesananOffline) transaksi;

            String namaPemilik = getNamaPemilik(poff.getIdPemilik());
            String namaDokter = getNamaDokter(poff.getIdDokter());
            String namaPet = getNamaPet(poff.getIdPet());
            String jenisPet = getJenisPet(poff.getIdPet());

            JPanel offlinePanel = new JPanel(new GridLayout(0, 2, 10, 5));
            offlinePanel.setOpaque(false);

            addOfflineRow(offlinePanel, "Pemilik:", namaPemilik);
            addOfflineRow(offlinePanel, "Dokter:", namaDokter);
            addOfflineRow(offlinePanel, "Nama Hewan:", namaPet);
            addOfflineRow(offlinePanel, "Jenis Hewan:", jenisPet);
            addOfflineRow(offlinePanel, "Nomor Antrean:", poff.getNomorAntrean());
            addOfflineRow(offlinePanel, "Tanggal Antrean:", poff.getTanggalAntrean().toString());
            addOfflineRow(offlinePanel, "Waktu Antrean:", poff.getWaktuAntrean().toString());
            if (poff.getEstimasiWaktu() != null) {
                addOfflineRow(offlinePanel, "Estimasi Waktu:", poff.getEstimasiWaktu().toString());
            }
            addOfflineRow(offlinePanel, "Status:", poff.getStatus());

            mainPanel.add(offlinePanel);

            if (poff.getBiayaJasa() != null && poff.getBiayaJasa().compareTo(BigDecimal.ZERO) > 0) {
                addRow(mainPanel, "Biaya Jasa:", "Rp " + poff.getBiayaJasa());
            }
        }

        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separatorBottom);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel footerLabel = new JLabel("Terima kasih telah menggunakan layanan kami.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        JButton btnDashboard = new JButton("Kembali ke Dashboard");
        btnDashboard.setFont(new Font("Arial", Font.PLAIN, 10));
        btnDashboard.setMaximumSize(new Dimension(180, 35));
        btnDashboard.setPreferredSize(new Dimension(180, 35));
        btnDashboard.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnDashboard.addActionListener(e -> {
            dispose();
            if (parentDashboard != null) {
                parentDashboard.showDashboard();
            }
        });
        buttonPanel.add(btnDashboard);
        buttonPanel.add(Box.createVerticalStrut(8));

        if (modeLangsung && dokter != null && pemilik != null) {
            JButton btnChat = new JButton("Chat Dokter");
            btnChat.setBackground(new Color(0, 102, 204));
            btnChat.setForeground(Color.WHITE);
            btnChat.setFont(new Font("Arial", Font.BOLD, 11));
            btnChat.setMaximumSize(new Dimension(130, 28));
            btnChat.setPreferredSize(new Dimension(130, 28));
            btnChat.setAlignmentX(Component.CENTER_ALIGNMENT);
            btnChat.addActionListener(e -> {
                dispose();
                new ChatDetailView(pemilik, dokter);
            });
            buttonPanel.add(btnChat);
        }

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalStrut(10));

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }

    private void addRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(10, 0));
        row.setOpaque(false);
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 12));
        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblValue, BorderLayout.EAST);
        panel.add(row);
        panel.add(Box.createVerticalStrut(5));
    }

    private void addOfflineRow(JPanel panel, String label, String value) {
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Arial", Font.PLAIN, 12));
        panel.add(lblLabel);
        panel.add(lblValue);
    }

    private String getNamaPemilik(int idPemilik) {
        try {
            abpaw.model.dao.PemilikDAO dao = new abpaw.model.dao.PemilikDAO();
            abpaw.model.entity.Pemilik p = dao.getById(idPemilik);
            return p != null ? p.getNamaPemilik() : "ID: " + idPemilik;
        } catch (Exception e) {
            return "ID: " + idPemilik;
        }
    }

    private String getNamaDokter(int idDokter) {
        try {
            abpaw.model.dao.DokterDAO dao = new abpaw.model.dao.DokterDAO();
            abpaw.model.entity.Dokter d = dao.getById(idDokter);
            return d != null ? d.getNamaLengkap() : "ID: " + idDokter;
        } catch (Exception e) {
            return "ID: " + idDokter;
        }
    }

    private String getNamaPet(int idPet) {
        try {
            abpaw.model.dao.PetsDAO dao = new abpaw.model.dao.PetsDAO();
            abpaw.model.entity.Pets p = dao.getById(idPet);
            return p != null ? p.getNamaPet() : "ID: " + idPet;
        } catch (Exception e) {
            return "ID: " + idPet;
        }
    }

    private String getJenisPet(int idPet) {
        try {
            abpaw.model.dao.PetsDAO dao = new abpaw.model.dao.PetsDAO();
            abpaw.model.entity.Pets p = dao.getById(idPet);
            return p != null ? p.getJenisHewan() : "-";
        } catch (Exception e) {
            return "-";
        }
    }

    private void initComponentsForObat(PembelianObat pb, String namaObat, String metodePembayaran) {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        JLabel logoLabel = new JLabel("🐾 AB Paw Klinik Hewan 🐾");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setForeground(new Color(0, 102, 204));
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel("STRUK PEMBELIAN OBAT");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(15));

        addRow(mainPanel, "Kode Transaksi:", pb.getKodeTransaksi());
        addRow(mainPanel, "Nama Obat:", namaObat);
        addRow(mainPanel, "Jumlah:", String.valueOf(pb.getJumlah()));
        addRow(mainPanel, "Harga Satuan:", "Rp " + pb.getHargaSatuan());
        addRow(mainPanel, "Total:", "Rp " + pb.getTotalHarga());
        addRow(mainPanel, "Metode Bayar:", metodePembayaran);
        addRow(mainPanel, "Tanggal:", pb.getTanggal().toString());
        addRow(mainPanel, "Status:", pb.getStatus());

        mainPanel.add(Box.createVerticalStrut(15));

        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separatorBottom);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel footerLabel = new JLabel("Terima kasih telah berbelanja.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        JButton btnOK = new JButton("OK");
        btnOK.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOK.setMaximumSize(new Dimension(100, 28));
        btnOK.setPreferredSize(new Dimension(100, 28));
        btnOK.addActionListener(e -> dispose());
        mainPanel.add(btnOK);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }
}
