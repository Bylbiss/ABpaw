/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.DokterController;
import abpaw.controller.KuponController;
import abpaw.controller.PemesananController;
import abpaw.controller.PetsController;
import abpaw.model.entity.*;
import abpaw.utils.AntrianGenerator;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class PemesananOfflineView extends JPanel {
    private static final int KUOTA_PER_JAM = 2;
    private Pemilik pemilik;
    private DashboardPemilikView parentDashboard; 
    private DokterController dokterController;
    private PetsController petsController;
    private PemesananController pemesananController;
    private KuponController kuponController;
    
    // Komponen seperti ChatDokterView
    private JPanel spesialisasiPanel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JPanel dokterListPanel;
    private String selectedSpesialisasi = "Semua";
    
    // Form booking
    private CardLayout cardLayout;
    private JPanel mainContainer;
    private JPanel formPanel;
    private JComboBox<Pets> cbPet;
    private JTextArea taKeluhan;
    private JComboBox<String> cbJam;
    private JButton btnBooking;
    private Dokter dokterTerpilih;
    
    // Komponen kupon
    private JComboBox<Kupon> cbKupon;
    private JLabel lblDiskon, lblTotal;
    private BigDecimal biayaAwal = BigDecimal.ZERO;
    private BigDecimal diskon = BigDecimal.ZERO;
    private BigDecimal total = BigDecimal.ZERO;
    
    public PemesananOfflineView(Pemilik pemilik, DashboardPemilikView parentDashboard) {
        this.pemilik = pemilik;
        this.dokterController = new DokterController();
        this.parentDashboard = parentDashboard; 
        this.petsController = new PetsController();
        this.pemesananController = new PemesananController();
        this.kuponController = new KuponController();
        
        initComponents();
        loadSpesialisasi();
        loadDokter();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        mainContainer = new JPanel();
        cardLayout = new CardLayout();
        mainContainer.setLayout(cardLayout);
        
        // Panel 1: Pilih Dokter (sama seperti ChatDokterView)
        mainContainer.add(createPilihDokterPanel(), "pilihDokter");
        
        // Panel 2: Form Booking
        formPanel = createFormPanel();
        mainContainer.add(formPanel, "form");
        
        add(mainContainer, BorderLayout.CENTER);
        cardLayout.show(mainContainer, "pilihDokter");
    }
    
    private JPanel createPilihDokterPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.WHITE);
        
        // Header
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBackground(Color.WHITE);
        JLabel titleLabel = new JLabel("Pilih Dokter untuk Antrean Offline");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Spesialisasi
        spesialisasiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        spesialisasiPanel.setBackground(Color.WHITE);
        JScrollPane spesialisasiScroll = new JScrollPane(spesialisasiPanel);
        spesialisasiScroll.setBorder(null);
        spesialisasiScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spesialisasiScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        topPanel.add(spesialisasiScroll, BorderLayout.CENTER);
        
        // Pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        JButton searchBtn = new JButton("Cari");
        filterCombo = new JComboBox<>(new String[]{"Harga Termurah", "Harga Termahal", "Sesuai Hewan Saya"});
        filterCombo.addActionListener(e -> loadDokter());
        searchBtn.addActionListener(e -> loadDokter());
        searchPanel.add(new JLabel("Cari:"));
        searchPanel.add(searchField);
        searchPanel.add(searchBtn);
        searchPanel.add(new JLabel("Filter:"));
        searchPanel.add(filterCombo);
        topPanel.add(searchPanel, BorderLayout.SOUTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        // Daftar dokter
        dokterListPanel = new JPanel();
        dokterListPanel.setLayout(new BoxLayout(dokterListPanel, BoxLayout.Y_AXIS));
        dokterListPanel.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(dokterListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Dokter"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        panel.setBackground(Color.WHITE);
        
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        JLabel dokterLabel = new JLabel();
        dokterLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridwidth = 2;
        form.add(dokterLabel, gbc);
        
        gbc.gridy++;
        gbc.gridwidth = 1;
        form.add(new JLabel("Pilih Hewan:"), gbc);
        cbPet = new JComboBox<>();
        gbc.gridx = 1;
        form.add(cbPet, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        form.add(new JLabel("Keluhan:"), gbc);
        taKeluhan = new JTextArea(3, 20);
        taKeluhan.setLineWrap(true);
        taKeluhan.setWrapStyleWord(true);
        JScrollPane scrollKeluhan = new JScrollPane(taKeluhan);
        gbc.gridx = 1;
        form.add(scrollKeluhan, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        form.add(new JLabel("Pilih Waktu:"), gbc);
        cbJam = new JComboBox<>();
        gbc.gridx = 1;
        form.add(cbJam, gbc);
        
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton btnBack = new JButton("Kembali");
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "pilihDokter"));
        
        btnBooking = new JButton("Booking Offline");
        btnBooking.setBackground(new Color(0, 102, 204));
        btnBooking.setForeground(Color.BLUE);
        btnBooking.setFont(new Font("Arial", Font.BOLD, 12));
        btnBooking.addActionListener(e -> prosesBooking());
        
        buttonPanel.add(btnBack);
        buttonPanel.add(btnBooking);
        form.add(buttonPanel, gbc);
        
        panel.add(form, BorderLayout.CENTER);
        form.putClientProperty("dokterLabel", dokterLabel);
        
        return panel;
    }
    
    private void loadSpesialisasi() {
        spesialisasiPanel.removeAll();
        String[] list = {"Semua", "Dokter Umum", "Spesialis Penyakit Dalam", "Spesialis Gigi",
                "Spesialis Kulit", "Spesialis Reproduksi", "Spesialis Parasitologi & Infeksi", "Spesialis Bedah"};
        for (String spes : list) {
            JButton btn = new JButton(spes);
            btn.setBackground(selectedSpesialisasi.equals(spes) ? new Color(0, 102, 204) : Color.LIGHT_GRAY);
            btn.setForeground(selectedSpesialisasi.equals(spes) ? new Color(0, 102, 204) : Color.BLACK);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                selectedSpesialisasi = spes;
                loadSpesialisasi();
                loadDokter();
            });
            spesialisasiPanel.add(btn);
        }
        spesialisasiPanel.revalidate();
        spesialisasiPanel.repaint();
    }
    
    private void loadDokter() {
        dokterListPanel.removeAll();
        String keyword = searchField.getText().trim();
        String filter = (String) filterCombo.getSelectedItem();
        
        List<Dokter> dokterList;
        if (!selectedSpesialisasi.equals("Semua")) {
            dokterList = dokterController.getDokterBySpesialisasi(selectedSpesialisasi);
        } else {
            dokterList = dokterController.getAllDokter();
        }
        
        if (!keyword.isEmpty()) {
            dokterList.removeIf(d -> !d.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()));
        }
        
        if (filter.equals("Harga Termurah")) {
            dokterList.sort((a,b) -> a.getBiayaKonsultasi().compareTo(b.getBiayaKonsultasi()));
        } else if (filter.equals("Harga Termahal")) {
            dokterList.sort((a,b) -> b.getBiayaKonsultasi().compareTo(a.getBiayaKonsultasi()));
        } else if (filter.equals("Sesuai Hewan Saya")) {
            List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            List<String> jenisHewanList = petsList.stream()
                .map(Pets::getJenisHewan)
                .distinct()
                .collect(Collectors.toList());
            if (jenisHewanList.isEmpty()) {
                dokterList = new ArrayList<>(); // tidak ada hewan, kosongkan
            } else {
                dokterList = dokterController.getDokterBySpesiesHewanList(jenisHewanList);
            }
        }
        
        if (dokterList.isEmpty()) {
            dokterListPanel.add(new JLabel("Tidak ada dokter."));
        } else {
            for (Dokter d : dokterList) {
                dokterListPanel.add(createDokterCard(d));
                dokterListPanel.add(Box.createVerticalStrut(10));
            }
        }
        dokterListPanel.revalidate();
        dokterListPanel.repaint();
    }
    
    private JPanel createDokterCard(Dokter dokter) {
        RoundedPanel card = new RoundedPanel(15);
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        card.setLayout(new BorderLayout(10, 5));
        card.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Foto
        JPanel fotoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 102, 204));
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
            }
        };
        fotoPanel.setPreferredSize(new Dimension(50, 50));
        fotoPanel.setLayout(new BorderLayout());
        JLabel iconLabel = new JLabel("👤", SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 28));
        iconLabel.setForeground(Color.WHITE);
        fotoPanel.add(iconLabel, BorderLayout.CENTER);
        card.add(fotoPanel, BorderLayout.WEST);
        
        // Info
        JLabel nameLabel = new JLabel(dokter.getNamaLengkap());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(0, 51, 102));
        JLabel spesLabel = new JLabel("Spesialisasi: " + dokter.getSpesialisasi());
        spesLabel.setForeground(Color.DARK_GRAY);
        JLabel priceLabel = new JLabel("Biaya: Rp " + dokter.getBiayaKonsultasi());
        priceLabel.setForeground(new Color(0, 153, 0));
        priceLabel.setFont(new Font("Arial", Font.BOLD, 12));
        
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setOpaque(false);
        middlePanel.add(nameLabel);
        middlePanel.add(Box.createVerticalStrut(5));
        middlePanel.add(spesLabel);
        middlePanel.add(Box.createVerticalStrut(3));
        middlePanel.add(priceLabel);
        
        String spesiesHewan = dokter.getSpesiesHewan();
        if (spesiesHewan == null || spesiesHewan.trim().isEmpty()) {
            spesiesHewan = "-";
        }
        JLabel hewanLabel = new JLabel("Menangani: " + spesiesHewan);
        hewanLabel.setForeground(new Color(100, 100, 100));
        hewanLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        middlePanel.add(Box.createVerticalStrut(3));
        middlePanel.add(hewanLabel);
        
        card.add(middlePanel, BorderLayout.CENTER);
        
        // Tombol Pilih (langsung ke form, tanpa dialog)
        JButton pilihBtn = new JButton("Pilih");
        pilihBtn.setBackground(new Color(0, 102, 204));
        pilihBtn.setForeground(Color.BLUE);
        pilihBtn.setFocusPainted(false);
        pilihBtn.addActionListener(e -> {
            List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            if (petsList.isEmpty()) {
                JOptionPane.showMessageDialog(PemesananOfflineView.this,
                    "Anda belum memiliki hewan peliharaan.\nSilakan tambahkan hewan terlebih dahulu di menu Profil.",
                    "Tidak Ada Hewan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            String spesiesDokterStr = dokter.getSpesiesHewan();
            if (spesiesDokterStr == null || spesiesDokterStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(PemesananOfflineView.this,
                    "Data dokter tidak lengkap (spesies hewan tidak terdaftar).",
                    "Informasi Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
                return;
            }

            List<String> spesiesDokter = Arrays.stream(spesiesDokterStr.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            boolean adaYangSesuai = petsList.stream()
                    .anyMatch(pet -> spesiesDokter.contains(pet.getJenisHewan().toLowerCase()));

            if (!adaYangSesuai) {
                String pesan = "Dokter Tidak Sesuai\n\n" +
                               "Maaf, dokter ini khusus menangani: " + spesiesDokterStr + "\n\n" +
                               "Silakan pilih dokter lain yang sesuai dengan hewan peliharaan Anda.";
                JOptionPane.showMessageDialog(PemesananOfflineView.this, pesan,
                    "Dokter Tidak Sesuai", JOptionPane.WARNING_MESSAGE);
                return;
            }
            // ========== AKHIR VALIDASI ==========

           dokterTerpilih = dokter;

            // Load hewan
            List<Pets> petsList2 = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            cbPet.removeAllItems();
            for (Pets pet : petsList2) {
                cbPet.addItem(pet);
            }
            if (petsList2.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Anda belum punya hewan.");
                btnBooking.setEnabled(false);
            } else {
                btnBooking.setEnabled(true);
            }
            refreshJamOptions();

            JLabel dokterLabel = (JLabel) ((JPanel) formPanel.getComponent(0)).getClientProperty("dokterLabel");
            if (dokterLabel != null) {
                dokterLabel.setText("Dokter: " + dokter.getNamaLengkap());
            }
            cardLayout.show(mainContainer, "form");
            });
            card.add(pilihBtn, BorderLayout.EAST);
            return card;
    }
    
    private void prosesBooking() {
        if (cbPet.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Pilih hewan.");
            return;
        }
        
         if (dokterTerpilih == null) {
        JOptionPane.showMessageDialog(this, "Pilih dokter terlebih dahulu.");
        return;
        }
         
        String waktuStr = (String) cbJam.getSelectedItem();
        if (waktuStr == null || waktuStr.equals("Tidak ada jam tersedia hari ini")) {
            JOptionPane.showMessageDialog(this, "Tidak ada jadwal yang tersedia.");
            return;
        }
    
        // Ambil jam dari string (karena format "HH:mm (sisa X kursi)")
        String jamOnly = waktuStr.split(" ")[0];
        LocalTime waktuAntrean = LocalTime.parse(jamOnly, DateTimeFormatter.ofPattern("HH:mm"));
    
        LocalTime now = LocalTime.now();
        if (waktuAntrean.isBefore(now.plusHours(1))) {
            JOptionPane.showMessageDialog(this, "Waktu booking harus minimal 1 jam dari sekarang.");
            return;
        }
    
    // CEK KUOTA
        LocalDate today = LocalDate.now();
        int sudahBooking = pemesananController.getJumlahBookingByJam(today, waktuAntrean,dokterTerpilih.getIdDokter());
    
        if (sudahBooking >= KUOTA_PER_JAM) {
            JOptionPane.showMessageDialog(this, 
                "Maaf, jam " + jamOnly + " sudah penuh!\nSilakan pilih jam lain.", 
                "Jam Penuh", 
                JOptionPane.WARNING_MESSAGE);
            refreshJamOptions();
            return;
        }
    
        // Lanjut booking
        Pets pet = (Pets) cbPet.getSelectedItem();
        String keluhan = taKeluhan.getText().trim();
        String nomorAntrean = AntrianGenerator.generate();

        PemesananOffline antrean = new PemesananOffline();
        antrean.setIdDokter(dokterTerpilih.getIdDokter());
        antrean.setIdPemilik(pemilik.getIdPemilik());
        antrean.setIdPet(pet.getIdPet());
        antrean.setNomorAntrean(nomorAntrean);
        antrean.setTanggalAntrean(today);
        antrean.setWaktuAntrean(waktuAntrean);
        antrean.setKeluhan(keluhan);
        antrean.setStatus("menunggu");
        antrean.setEstimasiWaktu(null);
    
            boolean success = pemesananController.createPemesananOffline(antrean);
        if (success) {
            JOptionPane.showMessageDialog(this, "Booking berhasil!\nNomor Antrean: " + nomorAntrean);
            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
            new StrukView(parent, antrean, parentDashboard);
        } else {
            JOptionPane.showMessageDialog(this, "Booking gagal.");
        }
    }
    
    private void refreshJamOptions() {
        cbJam.removeAllItems();
        LocalTime now = LocalTime.now();
        int currentHour = now.getHour();
        int currentMinute = now.getMinute();
        LocalDate today = LocalDate.now();

        // Minimal jam booking: jam sekarang + 1 (beri jeda 1 jam untuk persiapan)
        int minHour = currentHour + 1;
        // Jika sekarang sudah lewat jam 16:00, maka tidak ada jam tersedia
        if (minHour > 22) {
            cbJam.addItem("Tidak ada jam tersedia hari ini");
            cbJam.setEnabled(false);
            return;
        }

        // Tentukan apakah jam minimal perlu ditambah karena menit?
        // Misal jika sekarang 13:45, maka jam 14:00 masih bisa? Kita beri toleransi: jika menit > 30, maka minimal jam +2
        if (currentMinute > 30) {
            minHour++;
        }

        for (int i = minHour; i <= 22; i++) {
            LocalTime jamSlot = LocalTime.of(i, 0);
            int sudahBooking = pemesananController.getJumlahBookingByJam(today, jamSlot, dokterTerpilih.getIdDokter());
        
            if (sudahBooking < KUOTA_PER_JAM) {
                int sisa = KUOTA_PER_JAM - sudahBooking;
                   cbJam.addItem(String.format("%02d:00 (sisa %d kursi untuk Dr. %s)", i, sisa, dokterTerpilih.getNamaLengkap()));
            } else {
                // Jam penuh, tidak ditampilkan di combo box
                System.out.println("Jam " + i + ":00 untuk Dr. " + dokterTerpilih.getNamaLengkap() + " sudah penuh");
            }
        }

        if (cbJam.getItemCount() == 0) {
            cbJam.addItem("Tidak ada jam Tersedia untuk dokter ini");
            cbJam.setEnabled(false);
        } else {
            cbJam.setEnabled(true);
        }
    }
}