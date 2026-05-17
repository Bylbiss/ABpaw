/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.DokterController;
import abpaw.controller.PetsController;
import abpaw.view.pemilik.PemesananOnlineView;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Pets;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
/**
 *
 * @author LOQ
 */
public class ChatDokterView extends JPanel {
    private PetsController petsController;
    private Pemilik pemilik;
    private JPanel spesialisasiPanel;
    private JTextField searchField;
    private JComboBox<String> filterCombo;
    private JPanel dokterListPanel;
    private JScrollPane scrollPane;
    private DokterController dokterController;
    private String selectedSpesialisasi = "Semua";

    public ChatDokterView(Pemilik pemilik) {
        this.pemilik = pemilik;
        dokterController = new DokterController();
        petsController = new PetsController();
        initComponents();
        loadSpesialisasi();
        loadDokter();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(Color.WHITE);

        // Panel atas: spesialisasi dan pencarian
        JPanel topPanel = new JPanel(new BorderLayout(10, 5));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Pilih Dokter Hewan");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Spesialisasi panel dengan tombol-tombol
        spesialisasiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        spesialisasiPanel.setBackground(Color.WHITE);
        JScrollPane spesialisasiScroll = new JScrollPane(spesialisasiPanel);
        spesialisasiScroll.setBorder(null);
        spesialisasiScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        spesialisasiScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        topPanel.add(spesialisasiScroll, BorderLayout.CENTER);

        // Pencarian dan filter
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchField = new JTextField(20);
        searchField.setToolTipText("Cari nama dokter...");
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

        // Panel daftar dokter
        dokterListPanel = new JPanel();
        dokterListPanel.setLayout(new BoxLayout(dokterListPanel, BoxLayout.Y_AXIS));
        dokterListPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(dokterListPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Dokter"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadSpesialisasi() {
        spesialisasiPanel.removeAll();
        String[] spesialisasiList = {"Semua", "Dokter Umum", "Spesialis Penyakit Dalam", "Spesialis Gigi",
                "Spesialis Kulit", "Spesialis Reproduksi", "Spesialis Parasitologi & Infeksi", "Spesialis Bedah"};
        for (String spes : spesialisasiList) {
            JButton btn = new JButton(spes);
            btn.setBackground(selectedSpesialisasi.equals(spes) ? new Color(0, 102, 204) : Color.LIGHT_GRAY);
            btn.setForeground(selectedSpesialisasi.equals(spes) ? new Color(0, 102, 204) : Color.BLACK);
            btn.setFocusPainted(false);
            btn.addActionListener(e -> {
                selectedSpesialisasi = spes;
                loadSpesialisasi(); // refresh warna tombol
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
        // Filter keyword
        if (!keyword.isEmpty()) {
            dokterList.removeIf(d -> !d.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()));
        }
        // Sorting berdasarkan filter (sederhana)
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
            dokterListPanel.add(new JLabel("Tidak ada dokter yang ditemukan."));
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
        
        JLabel nameLabel = new JLabel(dokter.getNamaLengkap());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(new Color(0, 51, 102)); 
        JLabel spesLabel = new JLabel("Spesialisasi: " + dokter.getSpesialisasi());
        spesLabel.setForeground(Color.DARK_GRAY); 
        JLabel priceLabel = new JLabel("Biaya Konsultasi: Rp " + dokter.getBiayaKonsultasi());
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
        
        JButton pilihBtn = new JButton("Pilih");
        pilihBtn.setBackground(new Color(0, 102, 204));
        pilihBtn.setForeground(Color.BLUE);
        pilihBtn.setFocusPainted(false);
        card.add(pilihBtn, BorderLayout.EAST);

        pilihBtn.addActionListener(e -> {
            // Validasi: cek apakah dokter sesuai dengan hewan pemilik
            List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            if (petsList.isEmpty()) {
                JOptionPane.showMessageDialog(ChatDokterView.this,
                    "Anda belum memiliki hewan peliharaan.\nSilakan tambahkan hewan terlebih dahulu di menu Profil.",
                    "Tidak Ada Hewan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Dapatkan daftar spesies hewan yang ditangani dokter (pisah koma)
            String spesiesDokterStr = dokter.getSpesiesHewan();
            if (spesiesDokterStr == null || spesiesDokterStr.trim().isEmpty()) {
                JOptionPane.showMessageDialog(ChatDokterView.this,
                    "Data dokter tidak lengkap (spesies hewan tidak terdaftar).",
                    "Informasi Tidak Lengkap", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<String> spesiesDokter = Arrays.stream(spesiesDokterStr.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            // Cek apakah ada setidaknya satu hewan pemilik yang jenisnya cocok
            boolean adaYangSesuai = petsList.stream()
                    .anyMatch(pet -> spesiesDokter.contains(pet.getJenisHewan().toLowerCase()));

            if (!adaYangSesuai) {
                // Tampilkan dialog peringatan seperti gambar
                String pesan = "Dokter Tidak Sesuai\n\n" +
                               "Maaf, dokter ini khusus menangani: " + spesiesDokterStr + "\n\n" +
                               "Silakan pilih dokter lain yang sesuai dengan hewan peliharaan Anda.";
                JOptionPane.showMessageDialog(ChatDokterView.this, pesan, "Dokter Tidak Sesuai", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
    
            // Simpan LookAndFeel lama
            LookAndFeel oldLookAndFeel = UIManager.getLookAndFeel();
    
            // Set sementara ke Metal (yang tidak punya default button biru)
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    
            int option = JOptionPane.showOptionDialog(parent,
                "Pilih konsultasi dengan " + dokter.getNamaLengkap(),
                "Pilih Metode",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"Pesan Sekarang", "Pesan Nanti"},
                null);
    
            // Kembalikan LookAndFeel semula
            try {
                UIManager.setLookAndFeel(oldLookAndFeel);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    
            if (option == 0 || option == 1) {
                boolean langsung = (option == 0);
                new PemesananOnlineView(parent, pemilik, dokter, langsung);
            }
        });
        return card;
    }
}