/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view;

import abpaw.controller.AuthController;
import abpaw.controller.PetsController;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Pets;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class RegisterView extends JFrame {
    private AuthController authController;
    private PetsController petsController;

    // Panel untuk CardLayout
    private JPanel mainContainer;
    private CardLayout cardLayout;
    private JPanel panelDataPemilik;
    private JPanel panelDataHewan;

    // Field untuk data pemilik
    private JTextField txtNamaDepan, txtNamaBelakang, txtUsername, txtNoHp, txtEmail, txtAlamat;
    private JPasswordField txtPassword, txtConfirmPassword;

    // Panel dinamis untuk hewan
    private JPanel petContainer;
    private List<PetPanel> petPanels;
    private JButton btnAddPet;

    // Label pesan
    private JLabel lblMessagePemilik, lblMessageHewan;

    public RegisterView() {
        authController = new AuthController();
        petsController = new PetsController();
        petPanels = new ArrayList<>();
        initComponents();
        // Minimal satu panel hewan saat pertama kali panel hewan ditampilkan
        // (tapi kita buat nanti saat pindah ke panel hewan)
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initComponents() {
        setTitle("Daftar Akun Baru - AB Paw");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(550, 650);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(Color.WHITE);

        // ---- Panel 1: Data Pemilik ----
        panelDataPemilik = createPemilikPanel();
        mainContainer.add(panelDataPemilik, "pemilik");

        // ---- Panel 2: Data Hewan ----
        panelDataHewan = createHewanPanel();
        mainContainer.add(panelDataHewan, "hewan");

        add(mainContainer, BorderLayout.CENTER);
    }

    private JPanel createPemilikPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);

        // Data Pemilik
        JPanel pemilikPanel = new JPanel(new GridBagLayout());
        pemilikPanel.setBorder(BorderFactory.createTitledBorder("Data Pemilik"));
        pemilikPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        pemilikPanel.add(new JLabel("Nama Depan*:"), gbc);
        txtNamaDepan = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtNamaDepan, gbc);

        gbc.gridy = 1; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Nama Belakang (opsional):"), gbc);
        txtNamaBelakang = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtNamaBelakang, gbc);

        gbc.gridy = 2; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Username*:"), gbc);
        txtUsername = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtUsername, gbc);

        gbc.gridy = 3; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Password*:"), gbc);
        txtPassword = new JPasswordField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtPassword, gbc);

        gbc.gridy = 4; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Konfirmasi Password*:"), gbc);
        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtConfirmPassword, gbc);

        gbc.gridy = 5; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("No. HP* (hanya angka):"), gbc);
        txtNoHp = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtNoHp, gbc);

        gbc.gridy = 6; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Email*:"), gbc);
        txtEmail = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtEmail, gbc);

        gbc.gridy = 7; gbc.gridx = 0;
        pemilikPanel.add(new JLabel("Alamat*:"), gbc);
        txtAlamat = new JTextField(15);
        gbc.gridx = 1;
        pemilikPanel.add(txtAlamat, gbc);

        formPanel.add(pemilikPanel);
        formPanel.add(Box.createVerticalStrut(20));

        // Tombol aksi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> {
            dispose();
            new LandingPageView();
        });
        JButton btnSelanjutnya = new JButton("Selanjutnya");
        btnSelanjutnya.setBackground(new Color(0, 102, 204));
        btnSelanjutnya.setForeground(Color.WHITE);
        btnSelanjutnya.addActionListener(e -> {
            if (validatePemilikData()) {
                // Pindah ke panel hewan
                // Pastikan panel hewan memiliki minimal 1 form hewan
                if (petPanels.isEmpty()) {
                    addPetPanel(); // tambah satu panel hewan jika belum ada
                }
                cardLayout.show(mainContainer, "hewan");
            }
        });
        actionPanel.add(btnKembali);
        actionPanel.add(btnSelanjutnya);
        formPanel.add(actionPanel);

        lblMessagePemilik = new JLabel(" ");
        lblMessagePemilik.setForeground(Color.RED);
        formPanel.add(lblMessagePemilik);

        JScrollPane scroll = new JScrollPane(formPanel);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBorder(null);
        panel.add(scroll, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createHewanPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JPanel petMasterPanel = new JPanel(new BorderLayout());
        petMasterPanel.setBorder(BorderFactory.createTitledBorder("Data Hewan Peliharaan"));
        petMasterPanel.setBackground(Color.WHITE);

        petContainer = new JPanel();
        petContainer.setLayout(new BoxLayout(petContainer, BoxLayout.Y_AXIS));
        petContainer.setBackground(Color.WHITE);
        JScrollPane scrollPet = new JScrollPane(petContainer);
        scrollPet.setBorder(null);
        scrollPet.getVerticalScrollBar().setUnitIncrement(16);
        petMasterPanel.add(scrollPet, BorderLayout.CENTER);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAddPet = new JButton("+ Tambah Hewan Lain");
        btnAddPet.addActionListener(e -> addPetPanel());
        btnPanel.add(btnAddPet);
        petMasterPanel.add(btnPanel, BorderLayout.SOUTH);

        contentPanel.add(petMasterPanel, BorderLayout.CENTER);

        // Tombol aksi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        JButton btnKembali = new JButton("Kembali");
        btnKembali.addActionListener(e -> cardLayout.show(mainContainer, "pemilik"));
        JButton btnDaftar = new JButton("Daftar");
        btnDaftar.setBackground(new Color(34, 139, 34));
        btnDaftar.setForeground(Color.WHITE);
        btnDaftar.addActionListener(e -> register());
        actionPanel.add(btnKembali);
        actionPanel.add(btnDaftar);
        contentPanel.add(actionPanel, BorderLayout.SOUTH);

        lblMessageHewan = new JLabel(" ");
        lblMessageHewan.setForeground(Color.RED);
        contentPanel.add(lblMessageHewan, BorderLayout.NORTH);

        panel.add(contentPanel, BorderLayout.CENTER);
        return panel;
    }

    private boolean validatePemilikData() {
        String namaDepan = txtNamaDepan.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirm = new String(txtConfirmPassword.getPassword());
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();
        String alamat = txtAlamat.getText().trim();

        if (namaDepan.isEmpty()) {
            lblMessagePemilik.setText("Nama Depan wajib diisi!");
            return false;
        }
        if (username.isEmpty()) {
            lblMessagePemilik.setText("Username wajib diisi!");
            return false;
        }
        if (password.isEmpty()) {
            lblMessagePemilik.setText("Password wajib diisi!");
            return false;
        }
        if (confirm.isEmpty()) {
            lblMessagePemilik.setText("Konfirmasi Password wajib diisi!");
            return false;
        }
        if (!password.equals(confirm)) {
            lblMessagePemilik.setText("Password dan konfirmasi password tidak cocok!");
            return false;
        }
        if (password.length() < 6) {
            lblMessagePemilik.setText("Password minimal 6 karakter!");
            return false;
        }
        if (noHp.isEmpty()) {
            lblMessagePemilik.setText("No HP wajib diisi!");
            return false;
        }
        if (!Pattern.matches("\\d+", noHp)) {
            lblMessagePemilik.setText("No HP harus berupa angka!");
            return false;
        }
        if (email.isEmpty()) {
            lblMessagePemilik.setText("Email wajib diisi!");
            return false;
        }
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            lblMessagePemilik.setText("Format email tidak valid! Contoh: nama@domain.com");
            return false;
        }
        if (alamat.isEmpty()) {
            lblMessagePemilik.setText("Alamat wajib diisi!");
            return false;
        }
        return true;
    }

    private void addPetPanel() {
        PetPanel panel = new PetPanel(petPanels.size() + 1);
        petPanels.add(panel);
        petContainer.add(panel);
        updateRemoveButtons();
        petContainer.revalidate();
        petContainer.repaint();
    }

    private void removePetPanel(PetPanel panel) {
        petContainer.remove(panel);
        petPanels.remove(panel);
        for (int i = 0; i < petPanels.size(); i++) {
            petPanels.get(i).setTitle(i + 1);
        }
        updateRemoveButtons();
        petContainer.revalidate();
        petContainer.repaint();
    }

    private void updateRemoveButtons() {
        boolean enableRemove = petPanels.size() > 1;
        for (PetPanel p : petPanels) {
            p.btnRemove.setEnabled(enableRemove);
        }
    }

    private void register() {
        lblMessageHewan.setText(" ");

        // Ambil data pemilik dari panel pertama (tetap tersimpan)
        String namaDepan = txtNamaDepan.getText().trim();
        String namaBelakang = txtNamaBelakang.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String noHp = txtNoHp.getText().trim();
        String email = txtEmail.getText().trim();
        String alamat = txtAlamat.getText().trim();

        // Validasi hewan
        if (petPanels.isEmpty()) {
            lblMessageHewan.setText("Harap tambahkan minimal 1 hewan peliharaan!");
            return;
        }
        List<Pets> petsList = new ArrayList<>();
        for (PetPanel p : petPanels) {
            if (!p.isPetValid()) {
                lblMessageHewan.setText("Data hewan " + p.getTitle() + " belum lengkap (Nama dan Jenis Hewan wajib)!");
                return;
            }
            Pets pet = p.getPets();
            if (pet == null) continue;
            petsList.add(pet);
        }
        if (petsList.isEmpty()) {
            lblMessageHewan.setText("Minimal satu hewan harus diisi dengan lengkap!");
            return;
        }

        // Daftarkan pemilik
        String namaLengkap = namaDepan + (namaBelakang.isEmpty() ? "" : " " + namaBelakang);
        boolean pemilikSuccess = authController.registerPemilik(namaLengkap, username, password, noHp, email, alamat);
        if (!pemilikSuccess) {
            lblMessageHewan.setText("Username atau email sudah terdaftar!");
            return;
        }

        Pemilik newPemilik = authController.getPemilikByUsername(username);
        if (newPemilik == null) {
            lblMessageHewan.setText("Gagal mengambil data pemilik setelah registrasi.");
            return;
        }

        boolean allPetsSuccess = true;
        for (Pets pet : petsList) {
            pet.setIdPemilik(newPemilik.getIdPemilik());
            if (!petsController.insertPets(pet)) {
                allPetsSuccess = false;
                break;
            }
        }

        if (allPetsSuccess) {
            JOptionPane.showMessageDialog(this, "Pendaftaran berhasil! Silakan login.");
            dispose();
            new LoginView();
        } else {
            JOptionPane.showMessageDialog(this, "Pendaftaran pemilik berhasil, tetapi gagal menyimpan data hewan.");
            new LoginView();
        }
    }

    // ----- Inner class PetPanel (sama seperti sebelumnya, hanya jenis hewan tetap JComboBox) -----
    class PetPanel extends JPanel {
        private int nomor;
        private JTextField txtNamaPet;
        private JComboBox<String> cbJenisKelamin;
        private JComboBox<String> cbJenisHewan;
        private JTextField txtRas;
        private JTextField txtTanggalLahir;
        private JTextField txtUsia;
        private JTextField txtBerat;
        private JComboBox<String> cbSterilisasi;
        private JButton btnRemove;

        public PetPanel(int nomor) {
            this.nomor = nomor;
            setLayout(new GridBagLayout());
            setBorder(BorderFactory.createTitledBorder("Hewan ke-" + nomor));
            setBackground(Color.WHITE);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;
            gbc.gridy = 0;

            add(new JLabel("Nama Hewan*:"), gbc);
            txtNamaPet = new JTextField(15);
            gbc.gridx = 1;
            add(txtNamaPet, gbc);

            gbc.gridy = 1; gbc.gridx = 0;
            add(new JLabel("Jenis Kelamin:"), gbc);
            cbJenisKelamin = new JComboBox<>(new String[]{"Jantan", "Betina", "Tidak Diketahui"});
            cbJenisKelamin.setSelectedIndex(2);
            gbc.gridx = 1;
            add(cbJenisKelamin, gbc);

            gbc.gridy = 2; gbc.gridx = 0;
            add(new JLabel("Jenis Hewan*:"), gbc);
            String[] jenisHewan = {"sapi","kambing","kerbau","ayam","kucing","kelinci","anjing",
                                   "hamster","burung","ikan","musang","kura-kura","landak","babi","kuda","domba","monyet"};
            cbJenisHewan = new JComboBox<>(jenisHewan);
            gbc.gridx = 1;
            add(cbJenisHewan, gbc);

            gbc.gridy = 3; gbc.gridx = 0;
            add(new JLabel("Ras (opsional):"), gbc);
            txtRas = new JTextField(15);
            gbc.gridx = 1;
            add(txtRas, gbc);

            gbc.gridy = 4; gbc.gridx = 0;
            add(new JLabel("Tanggal Lahir (opsional, YYYY-MM-DD):"), gbc);
            txtTanggalLahir = new JTextField(10);
            gbc.gridx = 1;
            add(txtTanggalLahir, gbc);

            gbc.gridy = 5; gbc.gridx = 0;
            add(new JLabel("Usia (tahun, opsional):"), gbc);
            txtUsia = new JTextField(5);
            gbc.gridx = 1;
            add(txtUsia, gbc);

            gbc.gridy = 6; gbc.gridx = 0;
            add(new JLabel("Berat (kg, opsional):"), gbc);
            txtBerat = new JTextField(8);
            gbc.gridx = 1;
            add(txtBerat, gbc);

            gbc.gridy = 7; gbc.gridx = 0;
            add(new JLabel("Sterilisasi (opsional):"), gbc);
            cbSterilisasi = new JComboBox<>(new String[]{"belum", "sudah"});
            gbc.gridx = 1;
            add(cbSterilisasi, gbc);

            btnRemove = new JButton("Hapus Hewan Ini");
            btnRemove.addActionListener(e -> removePetPanel(this));
            gbc.gridy = 8; gbc.gridx = 0;
            gbc.gridwidth = 2;
            add(btnRemove, gbc);
        }

        public void setTitle(int nomorBaru) {
            this.nomor = nomorBaru;
            setBorder(BorderFactory.createTitledBorder("Hewan ke-" + nomorBaru));
        }

        public String getTitle() {
            return "ke-" + nomor;
        }

        public boolean isPetValid() {
            if (txtNamaPet == null) return false;
            String nama = txtNamaPet.getText().trim();
            return !nama.isEmpty();
        }

        public Pets getPets() {
            String nama = txtNamaPet.getText().trim();
            if (nama.isEmpty()) return null;
            Pets p = new Pets();
            p.setNamaPet(nama);
            p.setJenisKelamin(((String) cbJenisKelamin.getSelectedItem()).toLowerCase());
            p.setJenisHewan((String) cbJenisHewan.getSelectedItem());
            p.setRas(txtRas.getText().trim());

            String tglStr = txtTanggalLahir.getText().trim();
            if (!tglStr.isEmpty()) {
                try {
                    LocalDate tgl = LocalDate.parse(tglStr, DateTimeFormatter.ISO_LOCAL_DATE);
                    p.setTanggalLahir(Date.valueOf(tgl));
                } catch (DateTimeParseException e) { p.setTanggalLahir(null); }
            } else { p.setTanggalLahir(null); }

            String usiaStr = txtUsia.getText().trim();
            if (!usiaStr.isEmpty()) {
                try { p.setUsia(Integer.parseInt(usiaStr)); } catch (NumberFormatException e) { p.setUsia(null); }
            } else { p.setUsia(null); }

            String beratStr = txtBerat.getText().trim();
            if (!beratStr.isEmpty()) {
                try { p.setBerat(new BigDecimal(beratStr)); } catch (NumberFormatException e) { p.setBerat(null); }
            } else { p.setBerat(null); }

            p.setSterilisasi((String) cbSterilisasi.getSelectedItem());
            return p;
        }
    }
}