/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.DokterController;
import abpaw.model.entity.Dokter;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.util.List;

public class ManageDokterView extends JPanel {
    private DokterController dokterController;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh;

    public ManageDokterView() {
        dokterController = new DokterController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        searchField = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        searchField.addActionListener(e -> loadData());
        searchPanel.add(searchField);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.NORTH);

        // Tabel
        String[] cols = {"ID", "Username", "Nama Lengkap", "Email", "Spesialisasi", "Biaya", "No HP"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        // Tombol aksi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnTambah = new JButton("Tambah Dokter");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnTambah.addActionListener(e -> openFormDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                Dokter d = dokterController.getDokterById(id);
                if (d != null) openFormDialog(d);
            } else JOptionPane.showMessageDialog(this, "Pilih dokter yang akan diedit.");
        });
        btnHapus.addActionListener(e -> hapusDokter());
        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        String keyword = searchField.getText().trim();
        List<Dokter> list;
        if (keyword.isEmpty()) {
            list = dokterController.getAllDokter();
        } else {
            list = dokterController.searchDokter(keyword);
        }
        for (Dokter d : list) {
            model.addRow(new Object[]{
                d.getIdDokter(),
                d.getUsername(),
                d.getNamaLengkap(),
                d.getEmail(),
                d.getSpesialisasi(),
                "Rp " + d.getBiayaKonsultasi(),
                d.getNoHp()
            });
        }
    }

    private void openFormDialog(Dokter existing) {
        DokterFormDialog dialog = new DokterFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), existing);
        dialog.setVisible(true);
        if (dialog.isSaved()) loadData();
    }

    private void hapusDokter() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus dokter ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dokterController.deleteDokter(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Dokter berhasil dihapus.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus dokter.");
            }
        }
    }

    // Inner class dialog form tambah/edit dokter
    private class DokterFormDialog extends JDialog {
        private boolean saved = false;
        private Dokter dokter;
        private JTextField txtNamaDepan, txtNamaBelakang, txtUsername, txtPassword, txtEmail, txtAlamat, txtNoHp, txtSpesialisasi, txtBiaya, txtSpesiesHewan;
        private JButton btnSave;

        public DokterFormDialog(JFrame parent, Dokter dokter) {
            super(parent, dokter == null ? "Tambah Dokter" : "Edit Dokter", true);
            this.dokter = dokter;
            initComponents();
            if (dokter != null) loadDataToForm();
            setSize(450, 550);
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;

            gbc.gridy = 0; panel.add(new JLabel("Nama Depan:"), gbc);
            txtNamaDepan = new JTextField(15);
            gbc.gridx = 1; panel.add(txtNamaDepan, gbc);
            gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Nama Belakang:"), gbc);
            txtNamaBelakang = new JTextField(15);
            gbc.gridx = 1; panel.add(txtNamaBelakang, gbc);
            gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Username:"), gbc);
            txtUsername = new JTextField(15);
            gbc.gridx = 1; panel.add(txtUsername, gbc);
            gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Password:"), gbc);
            txtPassword = new JPasswordField(15);
            gbc.gridx = 1; panel.add(txtPassword, gbc);
            gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
            txtEmail = new JTextField(15);
            gbc.gridx = 1; panel.add(txtEmail, gbc);
            gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Alamat:"), gbc);
            txtAlamat = new JTextField(15);
            gbc.gridx = 1; panel.add(txtAlamat, gbc);
            gbc.gridy = 6; gbc.gridx = 0; panel.add(new JLabel("No HP:"), gbc);
            txtNoHp = new JTextField(15);
            gbc.gridx = 1; panel.add(txtNoHp, gbc);
            gbc.gridy = 7; gbc.gridx = 0; panel.add(new JLabel("Spesialisasi:"), gbc);
            txtSpesialisasi = new JTextField(15);
            gbc.gridx = 1; panel.add(txtSpesialisasi, gbc);
            gbc.gridy = 8; gbc.gridx = 0; panel.add(new JLabel("Biaya Konsultasi:"), gbc);
            txtBiaya = new JTextField(15);
            gbc.gridx = 1; panel.add(txtBiaya, gbc);
            gbc.gridy = 9; gbc.gridx = 0; panel.add(new JLabel("Spesies Hewan (pisah koma):"), gbc);
            txtSpesiesHewan = new JTextField(15);
            gbc.gridx = 1; panel.add(txtSpesiesHewan, gbc);

            btnSave = new JButton("Simpan");
            btnSave.addActionListener(e -> save());
            gbc.gridy = 10; gbc.gridx = 0; gbc.gridwidth = 2;
            panel.add(btnSave, gbc);

            add(panel);
        }

        private void loadDataToForm() {
            txtNamaDepan.setText(dokter.getNamaDepan() != null ? dokter.getNamaDepan() : "");
            txtNamaBelakang.setText(dokter.getNamaBelakang() != null ? dokter.getNamaBelakang() : "");
            txtUsername.setText(dokter.getUsername());
            txtEmail.setText(dokter.getEmail());
            txtAlamat.setText(dokter.getAlamat());
            txtNoHp.setText(dokter.getNoHp());
            txtSpesialisasi.setText(dokter.getSpesialisasi());
            txtBiaya.setText(dokter.getBiayaKonsultasi().toString());
            txtSpesiesHewan.setText(dokter.getSpesiesHewan());
            txtPassword.setEnabled(false);
        }

        private void save() {
            String namaDepan = txtNamaDepan.getText().trim();
            String namaBelakang = txtNamaBelakang.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(((JPasswordField) txtPassword).getPassword());
            String email = txtEmail.getText().trim();
            String alamat = txtAlamat.getText().trim();
            String noHp = txtNoHp.getText().trim();
            String spesialisasi = txtSpesialisasi.getText().trim();
            BigDecimal biaya;
            try {
                biaya = new BigDecimal(txtBiaya.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Biaya harus angka.");
                return;
            }
            String spesiesHewan = txtSpesiesHewan.getText().trim();

            if (username.isEmpty() || email.isEmpty() || biaya.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Username, email, dan biaya harus diisi dengan benar.");
                return;
            }

            boolean success;
            if (dokter == null) {
                // Tambah baru
                if (password.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Password harus diisi untuk dokter baru.");
                    return;
                }
                Dokter newDokter = new Dokter();
                newDokter.setNamaDepan(namaDepan);
                newDokter.setNamaBelakang(namaBelakang);
                newDokter.setUsername(username);
                newDokter.setPassword(password);
                newDokter.setEmail(email);
                newDokter.setAlamat(alamat);
                newDokter.setNoHp(noHp);
                newDokter.setSpesialisasi(spesialisasi);
                newDokter.setBiayaKonsultasi(biaya);
                newDokter.setSpesiesHewan(spesiesHewan);
                success = dokterController.insertDokter(newDokter);
            } else {
                // Update
                dokter.setNamaDepan(namaDepan);
                dokter.setNamaBelakang(namaBelakang);
                dokter.setUsername(username);
                dokter.setEmail(email);
                dokter.setAlamat(alamat);
                dokter.setNoHp(noHp);
                dokter.setSpesialisasi(spesialisasi);
                dokter.setBiayaKonsultasi(biaya);
                dokter.setSpesiesHewan(spesiesHewan);
                // Jika password diisi, update password
                if (!password.isEmpty()) dokter.setPassword(password);
                success = dokterController.updateDokter(dokter);
            }
            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Data dokter berhasil disimpan.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan data.");
            }
        }

        public boolean isSaved() { return saved; }
    }
}