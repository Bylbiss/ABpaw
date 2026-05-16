/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.AdminController;
import abpaw.model.entity.Admin;
import abpaw.view.LoginView;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardAdminView extends JFrame {
    private Admin admin;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton btnMasterData, btnKupon, btnLaporan, btnProfil;
    private JPanel subMasterPanel;
    private AdminController adminController;
    private boolean isMasterExpanded = false;

    public DashboardAdminView(Admin admin) {
        this.admin = admin;
        this.adminController = new AdminController();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Dashboard Admin - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 51, 102));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        JLabel logoLabel = new JLabel("AB Paw - Admin Panel");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);
        JLabel userLabel = new JLabel("Admin: " + admin.getUsername());
        userLabel.setForeground(Color.WHITE);
        headerPanel.add(userLabel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 248, 255));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        // Menu DATA MASTER (expandable)
        btnMasterData = createSidebarButton("📁 DATA MASTER", true);
        
        // Submenu panel
        subMasterPanel = new JPanel();
        subMasterPanel.setLayout(new BoxLayout(subMasterPanel, BoxLayout.Y_AXIS));
        subMasterPanel.setBackground(new Color(240, 248, 255));
        subMasterPanel.setVisible(false);
        subMasterPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        
        JButton btnDokter = createSubMenuButton("   👨‍⚕️ Manajemen Dokter");
        JButton btnPemilik = createSubMenuButton("   👤 Manajemen Pemilik");
        
        btnDokter.addActionListener(e -> showPasswordDialog("dokter", "dok12"));
        btnPemilik.addActionListener(e -> showPasswordDialog("pemilik", "pem12"));
        
        subMasterPanel.add(btnDokter);
        subMasterPanel.add(Box.createVerticalStrut(5));
        subMasterPanel.add(btnPemilik);
        
        btnMasterData.addActionListener(e -> {
            subMasterPanel.setVisible(!subMasterPanel.isVisible());
            sidebar.revalidate();
            sidebar.repaint();
        });

        // Menu lainnya
        btnKupon = createSidebarButton("🎫 DISKON & KUPON", false);
        btnLaporan = createSidebarButton("📊 LAPORAN", false);
        btnProfil = createSidebarButton("👤 PROFIL ADMIN", false);
        JButton btnLogout = createSidebarButton("🚪 LOGOUT", false);

        sidebar.add(btnMasterData);
        sidebar.add(subMasterPanel);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnKupon);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnLaporan);
        sidebar.add(Box.createVerticalStrut(10));
        sidebar.add(btnProfil);
        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(btnLogout);
        sidebar.add(Box.createVerticalGlue());

        add(sidebar, BorderLayout.WEST);

        // Content panel
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);

        contentPanel.add(createWelcomePanel(), "welcome");
        contentPanel.add(new ManageDokterView(), "dokter");
        contentPanel.add(new ManagePemilikView(), "pemilik");
        contentPanel.add(new ManageKuponView(), "kupon");
        contentPanel.add(new LaporanView(), "laporan");
        contentPanel.add(createProfilPanel(), "profil");

        add(contentPanel, BorderLayout.CENTER);

        btnKupon.addActionListener(e -> cardLayout.show(contentPanel, "kupon"));
        btnLaporan.addActionListener(e -> cardLayout.show(contentPanel, "laporan"));
        btnProfil.addActionListener(e -> cardLayout.show(contentPanel, "profil"));
        
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView();
            }
        });

        cardLayout.show(contentPanel, "welcome");
    }

    private void showPasswordDialog(String target, String expectedPassword) {
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel("Masukkan password untuk mengakses data " + 
            (target.equals("dokter") ? "Dokter" : "Pemilik") + ":"), BorderLayout.NORTH);
        JPasswordField passwordField = new JPasswordField(15);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        int result = JOptionPane.showConfirmDialog(this, passwordPanel, 
            "Verifikasi Akses", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            String enteredPassword = new String(passwordField.getPassword());
            if (enteredPassword.equals(expectedPassword)) {
                cardLayout.show(contentPanel, target);
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Password salah! Akses ditolak.", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel welcomeLabel = new JLabel("Selamat Datang, " + admin.getNama() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(new Color(0, 51, 102));
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(welcomeLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Anda login sebagai Admin Dunia");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(Color.GRAY);
        
        gbc.gridy = 1;
        panel.add(subtitleLabel, gbc);
        
        return panel;
    }

    private JButton createSidebarButton(String text, boolean isBold) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(new Color(52, 152, 219));
        btn.setForeground(Color.BLUE);  // ✅ Warna teks BIRU
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(41, 128, 185));
                btn.setForeground(Color.WHITE);  // saat hover jadi putih biar terbaca
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(52, 152, 219));
                btn.setForeground(Color.BLUE);   // kembali ke biru
            }
        });
        return btn;
    }

    private JButton createSubMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 35));
        btn.setBackground(new Color(240, 248, 255));
        btn.setForeground(new Color(0, 102, 204));  // ✅ Warna teks BIRU TUA
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(220, 235, 250));
                btn.setForeground(new Color(0, 51, 153));  // hover biru lebih tua
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(240, 248, 255));
                btn.setForeground(new Color(0, 102, 204)); // kembali ke biru
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
        panel.add(new JLabel("ID Admin:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(admin.getIdAdmin())), gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getUsername()), gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getNama()), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getEmail()), gbc);

        JButton gantiPassword = new JButton("Ganti Password");
        gantiPassword.addActionListener(e -> {
            String newPass = JOptionPane.showInputDialog(this, "Masukkan password baru:");
            if (newPass != null && !newPass.trim().isEmpty()) {
                boolean success = adminController.updatePassword(admin.getIdAdmin(), newPass);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Password berhasil diubah.");
                    admin.setPassword(newPass);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengubah password.");
                }
            }
        });
        gbc.gridx = 0; gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(gantiPassword, gbc);

        return panel;
    }
}