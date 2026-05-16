/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.model.entity.Pemilik;
import abpaw.view.pemilik.PemesananOfflineView;
import abpaw.view.pemilik.ObatView;
import abpaw.view.LoginView;
import abpaw.view.pemilik.RiwayatView;
import abpaw.view.components.ImagePanel;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class DashboardPemilikView extends JFrame {
    private Pemilik pemilik;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ChatDokterView chatDokterView;
    private JButton btnChat, btnOffline, btnObat, btnRiwayat, btnProfil;

    public DashboardPemilikView(Pemilik pemilik) {
        this.pemilik = pemilik;
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        // Tampilkan chat dokter sebagai default
        showChatDokter();
    }

    private void initComponents() {
        setTitle("Dashboard - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // Header panel (menu)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(0, 80, 160));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo kiri
        JLabel logoLabel = new JLabel("AB Paw");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Menu panel (FlowLayout)
        JPanel menuPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        menuPanel.setOpaque(false);
        btnChat = createMenuButton("Chat Dokter");
        btnOffline = createMenuButton("Pesan Offline");
        btnObat = createMenuButton("Beli Obat");
        btnRiwayat = createMenuButton("Riwayat Pesanan");
        btnProfil = createMenuButton("Profil");
        menuPanel.add(btnChat);
        menuPanel.add(btnOffline);
        menuPanel.add(btnObat);
        menuPanel.add(btnRiwayat);
        menuPanel.add(btnProfil);
        headerPanel.add(menuPanel, BorderLayout.CENTER);
        
        
       // Panel kanan untuk userLabel dan tombol Logout
      
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(new Color(173, 216, 230)); // Biru muda
        btnLogout.setForeground(Color.BLACK);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(100, 149, 237));
                btnLogout.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(173, 216, 230));
                btnLogout.setForeground(Color.BLACK);
            }
}       );

        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin logout?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView();
            }
        });
        
        btnRiwayat.addActionListener(e -> cardLayout.show(contentPanel, "riwayat"));
        
        JLabel userLabel = new JLabel("Halo, " + pemilik.getUsername());
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content panel dengan CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.blue);

        // Tambahkan panel-panel ke card
        chatDokterView = new ChatDokterView(pemilik);
        contentPanel.add(chatDokterView, "chat");
        
        PemesananOfflineView PemesananOfflineView = new PemesananOfflineView(pemilik);
        contentPanel.add(PemesananOfflineView, "offline");
       
        contentPanel.add(new ObatView(pemilik), "obat");
        contentPanel.add(new RiwayatView(pemilik), "riwayat");
        contentPanel.add(new ProfilPemilikView(pemilik), "profil");

        add(contentPanel, BorderLayout.CENTER);

        // Event menu
        btnChat.addActionListener(e -> cardLayout.show(contentPanel, "chat"));
        btnOffline.addActionListener(e -> cardLayout.show(contentPanel, "offline"));
        btnObat.addActionListener(e -> cardLayout.show(contentPanel, "obat"));
        btnRiwayat.addActionListener(e -> cardLayout.show(contentPanel, "riwayat"));
        btnProfil.addActionListener(e -> cardLayout.show(contentPanel, "profil"));
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(Color.BLUE);
        btn.setBackground(new Color(0, 102, 204));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14)); //tulisan  atas
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

    private void showChatDokter() {
        cardLayout.show(contentPanel, "chat");
    }
}