/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.pemilik;

import abpaw.controller.AlergiController;
import abpaw.controller.AuthController;
import abpaw.controller.PemilikController;
import abpaw.controller.PetsController;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Pets;
import abpaw.view.LoginView;
import abpaw.view.components.RoundedPanel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ProfilPemilikView extends JPanel {
    private Pemilik pemilik;
    private PemilikController pemilikController;
    private PetsController petsController;
    
    // Tab Profil
    private JTextField txtNamaLengkap, txtEmail, txtNoHp, txtAlamat, txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSimpanProfil;
    
    // Tab Hewan
    private JTable tableHewan;
    private DefaultTableModel tableModel;
    private JButton btnTambahHewan, btnEditHewan, btnHapusHewan;
    
    public ProfilPemilikView(Pemilik pemilik) {
        this.pemilik = pemilik;
        this.pemilikController = new PemilikController();
        this.petsController = new PetsController();
        initComponents();
        loadProfilData();
        loadHewanData();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(new Color(240, 248, 255)); // light blue background
        
        // Tabbed pane dengan warna biru
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(0, 102, 204));
        tabbedPane.setForeground(Color.DARK_GRAY); // diubah dari putih menjadi abu-abu gelap
        tabbedPane.setFont(new Font("Arial", Font.BOLD, 14));
        
        // Panel Informasi Pribadi
        JPanel panelProfil = createProfilPanel();
        tabbedPane.addTab("Informasi Pribadi", panelProfil);
        
        // Panel Hewan Peliharaan
        JPanel panelHewan = createHewanPanel();
        tabbedPane.addTab("Hewan Peliharaan", panelHewan);
        
        add(tabbedPane, BorderLayout.CENTER);
    }
    
    private JPanel createProfilPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Card style panel untuk form
        RoundedPanel formCard = new RoundedPanel(20);
        formCard.setBackground(new Color(255, 255, 255));
        formCard.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        formCard.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        
        // Judul
        JLabel titleLabel = new JLabel("Informasi Pribadi");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(0, 51, 102));
        gbc.gridwidth = 2;
        formCard.add(titleLabel, gbc);
        
        // Nama Lengkap
        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel lblNama = new JLabel("Nama Lengkap:");
        lblNama.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblNama, gbc);
        txtNamaLengkap = new JTextField(20);
        txtNamaLengkap.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNamaLengkap.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtNamaLengkap, gbc);
        
        // Email
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblEmail, gbc);
        txtEmail = new JTextField(20);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 12));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtEmail, gbc);
        
        // Nomor HP
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblNoHp = new JLabel("Nomor HP:");
        lblNoHp.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblNoHp, gbc);
        txtNoHp = new JTextField(20);
        txtNoHp.setFont(new Font("Arial", Font.PLAIN, 12));
        txtNoHp.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtNoHp, gbc);
        
        // Alamat
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblAlamat = new JLabel("Alamat:");
        lblAlamat.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblAlamat, gbc);
        txtAlamat = new JTextField(20);
        txtAlamat.setFont(new Font("Arial", Font.PLAIN, 12));
        txtAlamat.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtAlamat, gbc);
        
        // Username
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblUsername = new JLabel("Username:");
        lblUsername.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblUsername, gbc);
        txtUsername = new JTextField(20);
        txtUsername.setFont(new Font("Arial", Font.PLAIN, 12));
        txtUsername.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtUsername, gbc);
        
        // Password
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel lblPassword = new JLabel("Password Baru (kosongkan jika tidak diubah):");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 12));
        formCard.add(lblPassword, gbc);
        txtPassword = new JPasswordField(20);
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 12));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        gbc.gridx = 1;
        formCard.add(txtPassword, gbc);
        
        // Tombol Simpan
        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        btnSimpanProfil = new JButton("Simpan Perubahan");
        btnSimpanProfil.setBackground(new Color(0, 102, 204));
        btnSimpanProfil.setForeground(Color.DARK_GRAY);
        btnSimpanProfil.setFont(new Font("Arial", Font.BOLD, 14));
        btnSimpanProfil.setFocusPainted(false);
        btnSimpanProfil.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnSimpanProfil.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSimpanProfil.addActionListener(e -> simpanProfil());
        formCard.add(btnSimpanProfil, gbc);
        
        // Tombol Hapus Akun
        gbc.gridx = 1;
        JButton btnHapusAkun = new JButton("Hapus Akun");
        btnHapusAkun.setBackground(new Color(220, 20, 60));
        btnHapusAkun.setForeground(Color.DARK_GRAY);
        btnHapusAkun.setFont(new Font("Arial", Font.BOLD, 12));
        btnHapusAkun.setFocusPainted(false);
        btnHapusAkun.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        btnHapusAkun.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHapusAkun.addActionListener(e -> hapusAkun());
        formCard.add(btnHapusAkun, gbc);
        
        panel.add(formCard, BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createHewanPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Tabel dengan style
        String[] cols = {"Id Hewan", "No", "Nama Hewan", "Jenis Kelamin", "Jenis Hewan", "Ras", "Tanggal Lahir", "Usia", "Berat (kg)", "Sterilisasi", "Alergi"};
        tableModel = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        tableHewan = new JTable(tableModel);
        
        // Sembunyikan kolom ID Hewan (indeks 0)
        tableHewan.getColumnModel().getColumn(0).setMinWidth(0);
        tableHewan.getColumnModel().getColumn(0).setMaxWidth(0);
        tableHewan.getColumnModel().getColumn(0).setWidth(0);
        
        tableHewan.setRowHeight(30);
        tableHewan.setFont(new Font("Arial", Font.PLAIN, 12));
        tableHewan.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tableHewan.getTableHeader().setBackground(new Color(0, 102, 204));
        tableHewan.getTableHeader().setForeground(Color.DARK_GRAY);
        tableHewan.setSelectionBackground(new Color(173, 216, 230));
        
        JScrollPane scrollPane = new JScrollPane(tableHewan);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(0, 102, 204)),
            "Daftar Hewan Peliharaan",
            TitledBorder.DEFAULT_JUSTIFICATION,
            TitledBorder.DEFAULT_POSITION,
            new Font("Arial", Font.BOLD, 14),
            new Color(0, 102, 204)
        ));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Panel tombol aksi hewan
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        
        btnTambahHewan = new JButton("+ Tambah Hewan");
        btnTambahHewan.setBackground(new Color(34, 139, 34));
        btnTambahHewan.setForeground(Color.DARK_GRAY); // diubah dari putih
        btnTambahHewan.setFont(new Font("Arial", Font.BOLD, 12));
        btnTambahHewan.setFocusPainted(false);
        btnTambahHewan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTambahHewan.addActionListener(e -> tambahHewan());
        
        btnEditHewan = new JButton("Edit Hewan");
        btnEditHewan.setBackground(new Color(255, 140, 0));
        btnEditHewan.setForeground(Color.DARK_GRAY); // diubah dari putih
        btnEditHewan.setFont(new Font("Arial", Font.BOLD, 12));
        btnEditHewan.setFocusPainted(false);
        btnEditHewan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnEditHewan.addActionListener(e -> editHewan());
        
        btnHapusHewan = new JButton("Hapus Hewan");
        btnHapusHewan.setBackground(new Color(220, 20, 60));
        btnHapusHewan.setForeground(Color.DARK_GRAY); // diubah dari putih
        btnHapusHewan.setFont(new Font("Arial", Font.BOLD, 12));
        btnHapusHewan.setFocusPainted(false);
        btnHapusHewan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnHapusHewan.addActionListener(e -> hapusHewan());
        
        buttonPanel.add(btnTambahHewan);
        buttonPanel.add(btnEditHewan);
        buttonPanel.add(btnHapusHewan);
        
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void loadProfilData() {
        txtNamaLengkap.setText(pemilik.getNamaPemilik());
        txtEmail.setText(pemilik.getEmail());
        txtNoHp.setText(pemilik.getNoHp());
        txtAlamat.setText(pemilik.getAlamat());
        txtUsername.setText(pemilik.getUsername());
        txtPassword.setText("");
    }
    
    private void loadHewanData() {
        tableModel.setRowCount(0);
        List<Pets> petsList = petsController.getPetsByPemilik(pemilik.getIdPemilik());
        AlergiController alergiController = new AlergiController();
        int no = 1;
        for (Pets p : petsList) {
            String alergiStr = alergiController.getAlergiNamesByPet(p.getIdPet());
            tableModel.addRow(new Object[]{
                p.getIdPet(), // kolom 0: ID tersembunyi
                no++, // kolom 1: No urut
                p.getNamaPet(),
                p.getJenisKelamin(),
                p.getJenisHewan(),
                p.getRas() != null ? p.getRas() : "-",
                p.getTanggalLahir() != null ? p.getTanggalLahir().toString() : "-",
                p.getUsia() != null ? p.getUsia() : "-",
                p.getBerat() != null ? p.getBerat() : "-",
                p.getSterilisasi() != null ? p.getSterilisasi() : "belum",
                alergiStr.isEmpty() ? "-" : alergiStr
            });
        }
    }
    
    private void simpanProfil() {
        String nama = txtNamaLengkap.getText().trim();
        String email = txtEmail.getText().trim();
        String noHp = txtNoHp.getText().trim();
        String alamat = txtAlamat.getText().trim();
        String username = txtUsername.getText().trim();
        String passwordBaru = new String(txtPassword.getPassword());
        
        String[] namaParts = nama.split(" ", 2);
        String namaDepan = namaParts[0];
        String namaBelakang = namaParts.length > 1 ? namaParts[1] : "";
        
        AuthController auth = new AuthController();
        String error = auth.validateUpdatePemilikData(namaDepan, namaBelakang, username,
                passwordBaru, noHp, email, alamat, pemilik.getIdPemilik());
        if (error != null) {
            JOptionPane.showMessageDialog(this, error, "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        pemilik.setNamaPemilik(nama);
        pemilik.setEmail(email);
        pemilik.setNoHp(noHp);
        pemilik.setAlamat(alamat);
        pemilik.setUsername(username);
        
        boolean success;
        if (!passwordBaru.isEmpty()) {
            success = auth.updatePemilikProfil(pemilik, passwordBaru);
        } else {
            success = auth.updatePemilikProfil(pemilik, null);
        }
        
        if (success) {
            JOptionPane.showMessageDialog(this, "Data profil berhasil diperbarui!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Gagal memperbarui data. Coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void tambahHewan() {
        HewanDialog dialog = new HewanDialog((JFrame) SwingUtilities.getWindowAncestor(this), null);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadHewanData();
        }
    }
    
    private void editHewan() {
        int selectedRow = tableHewan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih hewan yang akan diedit.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPet = (int) tableModel.getValueAt(selectedRow, 0);
        Pets pet = petsController.getPetsById(idPet);
        if (pet != null) {
            HewanDialog dialog = new HewanDialog((JFrame) SwingUtilities.getWindowAncestor(this), pet);
            dialog.setVisible(true);
            if (dialog.isSaved()) {
                loadHewanData();
            }
        }
    }
    
    private void hapusAkun() {
        // Dialog input password
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Password:"), gbc);
        JPasswordField pfPassword = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(pfPassword, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("Konfirmasi Password:"), gbc);
        JPasswordField pfConfirm = new JPasswordField(15);
        gbc.gridx = 1;
        panel.add(pfConfirm, gbc);

        int option = JOptionPane.showConfirmDialog(this, panel, "Hapus Akun",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        if (option != JOptionPane.OK_OPTION) {
            return;
        }

        String password = new String(pfPassword.getPassword());
        String confirm = new String(pfConfirm.getPassword());

        if (password.isEmpty() || confirm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Password tidak boleh kosong.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!password.equals(confirm)) {
            JOptionPane.showMessageDialog(this, "Password dan konfirmasi tidak cocok.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ambil data pemilik dari database (untuk mendapatkan hash password)
        Pemilik current = pemilikController.getPemilikById(pemilik.getIdPemilik());
        if (current == null) {
            JOptionPane.showMessageDialog(this, "Data pemilik tidak ditemukan.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Verifikasi password dengan BCrypt
        boolean match = org.mindrot.jbcrypt.BCrypt.checkpw(password, current.getPassword());
        if (!match) {
            JOptionPane.showMessageDialog(this, "Password salah.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konfirmasi penghapusan
        int confirmDelete = JOptionPane.showConfirmDialog(this,
                "PERINGATAN: Semua data Anda (hewan, riwayat pemesanan, chat, dll.) akan dihapus secara permanen. Lanjutkan?",
                "Konfirmasi Hapus Akun", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (confirmDelete != JOptionPane.YES_OPTION) {
            return;
        }

        // Hapus pemilik (cascade akan menghapus data terkait)
        boolean deleted = pemilikController.deletePemilik(pemilik.getIdPemilik());
        if (deleted) {
            JOptionPane.showMessageDialog(this, "Akun berhasil dihapus.");
            // Kembali ke halaman login
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new LoginView();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal menghapus akun. Silakan coba lagi.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusHewan() {
        int selectedRow = tableHewan.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih hewan yang akan dihapus.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPet = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus hewan ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = petsController.deletePets(idPet);
            if (success) {
                JOptionPane.showMessageDialog(this, "Hewan berhasil dihapus.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadHewanData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus hewan.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Inner class dialog untuk tambah/edit hewan
    private class HewanDialog extends JDialog {
        private boolean saved = false;
        private Pets pet;
        private JTextField txtNamaPet, txtRas, txtTanggalLahir, txtUsia, txtBerat;
        private JComboBox<String> cbJenisKelamin, cbJenisHewan, cbSterilisasi;
        private JTextArea txtAlergi;
        
        public HewanDialog(JFrame parent, Pets pet) {
            super(parent, pet == null ? "Tambah Hewan" : "Edit Hewan", true);
            this.pet = pet;
            initComponents();
            if (pet != null) {
                loadDataToForm();
            }
            setSize(450, 550);
            setLocationRelativeTo(parent);
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
            
            mainPanel.add(new JLabel("Nama Hewan:"), gbc);
            txtNamaPet = new JTextField(15);
            gbc.gridx = 1;
            mainPanel.add(txtNamaPet, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Jenis Kelamin:"), gbc);
            cbJenisKelamin = new JComboBox<>(new String[]{"Jantan", "Betina", "Tidak Diketahui"});
            gbc.gridx = 1;
            mainPanel.add(cbJenisKelamin, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Jenis Hewan:"), gbc);
            String[] jenisHewan = {"sapi","kambing","kerbau","ayam","kucing","kelinci","anjing",
                                   "hamster","burung","ikan","musang","kura-kura","landak","babi","kuda","domba","monyet"};
            cbJenisHewan = new JComboBox<>(jenisHewan);
            gbc.gridx = 1;
            mainPanel.add(cbJenisHewan, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Ras (opsional):"), gbc);
            txtRas = new JTextField(15);
            gbc.gridx = 1;
            mainPanel.add(txtRas, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Tanggal Lahir (YYYY-MM-DD):"), gbc);
            txtTanggalLahir = new JTextField(10);
            gbc.gridx = 1;
            mainPanel.add(txtTanggalLahir, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Usia (tahun, opsional):"), gbc);
            txtUsia = new JTextField(5);
            gbc.gridx = 1;
            mainPanel.add(txtUsia, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Berat (kg, opsional):"), gbc);
            txtBerat = new JTextField(8);
            gbc.gridx = 1;
            mainPanel.add(txtBerat, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Sterilisasi:"), gbc);
            cbSterilisasi = new JComboBox<>(new String[]{"belum", "sudah"});
            gbc.gridx = 1;
            mainPanel.add(cbSterilisasi, gbc);
            
            gbc.gridy++;
            gbc.gridx = 0;
            mainPanel.add(new JLabel("Alergi (pisahkan dengan koma):"), gbc);
            txtAlergi = new JTextArea(3, 15);
            txtAlergi.setLineWrap(true);
            JScrollPane scrollAlergi = new JScrollPane(txtAlergi);
            gbc.gridx = 1;
            mainPanel.add(scrollAlergi, gbc);
            
            JButton btnSave = new JButton("Simpan");
            btnSave.setBackground(new Color(0, 102, 204));
            btnSave.setForeground(Color.DARK_GRAY); // diubah dari putih
            btnSave.addActionListener(e -> save());
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            mainPanel.add(btnSave, gbc);
            
            add(mainPanel, BorderLayout.CENTER);
        }
        
        private void loadDataToForm() {
            txtNamaPet.setText(pet.getNamaPet());
            cbJenisKelamin.setSelectedItem(pet.getJenisKelamin().substring(0,1).toUpperCase() + pet.getJenisKelamin().substring(1));
            cbJenisHewan.setSelectedItem(pet.getJenisHewan());
            txtRas.setText(pet.getRas() != null ? pet.getRas() : "");
            if (pet.getTanggalLahir() != null) txtTanggalLahir.setText(pet.getTanggalLahir().toString());
            if (pet.getUsia() != null) txtUsia.setText(String.valueOf(pet.getUsia()));
            if (pet.getBerat() != null) txtBerat.setText(pet.getBerat().toString());
            cbSterilisasi.setSelectedItem(pet.getSterilisasi());
            
            AlergiController alergiController = new AlergiController();
            String alergiStr = alergiController.getAlergiNamesByPet(pet.getIdPet());
            txtAlergi.setText(alergiStr);
        }
        
        private void save() {
            String nama = txtNamaPet.getText().trim();
            if (nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama hewan wajib diisi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (pet == null) pet = new Pets();
            pet.setNamaPet(nama);
            
            String jenisKelamin = ((String) cbJenisKelamin.getSelectedItem()).toLowerCase();
            if (!jenisKelamin.equals("jantan") && !jenisKelamin.equals("betina") && !jenisKelamin.equals("tidak_diketahui")) {
                jenisKelamin = "tidak_diketahui";
            }
            pet.setJenisKelamin(jenisKelamin);

            pet.setJenisHewan((String) cbJenisHewan.getSelectedItem());
            pet.setRas(txtRas.getText().trim());
            
            String tglStr = txtTanggalLahir.getText().trim();
            if (!tglStr.isEmpty()) {
                try {
                    LocalDate tgl = LocalDate.parse(tglStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    pet.setTanggalLahir(Date.valueOf(tgl));
                } catch (DateTimeParseException e) {
                    pet.setTanggalLahir(null);
                }
            } else {
                pet.setTanggalLahir(null);
            }
            
            String usiaStr = txtUsia.getText().trim();
            if (!usiaStr.isEmpty()) {
                try {
                    pet.setUsia(Integer.parseInt(usiaStr));
                } catch (NumberFormatException e) {
                    pet.setUsia(null);
                }
            } else {
                pet.setUsia(null);
            }
            
            String beratStr = txtBerat.getText().trim();
            if (!beratStr.isEmpty()) {
                try {
                    pet.setBerat(new BigDecimal(beratStr));
                } catch (NumberFormatException e) {
                    pet.setBerat(null);
                }
            } else {
                pet.setBerat(null);
            }
            
            pet.setSterilisasi((String) cbSterilisasi.getSelectedItem());
            pet.setIdPemilik(pemilik.getIdPemilik());
            
            boolean success;
            if (pet.getIdPet() == 0) {
                success = petsController.insertPets(pet);
            } else {
                success = petsController.updatePets(pet);
            }
            
            if (!success) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data hewan.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String alergiText = txtAlergi.getText().trim();
            List<String> alergiList = new ArrayList<>();
            if (!alergiText.isEmpty()) {
                // Split berdasarkan koma, bersihkan spasi
                String[] parts = alergiText.split(",");
                for (String part : parts) {
                    String trimmed = part.trim();
                    if (!trimmed.isEmpty()) {
                        alergiList.add(trimmed);
                    }
                }
            }
            AlergiController alergiController = new AlergiController();
            boolean alergiSuccess = alergiController.updateAlergiForPet(pet.getIdPet(), alergiList, pemilik.getIdPemilik(), "pemilik");
            if (!alergiSuccess) {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan alergi.", "Peringatan", JOptionPane.WARNING_MESSAGE);
            } else {
                saved = true;
                JOptionPane.showMessageDialog(this, "Data hewan dan alergi berhasil disimpan.", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            }
        }
        
        public boolean isSaved() { 
            return saved; 
        }
    }
}