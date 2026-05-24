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
import javax.swing.border.TitledBorder;

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

    private final Color mainBlue = new Color(0, 102, 204);

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

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Pilih Dokter Hewan");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(mainBlue);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(10));

        spesialisasiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        spesialisasiPanel.setBackground(Color.WHITE);
        spesialisasiPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        topPanel.add(spesialisasiPanel);
        topPanel.add(Box.createVerticalStrut(10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        searchField = new JTextField(20);
        searchField.setToolTipText("Cari nama dokter...");
        JButton searchBtn = new JButton("Cari");
        searchBtn.setBackground(mainBlue);
        searchBtn.setForeground(Color.WHITE);
        searchBtn.setFocusPainted(false);
        filterCombo = new JComboBox<>(new String[]{"Murah ke Mahal", "Mahal ke Murah", "Sesuai Hewan Saya"});
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
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(mainBlue), "Daftar Dokter",
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION,
                new Font("Arial", Font.BOLD, 12), mainBlue));
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
            if (selectedSpesialisasi.equals(spes)) {
                btn.setBackground(mainBlue);
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.LIGHT_GRAY);
                btn.setForeground(Color.BLACK);
            }
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

        List<Dokter> filteredDokter;
        if (!selectedSpesialisasi.equals("Semua")) {
            filteredDokter = dokterController.getDokterBySpesialisasi(selectedSpesialisasi);
        } else {
            filteredDokter = dokterController.getAllDokter();
        }

        if (!keyword.isEmpty()) {
            filteredDokter.removeIf(d -> !d.getNamaLengkap().toLowerCase().contains(keyword.toLowerCase()));
        }

        if (filter.equals("Sesuai Hewan Saya")) {
            List<Pets> hewanSaya = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            List<String> jenisHewanSaya = hewanSaya.stream()
                    .map(Pets::getJenisHewan)
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());

            if (!jenisHewanSaya.isEmpty()) {
                filteredDokter = filteredDokter.stream()
                        .filter(dokter -> {
                            String spesies = dokter.getSpesiesHewan();
                            if (spesies == null || spesies.trim().isEmpty()) {
                                return false;
                            }
                            return Arrays.stream(spesies.split(","))
                                    .map(String::trim)
                                    .map(String::toLowerCase)
                                    .anyMatch(jenisHewanSaya::contains);
                        })
                        .collect(Collectors.toList());
            } else {
                filteredDokter.clear();
            }
        }

        if (filter.equals("Murah ke Mahal")) {
            filteredDokter.sort((a, b) -> a.getBiayaKonsultasi().compareTo(b.getBiayaKonsultasi()));
        } else if (filter.equals("Mahal ke Murah")) {
            filteredDokter.sort((a, b) -> b.getBiayaKonsultasi().compareTo(a.getBiayaKonsultasi()));
        }

        if (filteredDokter.isEmpty()) {
            JLabel emptyLabel = new JLabel("Tidak ada dokter yang ditemukan sesusi hewan anda.");
            emptyLabel.setForeground(Color.RED);
            emptyLabel.setHorizontalAlignment(SwingConstants.CENTER);
            dokterListPanel.add(emptyLabel);
        } else {
            for (Dokter d : filteredDokter) {
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
                g2.setColor(mainBlue);
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
        nameLabel.setForeground(mainBlue);
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
        pilihBtn.setBackground(mainBlue);
        pilihBtn.setForeground(Color.WHITE);
        pilihBtn.setFocusPainted(false);
        card.add(pilihBtn, BorderLayout.EAST);

        pilihBtn.addActionListener(e -> {
            // cek apakah dokter sesuai dengan hewan pemilik
            List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
            if (petsList.isEmpty()) {
                JOptionPane.showMessageDialog(ChatDokterView.this,
                        "Anda belum memiliki hewan peliharaan.\nSilakan tambahkan hewan terlebih dahulu di menu Profil.",
                        "Tidak Ada Hewan", JOptionPane.WARNING_MESSAGE);
                return;
            }

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
                String pesan = "Dokter Tidak Sesuai\n\n"
                        + "Maaf, dokter ini khusus menangani: " + spesiesDokterStr + "\n\n"
                        + "Silakan pilih dokter lain yang sesuai dengan hewan peliharaan Anda.";
                JOptionPane.showMessageDialog(ChatDokterView.this, pesan, "Dokter Tidak Sesuai", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

            // Simpan LookAndFeel lama
            LookAndFeel oldLookAndFeel = UIManager.getLookAndFeel();

            // Set sementara ke Metal
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
        card.setPreferredSize(new Dimension(card.getPreferredSize().width, 110));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        card.setMinimumSize(new Dimension(200, 110));
        return card;
    }
}
