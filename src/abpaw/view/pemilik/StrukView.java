/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.model.entity.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
/**
 *
 * @author LOQ
 */
public class StrukView extends JDialog {
    private Transaksi transaksi;
    private boolean modeLangsung; // true untuk online langsung, false untuk online nanti atau offline
    private Dokter dokter;        // hanya diperlukan untuk chat langsung
    private Pemilik pemilik;      // untuk chat langsung
    private DashboardPemilikView parentDashboard;
    
    // Constructor untuk online langsung (butuh dokter dan pemilik)
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
    
    // Constructor untuk offline (tanpa chat)
    public StrukView(JFrame parent, Transaksi transaksi) {
        super(parent, "Struk Pemesanan Offline", true);
        this.transaksi = transaksi;
        this.modeLangsung = false;
        this.pemilik = null;
        this.dokter = null;
        initComponents();
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
     public StrukView(JFrame parent, Transaksi transaksi, DashboardPemilikView parentDashboard) {
        super(parent, "Struk Pemesanan Offline", true);
        this.transaksi = transaksi;
        this.modeLangsung = false;
        this.parentDashboard = parentDashboard;  // ← simpan parent dashboard
        this.pemilik = null;
        this.dokter = null;
        initComponents();
        setSize(450, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

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
            reminderWrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

            JLabel reminder = new JLabel("<html><center>Silakan chat dokter pada jadwal yang telah ditentukan:<br>" +
                    po.getTanggalKonsultasi() + " " + po.getWaktuKonsultasi() + "</center></html>");
            reminder.setForeground(Color.RED);
            reminder.setFont(new Font("Arial", Font.PLAIN, 12));

            reminderWrapper.add(reminder);
            mainPanel.add(reminderWrapper);
            
        } else if (transaksi instanceof PemesananOffline) {
            PemesananOffline poff = (PemesananOffline) transaksi;
            addRow(mainPanel, "Nomor Antrean:", poff.getNomorAntrean());
            addRow(mainPanel, "Tanggal Antrean:", poff.getTanggalAntrean().toString());
            addRow(mainPanel, "Waktu Antrean:", poff.getWaktuAntrean().toString());
            if (poff.getEstimasiWaktu() != null)
                addRow(mainPanel, "Estimasi Waktu:", poff.getEstimasiWaktu().toString());
            addRow(mainPanel, "Status:", poff.getStatus());
        }

        mainPanel.add(Box.createVerticalStrut(20));

        // Tombol berdasarkan mode
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnDashboard = new JButton("Kembali ke Dashboard");
        btnDashboard.addActionListener(e -> {
            dispose();  // tutup struk
            if (parentDashboard != null) {
                parentDashboard.showDashboard();  // balik ke dashboard
            }
        });
        buttonPanel.add(btnDashboard);

        if (modeLangsung && dokter != null && pemilik != null) {
            JButton btnChat = new JButton("Chat Dokter");
            btnChat.setBackground(new Color(0, 102, 204));
            btnChat.setForeground(Color.black);
            btnChat.addActionListener(e -> {
                dispose();
                new ChatDetailView(pemilik, dokter);
            });
            buttonPanel.add(btnChat);
        }

        mainPanel.add(buttonPanel);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }
    
    private void addRow(JPanel panel, String label, String value) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setOpaque(false);
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Arial", Font.BOLD, 12));
        JLabel lblValue = new JLabel(value);
        row.add(lblLabel, BorderLayout.WEST);
        row.add(lblValue, BorderLayout.EAST);
        panel.add(row);
        panel.add(Box.createVerticalStrut(8));
    }
    
    public StrukView(JFrame parent, PembelianObat pb, String namaObat, String metodePembayaran) {
        super(parent, "Struk Pembelian Obat", true);
        initComponentsForObat(pb, namaObat, metodePembayaran);
        setSize(450, 450);
        setLocationRelativeTo(parent);
        setVisible(true);
    }

    private void initComponentsForObat(PembelianObat pb, String namaObat, String metodePembayaran) {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

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

        mainPanel.add(Box.createVerticalStrut(20));
        JButton btnOK = new JButton("OK");
        btnOK.addActionListener(e -> dispose());
        btnOK.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(btnOK);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }
}