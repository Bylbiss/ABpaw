/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.DokterController;
import abpaw.controller.PemesananController;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.PemesananOffline;
import abpaw.model.entity.PemesananOnline;
import abpaw.model.entity.User;
import abpaw.view.LoginView;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class DashboardDokterView extends JFrame {
    private Dokter dokter;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton btnChat, btnResep, btnStatus, btnProfil, btnLogout;
    private PemesananController pemesananController;

    public DashboardDokterView(Dokter dokter) {
        this.dokter = dokter;
        this.pemesananController = new PemesananController();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Dashboard Dokter - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // Header panel (atas)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 102, 204));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logoLabel = new JLabel("AB Paw - Dokter");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        JLabel userLabel = new JLabel("Dr. " + dokter.getNamaLengkap());
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Sidebar kiri (menu)
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 248, 255));
        
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(200, getHeight()));

        btnChat = createSidebarButton("Chat Pasien");
        btnResep = createSidebarButton("Buat / Lihat Resep");
        btnStatus = createSidebarButton("Update Status Pemesanan");
        btnProfil = createSidebarButton("Profil Saya");
        btnLogout = createSidebarButton("Logout");
        
        sidebar.add(btnChat);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnResep);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnStatus);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnProfil);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // Content panel dengan CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        // Tambahkan panel-panel
        contentPanel.add(new ChatPasienView(dokter), "chat");
        contentPanel.add(new ResepObatView(dokter), "resep");
        contentPanel.add(new UpdateStatusView(dokter), "status");
        contentPanel.add(createProfilPanel(), "profil");

        add(contentPanel, BorderLayout.CENTER);

        // Event menu
        btnChat.addActionListener(e -> cardLayout.show(contentPanel, "chat"));
        btnResep.addActionListener(e -> cardLayout.show(contentPanel, "resep"));
        btnStatus.addActionListener(e -> cardLayout.show(contentPanel, "status"));
        btnProfil.addActionListener(e -> cardLayout.show(contentPanel, "profil"));
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView();
            }
        });

        // Default tampilkan chat
        cardLayout.show(contentPanel, "chat");
    }

    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(180, 40));
        btn.setBackground(Color.WHITE);
        btn.setForeground(new Color(0, 102, 204));
        btn.setOpaque(true);
        btn.setContentAreaFilled(true);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 80, 160));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(0, 102, 204));
            }
        });
        return btn;
    }
    
    private void editProfilDokter() {
        // Buat dialog untuk edit profil
        JDialog editDialog = new JDialog(this, "Edit Profil Dokter", true);
        editDialog.setLayout(new BorderLayout());
        editDialog.setSize(500, 550);
        editDialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;

        // Field yang bisa diedit
        JTextField txtNamaDepan = new JTextField(dokter.getNamaDepan(), 15);
        JTextField txtNamaBelakang = new JTextField(dokter.getNamaBelakang(), 15);
        JTextField txtUsername = new JTextField(dokter.getUsername(), 15);
        JTextField txtEmail = new JTextField(dokter.getEmail(), 15);
        JTextField txtAlamat = new JTextField(dokter.getAlamat(), 15);
        JTextField txtNoHp = new JTextField(dokter.getNoHp(), 15);
        JTextField txtSpesialisasi = new JTextField(dokter.getSpesialisasi(), 15);
        JTextField txtBiaya = new JTextField(dokter.getBiayaKonsultasi().toString(), 15);
        JTextField txtSpesiesHewan = new JTextField(dokter.getSpesiesHewan(), 15);
        JPasswordField txtPassword = new JPasswordField(15);

        // Layout form
        int row = 0;
        gbc.gridy = row++; formPanel.add(new JLabel("Nama Depan:"), gbc); gbc.gridx=1; formPanel.add(txtNamaDepan, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Nama Belakang:"), gbc); gbc.gridx=1; formPanel.add(txtNamaBelakang, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Username:"), gbc); gbc.gridx=1; formPanel.add(txtUsername, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Email:"), gbc); gbc.gridx=1; formPanel.add(txtEmail, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Alamat:"), gbc); gbc.gridx=1; formPanel.add(txtAlamat, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("No HP:"), gbc); gbc.gridx=1; formPanel.add(txtNoHp, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Spesialisasi:"), gbc); gbc.gridx=1; formPanel.add(txtSpesialisasi, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Biaya Konsultasi (Rp):"), gbc); gbc.gridx=1; formPanel.add(txtBiaya, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Spesies Hewan (pisah koma):"), gbc); gbc.gridx=1; formPanel.add(txtSpesiesHewan, gbc); gbc.gridx=0;
        gbc.gridy = row++; formPanel.add(new JLabel("Password Baru (kosongkan jika tidak diubah):"), gbc); gbc.gridx=1; formPanel.add(txtPassword, gbc);

        // Tombol simpan
        JButton btnSimpan = new JButton("Simpan Perubahan");
        btnSimpan.addActionListener(e -> {
            // Validasi
            String biayaStr = txtBiaya.getText().trim();
            if (biayaStr.isEmpty()) {
                JOptionPane.showMessageDialog(editDialog, "Biaya konsultasi harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            BigDecimal biaya;
            try {
                biaya = new BigDecimal(biayaStr);
                if (biaya.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(editDialog, "Biaya harus angka positif.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update data dokter
            dokter.setNamaDepan(txtNamaDepan.getText().trim());
            dokter.setNamaBelakang(txtNamaBelakang.getText().trim());
            dokter.setUsername(txtUsername.getText().trim());
            dokter.setEmail(txtEmail.getText().trim());
            dokter.setAlamat(txtAlamat.getText().trim());
            dokter.setNoHp(txtNoHp.getText().trim());
            dokter.setSpesialisasi(txtSpesialisasi.getText().trim());
            dokter.setBiayaKonsultasi(biaya);
            dokter.setSpesiesHewan(txtSpesiesHewan.getText().trim());

            String newPassword = new String(txtPassword.getPassword());
            if (!newPassword.isEmpty()) {
                if (newPassword.length() < 6) {
                    JOptionPane.showMessageDialog(editDialog, "Password minimal 6 karakter.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                dokter.setPassword(newPassword);
            }

            // Update ke database via DokterController
            DokterController dokterController = new DokterController();
            boolean success = dokterController.updateDokter(dokter);
            if (success) {
                JOptionPane.showMessageDialog(editDialog, "Profil berhasil diperbarui.");
                editDialog.dispose();
                // Refresh tampilan profil (misalnya dengan mengganti panel profil)
                refreshProfilPanel();
            } else {
                JOptionPane.showMessageDialog(editDialog, "Gagal memperbarui profil. Periksa koneksi atau data duplikat.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnSimpan);

        editDialog.add(formPanel, BorderLayout.CENTER);
        editDialog.add(buttonPanel, BorderLayout.SOUTH);
        editDialog.setVisible(true);
    }

    // Method untuk menyegarkan panel profil setelah update
    private void refreshProfilPanel() {
        // Hapus panel profil lama dan ganti dengan yang baru
        Component oldProfil = null;
        for (Component comp : contentPanel.getComponents()) {
            if (comp.getName() != null && comp.getName().equals("profilPanel")) {
                oldProfil = comp;
                break;
            }
        }
        JPanel newProfilPanel = createProfilPanel();
        newProfilPanel.setName("profilPanel");
        if (oldProfil != null) {
            contentPanel.remove(oldProfil);
        }
        contentPanel.add(newProfilPanel, "profil");
        // Jika sedang menampilkan profil, refresh tampilan
        if (cardLayout != null) {
            cardLayout.show(contentPanel, "profil");
        }
    }

    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setName("profilPanel");
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(dokter.getNamaLengkap()), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(dokter.getUsername()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(dokter.getEmail()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Spesialisasi:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(dokter.getSpesialisasi()), gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Biaya Konsultasi:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel("Rp " + dokter.getBiayaKonsultasi()), gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Spesies Hewan:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(dokter.getSpesiesHewan()), gbc);

        JButton editBtn = new JButton("Edit Profil");
        editBtn.addActionListener(e -> editProfilDokter());
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;     
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(editBtn, gbc);

        return panel;
    }
}