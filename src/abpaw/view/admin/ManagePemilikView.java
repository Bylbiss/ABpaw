/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.PemilikController;
import abpaw.model.entity.Pemilik;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ManagePemilikView extends JPanel {
    private PemilikController pemilikController;
    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;
    private JButton btnEdit, btnHapus, btnRefresh;

    public ManagePemilikView() {
        pemilikController = new PemilikController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        searchField = new JTextField(20);
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        searchField.addActionListener(e -> loadData());
        searchPanel.add(searchField);
        searchPanel.add(btnRefresh);
        add(searchPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Nama", "Username", "Email", "No HP", "Alamat"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnEdit = new JButton("Edit Pemilik");
        btnHapus = new JButton("Hapus Pemilik");
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                Pemilik p = pemilikController.getPemilikById(id);
                if (p != null) openFormDialog(p);
            } else JOptionPane.showMessageDialog(this, "Pilih pemilik yang akan diedit.");
        });
        btnHapus.addActionListener(e -> hapusPemilik());
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        String keyword = searchField.getText().trim();
        List<Pemilik> list;
        if (keyword.isEmpty()) list = pemilikController.getAllPemilik();
        else list = pemilikController.searchPemilik(keyword);
        for (Pemilik p : list) {
            model.addRow(new Object[]{
                p.getIdPemilik(),
                p.getNamaPemilik(),
                p.getUsername(),
                p.getEmail(),
                p.getNoHp(),
                p.getAlamat()
            });
        }
    }

    private void openFormDialog(Pemilik pemilik) {
        PemilikFormDialog dialog = new PemilikFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), pemilik);
        dialog.setVisible(true);
        if (dialog.isSaved()) loadData();
    }

    private void hapusPemilik() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Hapus pemilik beserta semua hewannya? Data tidak dapat dikembalikan.", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = pemilikController.deletePemilik(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Pemilik berhasil dihapus.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus pemilik.");
            }
        }
    }

    private class PemilikFormDialog extends JDialog {
        private boolean saved = false;
        private Pemilik pemilik;
        private JTextField txtNama, txtUsername, txtPassword, txtEmail, txtNoHp, txtAlamat;
        private JButton btnSave;

        public PemilikFormDialog(JFrame parent, Pemilik pemilik) {
            super(parent, "Edit Pemilik", true);
            this.pemilik = pemilik;
            initComponents();
            loadDataToForm();
            setSize(400, 350);
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            gbc.gridy = 0; gbc.gridx = 0; panel.add(new JLabel("Nama Lengkap:"), gbc);
            txtNama = new JTextField(15); gbc.gridx = 1; panel.add(txtNama, gbc);
            gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Username:"), gbc);
            txtUsername = new JTextField(15); gbc.gridx = 1; panel.add(txtUsername, gbc);
            gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Password Baru (kosongkan jika tidak diubah):"), gbc);
            txtPassword = new JPasswordField(15); gbc.gridx = 1; panel.add(txtPassword, gbc);
            gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Email:"), gbc);
            txtEmail = new JTextField(15); gbc.gridx = 1; panel.add(txtEmail, gbc);
            gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("No HP:"), gbc);
            txtNoHp = new JTextField(15); gbc.gridx = 1; panel.add(txtNoHp, gbc);
            gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Alamat:"), gbc);
            txtAlamat = new JTextField(15); gbc.gridx = 1; panel.add(txtAlamat, gbc);

            btnSave = new JButton("Simpan");
            btnSave.addActionListener(e -> save());
            gbc.gridy = 6; gbc.gridx = 0; gbc.gridwidth = 2;
            panel.add(btnSave, gbc);

            add(panel);
        }

        private void loadDataToForm() {
            txtNama.setText(pemilik.getNamaPemilik());
            txtUsername.setText(pemilik.getUsername());
            txtEmail.setText(pemilik.getEmail());
            txtNoHp.setText(pemilik.getNoHp());
            txtAlamat.setText(pemilik.getAlamat());
        }

        private void save() {
            String nama = txtNama.getText().trim();
            String username = txtUsername.getText().trim();
            String password = new String(((JPasswordField) txtPassword).getPassword());
            String email = txtEmail.getText().trim();
            String noHp = txtNoHp.getText().trim();
            String alamat = txtAlamat.getText().trim();

            if (nama.isEmpty() || username.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Nama, username, dan email wajib diisi.");
                return;
            }

            pemilik.setNamaPemilik(nama);
            pemilik.setUsername(username);
            if (!password.isEmpty()) pemilik.setPassword(password);
            pemilik.setEmail(email);
            pemilik.setNoHp(noHp);
            pemilik.setAlamat(alamat);

            boolean success = pemilikController.updatePemilik(pemilik);
            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Data pemilik berhasil diupdate.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan perubahan.");
            }
        }
        public boolean isSaved() { return saved; }
    }
}