/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package abpaw.view.admin;

import abpaw.controller.LaporanController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class LaporanView extends JPanel {
    private LaporanController laporanController;
    private JTable table;
    private DefaultTableModel model;
    private JComboBox<String> cbJenisLaporan;
    private JTextField txtStartDate, txtEndDate;
    private JButton btnGenerate;
    private JLabel totalLabel;

    public LaporanView() {
        laporanController = new LaporanController();
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Filter Laporan"));
        cbJenisLaporan = new JComboBox<>(new String[]{"Pemesanan Online", "Pemesanan Offline", "Semua Transaksi", "Pendapatan per Dokter"});
        txtStartDate = new JTextField(LocalDate.now().minusDays(30).toString(), 10);
        txtEndDate = new JTextField(LocalDate.now().toString(), 10);
        btnGenerate = new JButton("Tampilkan");
        btnGenerate.addActionListener(e -> generateLaporan());
        filterPanel.add(new JLabel("Jenis:"));
        filterPanel.add(cbJenisLaporan);
        filterPanel.add(new JLabel("Dari Tgl:"));
        filterPanel.add(txtStartDate);
        filterPanel.add(new JLabel("Sampai Tgl:"));
        filterPanel.add(txtEndDate);
        filterPanel.add(btnGenerate);
        add(filterPanel, BorderLayout.NORTH);

        model = new DefaultTableModel();
        table = new JTable(model);
        JScrollPane sp = new JScrollPane(table);
        add(sp, BorderLayout.CENTER);
        styleTable();
        
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.add(new JLabel("Total: "));
        totalLabel = new JLabel("Rp 0");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        footerPanel.add(totalLabel);
        add(footerPanel, BorderLayout.SOUTH);
    }

    private void generateLaporan() {
        LocalDate start, end;
        try {
            start = LocalDate.parse(txtStartDate.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            end = LocalDate.parse(txtEndDate.getText().trim(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Format tanggal harus YYYY-MM-DD");
            return;
        }
        String jenis = (String) cbJenisLaporan.getSelectedItem();
        BigDecimal total = BigDecimal.ZERO;
        
        switch (jenis) {
            case "Pemesanan Online":
                List<Object[]> onlineList = laporanController.getLaporanOnlineWithNames(start, end);
                displayOnlineWithNames(onlineList);
                total = onlineList.stream()
                        .map(row -> (BigDecimal) row[6])
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            case "Pemesanan Offline":
                List<Object[]> offlineList = laporanController.getLaporanOfflineWithNames(start, end);
                displayOfflineWithNames(offlineList);
                total = BigDecimal.ZERO;
                break;
            case "Semua Transaksi":
                List<Object[]> allOnline = laporanController.getLaporanOnlineWithNames(start, end);
                List<Object[]> allOffline = laporanController.getLaporanOfflineWithNames(start, end);
                displayAllWithNames(allOnline, allOffline);
                total = allOnline.stream()
                        .map(row -> (BigDecimal) row[6])
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                break;
            case "Pendapatan per Dokter":
                List<Object[]> pendapatan = laporanController.getPendapatanPerDokterWithNames(start, end);
                displayPendapatanPerDokterWithNames(pendapatan);
                break;
        }

        if (totalLabel != null && !jenis.equals("Pendapatan per Dokter")) {
            totalLabel.setText("Rp " + total.toString());
        }
    }

    private void displayOnlineWithNames(List<Object[]> list) {
        list.sort((a, b) -> {
            LocalDate tglA = (LocalDate) a[5];
            LocalDate tglB = (LocalDate) b[5];
            int cmp = tglA.compareTo(tglB);
            if (cmp != 0) return cmp;
            return Integer.compare((int) a[0], (int) b[0]);
        });
        
        String[] cols = {"Kode", "Pemilik", "Dokter", "Hewan", "Tanggal", "Total", "Status"};
        model.setDataVector(new Object[0][0], cols);
        for (Object[] row : list) {
            String kode = row[1] != null ? row[1].toString() : "Tidak ada";
            String hewan = row[4] != null ? row[4].toString() : "Tidak ada";
            String status = row[7] != null ? row[7].toString() : "Tidak diketahui";
            model.addRow(new Object[]{
                kode, row[2], row[3], hewan, row[5], "Rp " + row[6], status
            });
        }
    }

    private void displayOfflineWithNames(List<Object[]> list) {
        list.sort((a, b) -> {
            LocalDate tglA = (LocalDate) a[5];
            LocalDate tglB = (LocalDate) b[5];
            int cmp = tglA.compareTo(tglB);
            if (cmp != 0) return cmp;
            return Integer.compare((int) a[0], (int) b[0]);
        });
        
        String[] cols = {"Nomor Antrean", "Pemilik", "Dokter", "Hewan", "Tanggal", "Status"};
        model.setDataVector(new Object[0][0], cols);
        for (Object[] row : list) {
            String nomor = row[1] != null ? row[1].toString() : "Tidak ada";
            String hewan = row[4] != null ? row[4].toString() : "Tidak ada";
            String status = row[6] != null ? row[6].toString() : "Tidak diketahui";
            model.addRow(new Object[]{
                nomor, row[2], row[3], hewan, row[5], status
            });
        }
    }

    private void displayAllWithNames(List<Object[]> online, List<Object[]> offline) {
        List<Object[]> all = new ArrayList<>();
        
        for (Object[] row : online) {
            Object[] newRow = new Object[7];
            newRow[0] = "Online";
            newRow[1] = row[1];
            newRow[2] = row[2];
            newRow[3] = row[3];
            newRow[4] = row[4];
            newRow[5] = row[5];
            newRow[6] = "Rp " + row[6];
            all.add(newRow);
        }
        
        for (Object[] row : offline) {
            Object[] newRow = new Object[7];
            newRow[0] = "Offline";
            newRow[1] = row[1];
            newRow[2] = row[2];
            newRow[3] = row[3];
            newRow[4] = row[4];
            newRow[5] = row[5];
            newRow[6] = row[6];
            all.add(newRow);
        }
        
        all.sort((a, b) -> {
            LocalDate tglA = (LocalDate) a[5];
            LocalDate tglB = (LocalDate) b[5];
            return tglA.compareTo(tglB);
        });
        
        String[] cols = {"Tipe", "Kode/Nomor", "Pemilik", "Dokter", "Hewan", "Tanggal", "Total/Status"};
        model.setDataVector(new Object[0][0], cols);
        for (Object[] row : all) {
            model.addRow(row);
        }
    }

    private void displayPendapatanPerDokterWithNames(List<Object[]> data) {
        String[] cols = {"ID Dokter", "Nama Dokter", "Jumlah Konsultasi", "Total Pendapatan"};
        model.setDataVector(new Object[0][0], cols);
        for (Object[] row : data) {
            model.addRow(new Object[]{
                row[0], row[1], row[2], "Rp " + row[3]
            });
        }
    }

    private void styleTable() {
        table.getTableHeader().setBackground(new Color(0, 102, 204));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.getTableHeader().setPreferredSize(new Dimension(0, 35));

        table.getTableHeader().setDefaultRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, false, false, row, column);
                c.setBackground(new Color(0, 102, 204));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Arial", Font.BOLD, 14));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.setRowHeight(30);
        table.setSelectionBackground(new Color(0, 102, 204));
        table.setSelectionForeground(Color.WHITE);
        table.setFocusable(true);
        table.putClientProperty("JTable.autoStartsEdit", false);

        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (hasFocus && !isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(240, 248, 255));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(0, 102, 204));
                    c.setForeground(Color.WHITE);
                }
                return c;
            }
        });
    }
}