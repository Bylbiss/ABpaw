/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.AdminController;
import abpaw.model.entity.Admin;
import abpaw.utils.IconHelper;
import abpaw.view.LoginView;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DashboardAdminView extends JFrame {

    // Encapsulation: atribut private hanya bisa diakses lewat method di kelas ini
    private Admin admin;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JButton btnMasterData, btnKupon, btnLaporan, btnProfil;
    private JPanel subMasterPanel;
    private AdminController adminController;
    private boolean isMasterExpanded = false;
    
    private final Color biru = new Color(0, 102, 204);
    private final Color biruGelap = new Color(0, 80, 160);
    private final Color biruTerang = new Color(200, 220, 255);

    // Constructor: inisialisasi objek saat Dashboard dibuat
    public DashboardAdminView(Admin admin) {
        this.admin = admin;
        this.adminController = new AdminController();
        initComponents();     // memanggil method untuk membuat GUI
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Method untuk membangun semua komponen GUI (Swing & Layout)
    private void initComponents() {
        setTitle("Dashboard Admin - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(biru);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        ImageIcon pawIcon = IconHelper.getScaledPawIcon(35, 35);
        JLabel logoLabel = new JLabel(" AB Paw - Admin Panel", pawIcon, SwingConstants.LEFT);
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

        ImageIcon folderIcon = IconHelper.getScaledIcon("/images/folder.png", 26, 26);
        ImageIcon dokterIcon = IconHelper.getScaledIcon("/images/dokter.png", 20, 20);
        ImageIcon pemilikIcon = IconHelper.getScaledIcon("/images/pemilik.png", 20, 20);
        ImageIcon kuponIcon = IconHelper.getScaledIcon("/images/kupon.png", 26, 26);
        ImageIcon laporanIcon = IconHelper.getScaledIcon("/images/laporan.png", 28, 28);
        ImageIcon profilIcon = IconHelper.getScaledIcon("/images/profil.png", 20, 20);
        ImageIcon logoutIcon = IconHelper.getScaledIcon("/images/logout.png", 20, 20);
        
        btnMasterData = createSidebarButton("DATA MASTER", true);
        btnMasterData.setIcon(folderIcon);
        btnMasterData.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnMasterData.setIconTextGap(10);
        
        subMasterPanel = new JPanel();
        subMasterPanel.setLayout(new BoxLayout(subMasterPanel, BoxLayout.Y_AXIS));
        subMasterPanel.setBackground(new Color(240, 248, 255));
        subMasterPanel.setVisible(false);
        subMasterPanel.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JButton btnDokter = createSubMenuButton("Manajemen Dokter");
        btnDokter.setIcon(dokterIcon);
        btnDokter.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnDokter.setIconTextGap(10);
        
        JButton btnPemilik = createSubMenuButton(" Manajemen Pemilik");
        btnPemilik.setIcon(pemilikIcon);
        btnPemilik.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnPemilik.setIconTextGap(10);

        // Event Handling (ActionListener) untuk tombol
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

        btnKupon = createSidebarButton(" DISKON & KUPON", false);
        btnKupon.setIcon(kuponIcon);
        btnKupon.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnKupon.setIconTextGap(10);
        
        btnLaporan = createSidebarButton(" LAPORAN", false);
        btnLaporan.setIcon(laporanIcon);
        btnLaporan.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnLaporan.setIconTextGap(10);
        
        btnProfil = createSidebarButton(" PROFIL ADMIN", false);
        btnProfil.setIcon(profilIcon);
        btnProfil.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnProfil.setIconTextGap(10);
        
        JButton btnLogout = createSidebarButton(" LOGOUT", false);
        btnLogout.setIcon(logoutIcon);
        btnLogout.setHorizontalTextPosition(SwingConstants.RIGHT);
        btnLogout.setIconTextGap(10);

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

        // Content Area (CardLayout)
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

        // Event Handling untuk menu sidebar
        btnKupon.addActionListener(e -> cardLayout.show(contentPanel, "kupon"));
        btnLaporan.addActionListener(e -> cardLayout.show(contentPanel, "laporan"));
        btnProfil.addActionListener(e -> cardLayout.show(contentPanel, "profil"));

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView();   // pindah ke halaman login
            }
        });

        cardLayout.show(contentPanel, "welcome");
    }

    // Method untuk menampilkan dialog password (validasi akses)
    private void showPasswordDialog(String target, String expectedPassword) {
        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.add(new JLabel("Masukkan password untuk mengakses data "
                + (target.equals("dokter") ? "Dokter" : "Pemilik") + ":"), BorderLayout.NORTH);
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

    // Membuat panel welcome (tanpa layout manager khusus, pakai GridBagLayout)
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(40, 40, 40, 40));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.anchor = GridBagConstraints.CENTER;

        JLabel welcomeLabel = new JLabel("Selamat Datang, " + admin.getNama() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(biru);

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

    // Membuat tombol sidebar dengan efek hover (MouseListener)
    private JButton createSidebarButton(String text, boolean isBold) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setBackground(biru);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, 14));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(biruGelap);
                btn.setForeground(Color.WHITE);
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(biru);
                btn.setForeground(Color.WHITE);
            }
        });
        return btn;
    }

    private JButton createSubMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 35));
        btn.setBackground(new Color(240, 248, 255));
        btn.setForeground(biru);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.PLAIN, 12));
        btn.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 5));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(biruTerang);
                btn.setForeground(biruGelap);
            }

            public void mouseExited(MouseEvent evt) {
                btn.setBackground(new Color(240, 248, 255));
                btn.setForeground(biru);
            }
        });
        return btn;
    }

    // Panel profil admin (menggunakan GridBagLayout)
    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("ID Admin:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(String.valueOf(admin.getIdAdmin())), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getUsername()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getNama()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        panel.add(new JLabel(admin.getEmail()), gbc);

        JButton gantiPassword = new JButton("Ganti Password");

        // Event Handling untuk mengganti password
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
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        panel.add(gantiPassword, gbc);

        return panel;
    }
}
