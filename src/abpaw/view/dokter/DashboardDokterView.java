/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

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

    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
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
        editBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "Fitur edit profil dokter akan segera hadir."));
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        panel.add(editBtn, gbc);

        return panel;
    }
}