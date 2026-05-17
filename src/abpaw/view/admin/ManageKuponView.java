/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.admin;

import abpaw.controller.KuponController;
import abpaw.model.entity.Kupon;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

public class ManageKuponView extends JPanel {
    private KuponController kuponController;
    private JTable table;
    private DefaultTableModel model;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh;

    public ManageKuponView() {
        kuponController = new KuponController();
        initComponents();
        loadData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);  // Background putih

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        btnRefresh = new JButton("Refresh");
        btnRefresh.addActionListener(e -> loadData());
        topPanel.add(btnRefresh);
        add(topPanel, BorderLayout.NORTH);

        String[] cols = {"ID", "Kode", "Deskripsi", "Diskon (%)", "Maks Diskon", "Min Belanja", "Berlaku Hingga", "Aktif"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(model);
        table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        styleTable();
        
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        
        btnTambah = new JButton("Tambah Kupon");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        
        styleButton(btnTambah, new Color(34, 139, 34));  // Hijau
        styleButton(btnEdit, new Color(0, 102, 204));    // Biru
        styleButton(btnHapus, new Color(220, 20, 60));   // Merah
        styleButton(btnRefresh, new Color(255, 140, 0)); // Orange
        
        btnTambah.addActionListener(e -> openFormDialog(null));
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                Kupon k = kuponController.getKuponById(id);
                if (k != null) openFormDialog(k);
            } else {
                JOptionPane.showMessageDialog(this, "Pilih kupon yang akan diedit.");
            }
        });
        btnHapus.addActionListener(e -> hapusKupon());
        
        actionPanel.add(btnTambah);
        actionPanel.add(btnEdit);
        actionPanel.add(btnHapus);
        add(actionPanel, BorderLayout.SOUTH);
    }

    private void styleTable() {
        // Header tabel
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        // Custom renderer header (matikan efek hover)
        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(0, 102, 204));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Arial", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Font isi tabel
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);

        // Warna selang-seling (zebra stripe)
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255)); // Biru muda
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else {
                    c.setBackground(new Color(0, 102, 204));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }

    private void styleButton(JButton btn, Color bgColor) {
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 12));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(bgColor);
            }
        });
    }

    private void loadData() {
        model.setRowCount(0);
        List<Kupon> list = kuponController.getAllKupon();
        for (Kupon k : list) {
            model.addRow(new Object[]{
                k.getIdKupon(),
                k.getKode(),
                k.getDeskripsi(),
                k.getDiskonPersen(),
                "Rp " + k.getDiskonMaks(),
                "Rp " + k.getMinimalPembelian(),
                k.getBerlakuHingga() != null ? k.getBerlakuHingga() : "Selamanya",
                k.isAktif() ? "Ya" : "Tidak"
            });
        }
    }

    private void openFormDialog(Kupon existing) {
        KuponFormDialog dialog = new KuponFormDialog((JFrame) SwingUtilities.getWindowAncestor(this), existing);
        dialog.setVisible(true);
        if (dialog.isSaved()) loadData();
    }

    private void hapusKupon() {
        int row = table.getSelectedRow();
        if (row == -1) return;
        int id = (int) model.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin hapus kupon ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = kuponController.deleteKupon(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Kupon berhasil dihapus.");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus kupon.");
            }
        }
    }

    // Inner class KuponFormDialog (sama seperti kode Anda, tidak perlu diubah)
    private class KuponFormDialog extends JDialog {
        private boolean saved = false;
        private Kupon kupon;
        private JTextField txtKode, txtDeskripsi, txtDiskonPersen, txtDiskonMaks, txtMinPembelian, txtBerlakuHingga;
        private JCheckBox chkAktif;

        public KuponFormDialog(JFrame parent, Kupon kupon) {
            super(parent, kupon == null ? "Tambah Kupon" : "Edit Kupon", true);
            this.kupon = kupon;
            initComponents();
            if (kupon != null) loadDataToForm();
            setSize(400, 350);
            setLocationRelativeTo(parent);
        }

        private void initComponents() {
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(15, 15, 15, 15));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.gridx = 0;

            gbc.gridy = 0; panel.add(new JLabel("Kode Kupon:"), gbc);
            txtKode = new JTextField(15);
            gbc.gridx = 1; panel.add(txtKode, gbc);

            gbc.gridy = 1; gbc.gridx = 0; panel.add(new JLabel("Deskripsi:"), gbc);
            txtDeskripsi = new JTextField(15);
            gbc.gridx = 1; panel.add(txtDeskripsi, gbc);

            gbc.gridy = 2; gbc.gridx = 0; panel.add(new JLabel("Diskon (%):"), gbc);
            txtDiskonPersen = new JTextField(15);
            gbc.gridx = 1; panel.add(txtDiskonPersen, gbc);

            gbc.gridy = 3; gbc.gridx = 0; panel.add(new JLabel("Maksimum Diskon (Rp):"), gbc);
            txtDiskonMaks = new JTextField(15);
            gbc.gridx = 1; panel.add(txtDiskonMaks, gbc);

            gbc.gridy = 4; gbc.gridx = 0; panel.add(new JLabel("Minimal Pembelian (Rp):"), gbc);
            txtMinPembelian = new JTextField(15);
            gbc.gridx = 1; panel.add(txtMinPembelian, gbc);

            gbc.gridy = 5; gbc.gridx = 0; panel.add(new JLabel("Berlaku Hingga (YYYY-MM-DD):"), gbc);
            txtBerlakuHingga = new JTextField(15);
            gbc.gridx = 1; panel.add(txtBerlakuHingga, gbc);

            gbc.gridy = 6; gbc.gridx = 0; panel.add(new JLabel("Aktif:"), gbc);
            chkAktif = new JCheckBox();
            gbc.gridx = 1; panel.add(chkAktif, gbc);

            JButton btnSave = new JButton("Simpan");
            btnSave.addActionListener(e -> save());
            gbc.gridy = 7; gbc.gridx = 0; gbc.gridwidth = 2;
            panel.add(btnSave, gbc);
            add(panel);
        }

        private void loadDataToForm() {
            txtKode.setText(kupon.getKode());
            txtDeskripsi.setText(kupon.getDeskripsi());
            txtDiskonPersen.setText(kupon.getDiskonPersen().toString());
            txtDiskonMaks.setText(kupon.getDiskonMaks().toString());
            txtMinPembelian.setText(kupon.getMinimalPembelian().toString());
            if (kupon.getBerlakuHingga() != null) txtBerlakuHingga.setText(kupon.getBerlakuHingga().toString());
            chkAktif.setSelected(kupon.isAktif());
        }

        private void save() {
            String kode = txtKode.getText().trim();
            String deskripsi = txtDeskripsi.getText().trim();
            BigDecimal diskonPersen, diskonMaks, minPembelian;
            try {
                diskonPersen = new BigDecimal(txtDiskonPersen.getText().trim());
                diskonMaks = new BigDecimal(txtDiskonMaks.getText().trim());
                minPembelian = new BigDecimal(txtMinPembelian.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Diskon dan nominal harus angka.");
                return;
            }
            Date berlakuHingga = null;
            String tglStr = txtBerlakuHingga.getText().trim();
            if (!tglStr.isEmpty()) {
                try {
                    berlakuHingga = Date.valueOf(tglStr);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, "Format tanggal salah (YYYY-MM-DD)");
                    return;
                }
            }
            boolean aktif = chkAktif.isSelected();

            boolean success;
            if (kupon == null) {
                Kupon newKupon = new Kupon();
                newKupon.setKode(kode);
                newKupon.setDeskripsi(deskripsi);
                newKupon.setDiskonPersen(diskonPersen);
                newKupon.setDiskonMaks(diskonMaks);
                newKupon.setMinimalPembelian(minPembelian);
                newKupon.setBerlakuHingga(berlakuHingga);
                newKupon.setAktif(aktif);
                success = kuponController.insertKupon(newKupon);
            } else {
                kupon.setKode(kode);
                kupon.setDeskripsi(deskripsi);
                kupon.setDiskonPersen(diskonPersen);
                kupon.setDiskonMaks(diskonMaks);
                kupon.setMinimalPembelian(minPembelian);
                kupon.setBerlakuHingga(berlakuHingga);
                kupon.setAktif(aktif);
                success = kuponController.updateKupon(kupon);
            }
            if (success) {
                saved = true;
                JOptionPane.showMessageDialog(this, "Kupon berhasil disimpan.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menyimpan kupon. Periksa kode unik.");
            }
        }
        public boolean isSaved() { return saved; }
    }
}