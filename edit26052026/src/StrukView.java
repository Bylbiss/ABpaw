/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.model.entity.*;
import abpaw.utils.IconHelper;
import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class StrukView extends JDialog {
    private Transaksi transaksi;
    private boolean modeLangsung;
    private Dokter dokter;
    private Pemilik pemilik;
    private DashboardPemilikView parentDashboard;

    // Constructor untuk online langsung
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
        super(parent, "Struk Pemesanan", true);
        this.transaksi = transaksi;
        this.modeLangsung = false;
        this.pemilik = null;
        this.dokter = null;
        if (transaksi instanceof PemesananOffline) {
                initComponentsForOffline((PemesananOffline) transaksi);
            } else {
                initComponents();
            }

            setSize(500, 650);
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
        if (transaksi instanceof PemesananOffline) {
              initComponentsForOffline((PemesananOffline) transaksi);
          } else {
              initComponents();
          }

          setSize(500, 650);
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

        // Logo
        ImageIcon pawIcon = IconHelper.getScaledPawIcon(35, 35);
        JLabel logoLabel = new JLabel(" AB Paw Klinik Hewan", pawIcon, SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setForeground(new Color(232, 62, 140));
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Garis pemisah
        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(10));

        // Judul
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
            JLabel reminder = new JLabel("<html><center>Silakan chat dokter pada jadwal yang telah ditentukan:<br>" +
                    po.getTanggalKonsultasi() + " " + po.getWaktuKonsultasi() + "</center></html>");
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
       
        // Garis pemisah bawah
        JSeparator separatorBottom = new JSeparator();
        separatorBottom.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separatorBottom);
        mainPanel.add(Box.createVerticalStrut(10));

        // Footer
        JLabel footerLabel = new JLabel("Terima kasih telah menggunakan layanan kami.");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        footerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        footerLabel.setForeground(Color.GRAY);
        mainPanel.add(footerLabel);
        mainPanel.add(Box.createVerticalStrut(15));

        // Tombol
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
            btnChat.setBackground(new Color(232, 62, 140));
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

        ImageIcon pawIcon = IconHelper.getScaledPawIcon(35, 35);
        JLabel logoLabel = new JLabel(" AB Paw Klinik Hewan", pawIcon, SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setForeground(new Color(232, 62, 140));
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
    
    // Method khusus untuk menampilkan struk offline dengan rincian biaya tambahan
    private void initComponentsForOffline(PemesananOffline poff) {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // Logo
        ImageIcon pawIcon = IconHelper.getScaledPawIcon(35, 35);
        JLabel logoLabel = new JLabel(" AB Paw Klinik Hewan", pawIcon, SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 14));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoLabel.setForeground(new Color(232, 62, 140));
        mainPanel.add(logoLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        JSeparator separator = new JSeparator();
        separator.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(separator);
        mainPanel.add(Box.createVerticalStrut(10));

        JLabel title = new JLabel("STRUK PEMESANAN OFFLINE");
        title.setFont(new Font("Arial", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(title);
        mainPanel.add(Box.createVerticalStrut(15));

        // Info pemilik dan dokter
        String namaPemilik = getNamaPemilik(poff.getIdPemilik());
        String namaDokter = getNamaDokter(poff.getIdDokter());
        String namaPet = getNamaPet(poff.getIdPet());
        String jenisPet = getJenisPet(poff.getIdPet());

        addRow(mainPanel, "Pemilik:", namaPemilik);
        addRow(mainPanel, "Dokter:", namaDokter);
        addRow(mainPanel, "Nama Hewan:", namaPet);
        addRow(mainPanel, "Jenis Hewan:", jenisPet);
        addRow(mainPanel, "Nomor Antrean:", poff.getNomorAntrean());
        addRow(mainPanel, "Tanggal Antrean:", poff.getTanggalAntrean().toString());
        addRow(mainPanel, "Waktu Antrean:", poff.getWaktuAntrean().toString());

        mainPanel.add(Box.createVerticalStrut(10));

        // Rincian Biaya
        JLabel rincianLabel = new JLabel("RINCIAN BIAYA:");
        rincianLabel.setFont(new Font("Arial", Font.BOLD, 14));
        rincianLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        mainPanel.add(rincianLabel);
        mainPanel.add(Box.createVerticalStrut(5));

        // Biaya Jasa Dasar
        JPanel rowJasa = new JPanel(new BorderLayout(10, 0));
        rowJasa.setOpaque(false);
        rowJasa.add(new JLabel("Biaya Jasa Konsultasi:"), BorderLayout.WEST);
        rowJasa.add(new JLabel("Rp " + (poff.getBiayaJasa() != null ? poff.getBiayaJasa().toString() : "0")), BorderLayout.EAST);
        mainPanel.add(rowJasa);

        // Biaya Tambahan (jika ada)
        abpaw.controller.PemesananController pc = new abpaw.controller.PemesananController();
        java.util.List<abpaw.model.entity.DetailBiayaOffline> daftarBiaya = pc.getBiayaTambahan(poff.getIdTransaksi());

        if (!daftarBiaya.isEmpty()) {
            mainPanel.add(Box.createVerticalStrut(5));
            JLabel tambahanLabel = new JLabel("Biaya Penanganan Tambahan:");
            tambahanLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            tambahanLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            mainPanel.add(tambahanLabel);

            for (abpaw.model.entity.DetailBiayaOffline d : daftarBiaya) {
                JPanel rowTambahan = new JPanel(new BorderLayout(10, 0));
                rowTambahan.setOpaque(false);
                String deskripsi = "   - " + d.getNamaBiaya() + " (x" + d.getJumlah() + ")";
                rowTambahan.add(new JLabel(deskripsi), BorderLayout.WEST);
                rowTambahan.add(new JLabel("Rp " + d.getSubtotal().toString()), BorderLayout.EAST);
                mainPanel.add(rowTambahan);
            }
        }

        mainPanel.add(Box.createVerticalStrut(10));

        // Garis pemisah
        JSeparator lineSep = new JSeparator();
        lineSep.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(lineSep);
        mainPanel.add(Box.createVerticalStrut(5));

        // Total
        JPanel rowTotal = new JPanel(new BorderLayout(10, 0));
        rowTotal.setOpaque(false);
        JLabel totalLabel = new JLabel("TOTAL BIAYA:");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        totalLabel.setForeground(new Color(0, 150, 0));
        rowTotal.add(totalLabel, BorderLayout.WEST);
        JLabel totalValue = new JLabel("Rp " + (poff.getTotalBiaya() != null ? poff.getTotalBiaya().toString() : "0"));
        totalValue.setFont(new Font("Arial", Font.BOLD, 14));
        totalValue.setForeground(new Color(0, 150, 0));
        rowTotal.add(totalValue, BorderLayout.EAST);
        mainPanel.add(rowTotal);

        mainPanel.add(Box.createVerticalStrut(10));

        // Status
        addRow(mainPanel, "Status:", poff.getStatus());
        addRow(mainPanel, "Status Pembayaran:", poff.getStatusPembayaran() != null ? poff.getStatusPembayaran() : "belum_bayar");

        mainPanel.add(Box.createVerticalStrut(15));

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

        JButton btnOK = new JButton("OK");
        btnOK.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnOK.addActionListener(e -> dispose());
        mainPanel.add(btnOK);

        JScrollPane scroll = new JScrollPane(mainPanel);
        scroll.setBorder(null);
        add(scroll, BorderLayout.CENTER);
    }
}