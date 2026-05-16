/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.PemesananController;
import abpaw.controller.KuponController;
import abpaw.controller.PetsController;
import abpaw.model.entity.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
/**
 *
 * @author LOQ
 */
public class PemesananOnlineView extends JDialog {
    private Pemilik pemilik;
    private Dokter dokter;
    private PemesananController pemesananController;
    private PetsController petsController;
    private KuponController kuponController;
    
    private JComboBox<Pets> cbPet;
    private JTextArea taKeluhan;
    private JComboBox<Kupon> cbKupon;
    private JLabel lblDiskon, lblTotal;
    private JComboBox<String> cbPembayaran;
    private JButton btnBayar;
    private JPanel jamPanel;
    private JComboBox<String> cbJam;
    private boolean modeLangsung; 
    private BigDecimal biayaAwal, diskon, total;
    private Kupon kuponTerpakai;
    
    public PemesananOnlineView(JFrame parent, Pemilik pemilik, Dokter dokter, boolean langsung) {
        super(parent, "Pemesanan Konsultasi Online", true);
        this.pemilik = pemilik;
        this.dokter = dokter;
        this.modeLangsung = langsung;
        this.pemesananController = new PemesananController();
        this.petsController = new PetsController();
        this.kuponController = new KuponController();
        
        initComponents();
        loadPets();
        loadJamJikaNanti();
        hitungBiaya();
        setSize(500, 550);
        setLocationRelativeTo(parent);
        setVisible(true);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Label nama dokter
        mainPanel.add(new JLabel("Dokter: " + dokter.getNamaLengkap()), gbc);
        
        gbc.gridy++;
        mainPanel.add(new JLabel("Pilih Hewan:"), gbc);
        cbPet = new JComboBox<>();
        gbc.gridy++;
        mainPanel.add(cbPet, gbc);
        
        gbc.gridy++;
        mainPanel.add(new JLabel("Keluhan (opsional):"), gbc);
        taKeluhan = new JTextArea(3, 20);
        taKeluhan.setLineWrap(true);
        taKeluhan.setWrapStyleWord(true);
        JScrollPane scrollKeluhan = new JScrollPane(taKeluhan);
        gbc.gridy++;
        mainPanel.add(scrollKeluhan, gbc);
        
        // Kupon
        gbc.gridy++;
        mainPanel.add(new JLabel("Pilih Kupon:"), gbc);
        cbKupon = new JComboBox<>();
        cbKupon.addItem(null); // opsi tanpa kupon
        List<Kupon> kuponList = kuponController.getKuponAktif();
        for (Kupon k : kuponList) {
            cbKupon.addItem(k);
        }
        cbKupon.addActionListener(e -> pilihKupon());
        gbc.gridy++;
        mainPanel.add(cbKupon, gbc);
        
        // Diskon & total
        gbc.gridy++;
        mainPanel.add(new JLabel("Diskon:"), gbc);
        lblDiskon = new JLabel("Rp 0");
        gbc.gridy++;
        mainPanel.add(lblDiskon, gbc);
        gbc.gridy++;
        mainPanel.add(new JLabel("Total Biaya:"), gbc);
        lblTotal = new JLabel("");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 14));
        gbc.gridy++;
        mainPanel.add(lblTotal, gbc);
        
        // Jika pesan nanti, tambahkan pilihan jam
        if (!modeLangsung) {
            gbc.gridy++;
            mainPanel.add(new JLabel("Pilih Jam Konsultasi:"), gbc);
            jamPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); // inisialisasi
            cbJam = new JComboBox<>();
            jamPanel.add(cbJam);
            gbc.gridy++;
            mainPanel.add(jamPanel, gbc);
        }
        
        // Metode pembayaran
        gbc.gridy++;
        mainPanel.add(new JLabel("Metode Pembayaran:"), gbc);
        cbPembayaran = new JComboBox<>(new String[]{"Bank Transfer", "E-Wallet", "Kartu Kredit"});
        gbc.gridy++;
        mainPanel.add(cbPembayaran, gbc);
        
        // Tombol bayar
        btnBayar = new JButton(modeLangsung ? "Bayar Sekarang" : "Bayar Sekarang & Konsultasi Nanti");
        btnBayar.setBackground(Color.WHITE);           
        btnBayar.setForeground(Color.BLACK);           
        btnBayar.setFont(new Font("Arial", Font.PLAIN, 12));
        btnBayar.setFocusPainted(false);
        btnBayar.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
        btnBayar.addActionListener(e -> prosesPemesanan());
        gbc.gridy++;
        mainPanel.add(btnBayar, gbc);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void loadPets() {
        List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
        cbPet.removeAllItems();
        for (Pets p : petsList) {
            cbPet.addItem(p);
        }
        if (petsList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Anda belum memiliki hewan. Silakan tambahkan di menu Profil.");
            btnBayar.setEnabled(false);
        }
    }
    
    private void loadJamJikaNanti() {
        if (!modeLangsung && cbJam != null) {
            LocalTime now = LocalTime.now();
            int currentHour = now.getHour();
            for (int i = 6; i <= 22; i++) {
                if (i > currentHour) { // hanya jam yang lebih besar dari jam sekarang
                    cbJam.addItem(String.format("%02d:00", i));
                }
            }
            if (cbJam.getItemCount() == 0) {
                cbJam.addItem("Tidak ada jam tersedia");
                cbJam.setEnabled(false);
            }
        }
    }
    
    private void pilihKupon() {
        Kupon selected = (Kupon) cbKupon.getSelectedItem();
        if (selected != null && selected.isValid() && biayaAwal.compareTo(selected.getMinimalPembelian()) >= 0) {
            kuponTerpakai = selected;
        } else {
            kuponTerpakai = null;
            if (selected != null && biayaAwal.compareTo(selected.getMinimalPembelian()) < 0) {
                JOptionPane.showMessageDialog(this,
                    "Minimal pembelian Rp " + selected.getMinimalPembelian() + " untuk kupon ini.");
                cbKupon.setSelectedItem(null);
            }
        }
        hitungBiaya();
    }
    
    private void hitungBiaya() {
        biayaAwal = dokter.getBiayaKonsultasi();
        diskon = (kuponTerpakai != null && kuponTerpakai.isValid()) ? 
                 kuponTerpakai.hitungPotongan(biayaAwal) : BigDecimal.ZERO;
        total = biayaAwal.subtract(diskon);
        if (total.compareTo(BigDecimal.ZERO) < 0) total = BigDecimal.ZERO;
        
        lblDiskon.setText("Rp " + diskon.toString());
        lblTotal.setText("Rp " + total.toString());
    }
    
    private void prosesPemesanan() {
        Pets pet = (Pets) cbPet.getSelectedItem();
        if (pet == null) {
            JOptionPane.showMessageDialog(this, "Pilih hewan terlebih dahulu.");
            return;
        }
        String keluhan = taKeluhan.getText().trim();
        String kodeKupon = (kuponTerpakai != null) ? kuponTerpakai.getKode() : null;
        BigDecimal diskonFinal = diskon;
        BigDecimal totalFinal = total;
        String kodePemesanan = "ONL" + System.currentTimeMillis();
        LocalDate tanggalKonsultasi = modeLangsung ? LocalDate.now() : LocalDate.now().plusDays(1);
        String waktuKonsultasi;
        if (modeLangsung) {
            waktuKonsultasi = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            if (cbJam.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(this, "Pilih jam konsultasi.");
                return;
            }
            waktuKonsultasi = (String) cbJam.getSelectedItem();
        }

        PemesananOnline pesan = new PemesananOnline();
        pesan.setKodePemesanan(kodePemesanan);
        pesan.setIdPemilik(pemilik.getIdPemilik());
        pesan.setIdDokter(dokter.getIdDokter());
        pesan.setIdPet(pet.getIdPet());
        pesan.setTanggalKonsultasi(tanggalKonsultasi);
        pesan.setWaktuKonsultasi(waktuKonsultasi);
        pesan.setKeluhan(keluhan);
        pesan.setBiayaKonsultasi(biayaAwal);
        pesan.setKuponDigunakan(kodeKupon);
        pesan.setJumlahDiskon(diskonFinal);
        pesan.setTotalBiaya(totalFinal);
        pesan.setStatus(modeLangsung ? "sudah bayar" : "menunggu");
        pesan.setWaktuPemesanan(Timestamp.valueOf(LocalDateTime.now()));

        boolean success = pemesananController.createPemesananOnline(pesan);
        if (success) {
            if (modeLangsung) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");
                dispose();
                new StrukView((JFrame) getParent(), pesan, true, pemilik, dokter);
            } else {
                if (success) {
                    JOptionPane.showMessageDialog(this, "Pesanan berhasil disimpan.");
                    dispose();
                    new StrukView((JFrame) getParent(), pesan);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memproses pemesanan.");
        }
    }
}