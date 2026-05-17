/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view;

import abpaw.controller.AuthController;
import abpaw.model.entity.Admin;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.User;
import abpaw.view.admin.DashboardAdminView;
import abpaw.view.dokter.DashboardDokterView;
import abpaw.view.pemilik.DashboardPemilikView;
import javax.swing.*;
import java.awt.*;
/**
 *
 * @author LOQ
 */
public class LoginView extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JLabel lblMessage;
    private AuthController authController;

    public LoginView() {
        authController = new AuthController();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Login - AB Paw Klinik Hewan");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 350);
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Silahkan Masuk ke Akun Anda");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Username:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(txtUsername, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(txtPassword, gbc);

        btnLogin = new JButton("Masuk");
        btnLogin.setBackground(Color.WHITE); // Background putih
        btnLogin.setForeground(new Color(0, 102, 204)); // Tulisan BIRU
        btnLogin.setFont(new Font("Arial", Font.PLAIN,14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 2)); // Border biru biar keliatan
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        formPanel.add(btnLogin, gbc);

        lblMessage = new JLabel(" ");
        lblMessage.setForeground(Color.RED);
        gbc.gridy = 4;
        formPanel.add(lblMessage, gbc);

        JPanel footerPanel = new JPanel();
        JLabel registerLink = new JLabel("Belum punya akun? Daftar di sini");
        registerLink.setForeground(new Color(0, 102, 204));
        registerLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        registerLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                dispose();
                new RegisterView();
            }
        });
        footerPanel.add(registerLink);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblMessage.setText("Username dan password harus diisi!");
            return;
        }

        User user = authController.login(username, password);
        if (user == null) {
            lblMessage.setText("Login gagal! Periksa username dan password.");
            return;
        }

        // Buka dashboard sesuai role
        dispose();
        switch (user.getRole()) {
            case "ADMIN":
                new DashboardAdminView((Admin) user);
                break;
            case "DOKTER":
                new DashboardDokterView((Dokter) user);
                break;
            case "PEMILIK":
                new DashboardPemilikView((Pemilik)user);
                break;
        }
    }
}