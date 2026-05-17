/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.dokter;

import abpaw.controller.ChatController;
import abpaw.controller.PemesananController;
import abpaw.controller.PemilikController;
import abpaw.controller.PetsController;
import abpaw.model.entity.Chat;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Pemilik;
import abpaw.model.entity.Pets;
import abpaw.model.entity.PemesananOnline;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.stream.Collectors;

public class ChatPasienView extends JPanel {
    private Dokter dokter;
    private ChatController chatController;
    private PemilikController pemilikController;
    private PetsController petsController;
    private PemesananController pemesananController;
    private JList<PasienItem> pasienList;
    private DefaultListModel<PasienItem> listModel;
    private JPanel chatPanel;          // Ganti dari JTextArea menjadi JPanel
    private JTextField messageField;
    private JButton sendButton;
    private Pemilik selectedPemilik;
    private Timer refreshTimer;
    private JLabel lblUnread;
    private JScrollPane scrollChat;
    private int lastMessageCount = 0;

    public ChatPasienView(Dokter dokter) {
        this.dokter = dokter;
        this.chatController = new ChatController();
        this.pemesananController = new PemesananController();
        this.pemilikController = new PemilikController();   
        this.petsController = new PetsController();  
        initComponents();
        loadPasienList();
        startAutoRefresh();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel kiri: daftar pasien
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Pasien yang pernah chat"));

        listModel = new DefaultListModel<>();
        pasienList = new JList<>(listModel);
        pasienList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pasienList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                loadChatHistory();
            }
        });
        JScrollPane scrollPasien = new JScrollPane(pasienList);
        leftPanel.add(scrollPasien, BorderLayout.CENTER);

        // Panel kanan: area chat
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Percakapan"));

        // Panel chat dengan BoxLayout untuk bubble-bubble
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        chatPanel.add(Box.createVerticalGlue());
        scrollChat = new JScrollPane(chatPanel);
        scrollChat.setBorder(null);
        scrollChat.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(scrollChat, BorderLayout.CENTER);

        // Panel input
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        messageField = new JTextField();
        sendButton = new JButton("Kirim");
        sendButton.setBackground(new Color(0, 102, 204));
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this::sendMessage);
        messageField.addActionListener(this::sendMessage);
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Badge unread
        lblUnread = new JLabel("🔔");
        leftPanel.add(lblUnread, BorderLayout.NORTH);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
    }

    private void loadPasienList() {
        int selectedId = -1;
        PasienItem selected = pasienList.getSelectedValue();
        if (selected != null && selected.idPemilik != -1) {
            selectedId = selected.idPemilik;
        }
        
        listModel.clear();
        
        // Ambil semua chat dari dokter ini, grup berdasarkan id_pemilik
        List<Chat> allChats = chatController.getChatsForDokter(dokter.getIdDokter());
        Map<Integer, String> pemilikMap = new HashMap<>();
        
        for (Chat chat : allChats) {
            if (chat.getIdPemilik() != null) {
                int idPemilik = chat.getIdPemilik();
                if (!pemilikMap.containsKey(idPemilik)) {
                    Pemilik p = pemilikController.getPemilikById(idPemilik);
                    if (p != null) {  // hanya tambahkan jika pemilik benar-benar ada
                        pemilikMap.put(idPemilik, p.getNamaPemilik());
                    } else {
                        // Jika tidak ditemukan, abaikan chat ini (bisa juga dihapus)
                        System.out.println("Chat dengan id_pemilik " + idPemilik + " tidak valid, diabaikan.");
                    }
                }
            }
        }

        // Tambahkan ke list model
        for (Map.Entry<Integer, String> entry : pemilikMap.entrySet()) {
            listModel.addElement(new PasienItem(entry.getKey(), entry.getValue()));
        }

        if (listModel.isEmpty()) {
            listModel.addElement(new PasienItem(-1, "Belum ada pasien"));
        }
        // Kembalikan seleksi sebelumnya jika masih ada
        if (selectedId != -1) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).idPemilik == selectedId) {
                    pasienList.setSelectedIndex(i);
                    break;
                }
            }
        }

        int unread = chatController.getUnreadCountForDokter(dokter.getIdDokter());
        lblUnread.setText(unread > 0 ? "🔔 " + unread + " pesan baru" : "🔔 Tidak ada pesan baru");
    }

    private void loadChatHistory() {
        PasienItem selectedItem = pasienList.getSelectedValue();
        if (selectedItem == null || selectedItem.idPemilik == -1) {
            chatPanel.removeAll();
            chatPanel.add(Box.createVerticalGlue());
            chatPanel.revalidate();
            chatPanel.repaint();
            SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum()));
            selectedPemilik = null;
            return;
        }

        try {
            int idPemilik = selectedItem.idPemilik;
            selectedPemilik = pemilikController.getPemilikById(idPemilik);
            if (selectedPemilik == null) {
                selectedPemilik = new Pemilik();
                selectedPemilik.setIdPemilik(idPemilik);
            }

            List<Chat> chats = chatController.getChatBetween(dokter.getIdDokter(), idPemilik);
            
            // Hitung jumlah pesan saat ini
            int currentCount = chats.size();

            // Jika pasien berbeda, reset lastMessageCount
            if (selectedPemilik == null || selectedPemilik.getIdPemilik() != idPemilik) {
                lastMessageCount = 0;
            }

            // Jika ada pesan baru (currentCount > lastMessageCount) dan lastMessageCount > 0 (berarti benar-benar ada pesan tambahan)
            if (currentCount > lastMessageCount && lastMessageCount > 0) {
                // Mainkan bunyi notifikasi
                Toolkit.getDefaultToolkit().beep();
            }

            // Perbarui lastMessageCount dengan jumlah pesan terbaru
            lastMessageCount = currentCount;
            
            chatPanel.removeAll();

            if (chats.isEmpty()) {
                showPlaceholder("Belum ada percakapan dengan pasien ini.");
            } else {
                for (Chat c : chats) {
                    boolean isFromDokter = (c.getIdDokter() != null && c.getIdPemilik() == null);
                    if (c.getIdDokter() != null && c.getIdPemilik() != null) {
                        // Data korup, abaikan atau anggap dari pasien
                        isFromDokter = false;
                    }
                addMessageBubble(c.getPesan(), c.getWaktu(), isFromDokter);
                }
                chatPanel.add(Box.createVerticalGlue());
            }

            chatPanel.revalidate();
            chatPanel.repaint();
            // Scroll ke bawah
            SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum()));

            // Tandai semua pesan dari pasien sebagai read
            for (Chat c : chats) {
                if (c.getIdPemilik() != null && "unread".equals(c.getStatusBaca())) {
                    chatController.markAsRead(c.getIdChat());
                }
            }
            // Update unread count
            int unread = chatController.getUnreadCountForDokter(dokter.getIdDokter());
            lblUnread.setText(unread > 0 ? "🔔 " + unread + " pesan baru" : "🔔 Tidak ada pesan baru");

        } catch (Exception ex) {
            ex.printStackTrace();
            showPlaceholder("Terjadi kesalahan: " + ex.getMessage());
            selectedPemilik = null;
        }
    }

    // Tambahkan method helper untuk menampilkan placeholder
    private void showPlaceholder(String message) {
        chatPanel.removeAll();
        // Gunakan panel dengan GridBagLayout agar label terpusat
        JPanel placeholderPanel = new JPanel(new GridBagLayout());
        placeholderPanel.setBackground(Color.WHITE);
        placeholderPanel.setOpaque(true);
        JLabel placeholder = new JLabel(message);
        placeholder.setForeground(Color.GRAY);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        placeholderPanel.add(placeholder);
        // Agar panel memenuhi lebar dan tinggi
        placeholderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        chatPanel.add(placeholderPanel);
        chatPanel.revalidate();
        chatPanel.repaint();
        // Scroll tetap di atas (opsional)
        SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(0));
    }

    private void addMessageBubble(String message, Timestamp waktu, boolean isFromDokter) {
        JPanel bubbleContainer = new JPanel();
        bubbleContainer.setLayout(new BorderLayout());
        bubbleContainer.setOpaque(false);
        bubbleContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        bubbleContainer.setBorder(new EmptyBorder(5, 10, 5, 10));

        String timeStr = (waktu != null) ? waktu.toString().substring(11, 16) : "00:00";

        // Bubble panel
        JPanel bubblePanel = new JPanel(new BorderLayout());
        bubblePanel.setOpaque(false);
        Color bgColor = isFromDokter ? new Color(0, 102, 204) : new Color(240, 240, 240);
        int radius = 15;
        bubblePanel.setBorder(new RoundedBorder(radius, bgColor));

        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(bgColor);
        textArea.setForeground(isFromDokter ? Color.WHITE : Color.BLACK);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        textArea.setOpaque(false);

        int maxWidth = 300;
        textArea.setSize(maxWidth, Integer.MAX_VALUE);
        int prefHeight = textArea.getPreferredSize().height;
        textArea.setPreferredSize(new Dimension(maxWidth, prefHeight));
        bubblePanel.add(textArea, BorderLayout.CENTER);

        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setHorizontalAlignment(isFromDokter ? SwingConstants.RIGHT : SwingConstants.LEFT);

        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        if (isFromDokter) {
            wrapper.add(bubblePanel, BorderLayout.EAST);
            wrapper.add(timeLabel, BorderLayout.SOUTH);
            bubbleContainer.add(wrapper, BorderLayout.EAST);
        } else {
            wrapper.add(bubblePanel, BorderLayout.WEST);
            wrapper.add(timeLabel, BorderLayout.SOUTH);
            bubbleContainer.add(wrapper, BorderLayout.WEST);
        }

        chatPanel.add(bubbleContainer);
        chatPanel.add(Box.createVerticalStrut(5));
    }

    private void sendMessage(ActionEvent e) {
        String pesan = messageField.getText().trim();
        if (pesan.isEmpty() || selectedPemilik == null) return;
        boolean success = chatController.sendMessageFromDokter(dokter.getIdDokter(), selectedPemilik.getIdPemilik(), pesan);
        if (success) {
            messageField.setText("");
            loadChatHistory();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengirim pesan.");
        }
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // Simpan siapa yang sedang dipilih saat ini
                    PasienItem selectedBefore = pasienList.getSelectedValue();
                    int selectedId = (selectedBefore != null) ? selectedBefore.idPemilik : -1;

                    // Refresh daftar pasien (tanpa mengganggu seleksi)
                    loadPasienList();

                    // Jika sebelumnya ada pasien terpilih, cari ulang item yang sama
                    PasienItem newSelected = null;
                    for (int i = 0; i < listModel.size(); i++) {
                        PasienItem item = listModel.get(i);
                        if (item.idPemilik == selectedId) {
                            newSelected = item;
                            break;
                        }
                    }
                    if (newSelected != null) {
                        pasienList.setSelectedValue(newSelected, true);
                        // Jika pasien yang sama masih terpilih, reload chat history-nya
                        loadChatHistory();
                    } else if (selectedId == -1 && listModel.size() > 0 && listModel.get(0).idPemilik != -1) {
                        // Jika sebelumnya tidak ada seleksi, pilih pasien pertama (opsional)
                        pasienList.setSelectedIndex(0);
                        loadChatHistory();
                    }
                });
            }
        }, 3000, 3000);
    }
    
    // Inner class untuk menyimpan id dan teks tampilan
    private class PasienItem {
        int idPemilik;
        String displayText;
        PasienItem(int id, String text) {
            this.idPemilik = id;
            this.displayText = text;
        }
        @Override
        public String toString() {
            return displayText;
        }
    }

    @Override
    public void removeNotify() {
        if (refreshTimer != null) refreshTimer.cancel();
        super.removeNotify();
    }

    // Inner class RoundedBorder 
    private static class RoundedBorder implements Border {
        private int radius;
        private Color color;

        public RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.fillRoundRect(x, y, width, height, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius / 2, radius / 2, radius / 2, radius / 2);
        }

        @Override
        public boolean isBorderOpaque() {
            return true;
        }
    }
}
