/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.PemesananController;
import abpaw.model.entity.Pemilik;
import abpaw.utils.IconHelper;
import abpaw.view.pemilik.PemesananOfflineView;
import abpaw.view.pemilik.ObatView;
import abpaw.view.LoginView;
import abpaw.view.pemilik.RiwayatView;
import abpaw.view.components.ImagePanel;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.TimerTask;
/**
 *
 * @author LOQ
 */
public class DashboardPemilikView extends JFrame {
    private Pemilik pemilik;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private ChatDokterView chatDokterView;
    private JButton btnChat, btnOffline, btnObat, btnRiwayat, btnProfil;
    private PemesananController pemesananController = new PemesananController();

    private final Color pink = new Color(232, 62, 140);
    private final Color pinkGelap = new Color(200, 40, 110);
    private final Color putih = Color.WHITE;
    private final Color hitam = Color.BLACK;
    
    public DashboardPemilikView(Pemilik pemilik) {
        this.pemilik = pemilik;
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        showChatDokter();
    }

    private void initComponents() {
        setTitle("Dashboard - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLayout(new BorderLayout());

        // Header panel (menu)
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(pink);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        ImageIcon pawIcon = IconHelper.getScaledPawIcon(35, 35);
        JLabel logoLabel = new JLabel(" AB Paw", pawIcon, SwingConstants.LEFT);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(putih);
        headerPanel.add(logoLabel, BorderLayout.WEST);

        // Menu panel 
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
        
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        rightPanel.setOpaque(false);

        JButton btnLogout = new JButton("Logout");
        btnLogout.setBackground(pinkGelap);
        btnLogout.setForeground(putih);
        btnLogout.setFont(new Font("Arial", Font.BOLD, 12));
        btnLogout.setFocusPainted(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogout.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

        btnLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(new Color(0, 60, 120));
                btnLogout.setForeground(putih);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogout.setBackground(pinkGelap);
                btnLogout.setForeground(putih);
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
        userLabel.setForeground(putih);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));

        rightPanel.add(userLabel);
        rightPanel.add(btnLogout);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);

        // Content panel dengan CardLayout
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(putih);

        chatDokterView = new ChatDokterView(pemilik);
        contentPanel.add(chatDokterView, "chat");
        
        PemesananOfflineView offlineView = new PemesananOfflineView(pemilik, this);
        contentPanel.add(offlineView, "offline");
       
        contentPanel.add(new ObatView(pemilik), "obat");
        contentPanel.add(new RiwayatView(pemilik), "riwayat");
        contentPanel.add(new ProfilPemilikView(pemilik), "profil");

        add(contentPanel, BorderLayout.CENTER);
        
        java.util.Timer timer = new java.util.Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                pemesananController.updateStatusOtomatis();
                SwingUtilities.invokeLater(() -> {
                    if (cardLayout != null && contentPanel != null) {
                        for (Component comp : contentPanel.getComponents()) {
                            if (comp.isVisible() && comp instanceof RiwayatView) {
                                ((RiwayatView) comp).loadRiwayat();
                            }
                        }
                    }
                });
            }
        }, 0, 10 * 60 * 1000);

        // Event menu
        btnChat.addActionListener(e -> cardLayout.show(contentPanel, "chat"));
        btnOffline.addActionListener(e -> cardLayout.show(contentPanel, "offline"));
        btnObat.addActionListener(e -> cardLayout.show(contentPanel, "obat"));
        btnRiwayat.addActionListener(e -> cardLayout.show(contentPanel, "riwayat"));
        btnProfil.addActionListener(e -> cardLayout.show(contentPanel, "profil"));
    }

    private JButton createMenuButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(putih);
        btn.setBackground(pink);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 14)); 
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(pinkGelap);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(pink);
            }
        });
        return btn;
    }

    private void showChatDokter() {
        cardLayout.show(contentPanel, "chat");
    }
     public void showDashboard() {
         // Kembali ke halaman chat dokter
        cardLayout.show(contentPanel, "chat");
        this.revalidate();
        this.repaint();
    } 
}