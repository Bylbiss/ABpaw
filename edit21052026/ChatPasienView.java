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
import abpaw.model.entity.PemesananOffline;
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
import javax.swing.border.TitledBorder;

public class ChatPasienView extends JPanel {

    private Dokter dokter;
    private ChatController chatController;
    private PemilikController pemilikController;
    private PetsController petsController;
    private PemesananController pemesananController;
    private JList<PasienItem> pasienList;
    private DefaultListModel<PasienItem> listModel;
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private Pemilik selectedPemilik;
    private Timer refreshTimer;
    private JLabel lblUnread;
    private JScrollPane scrollChat;
    private int lastMessageCount = 0;

    private final Color GREEN_COLOR = new Color(0, 128, 0);
    private final Color GREEN_DARK = new Color(0, 100, 0);
    private final Color GREEN_LIGHT = new Color(200, 230, 200);

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

        // LEFT PANEL
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setPreferredSize(new Dimension(250, 0));
        leftPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(GREEN_COLOR), "Pasien yang pernah chat", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Arial", Font.BOLD, 12), GREEN_COLOR));

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

        lblUnread = new JLabel("🔔");
        JPanel leftHeaderPanel = new JPanel(new BorderLayout());
        leftHeaderPanel.setOpaque(false);
        leftHeaderPanel.add(lblUnread, BorderLayout.WEST);

        JButton btnRefreshList = new JButton("↻");
        btnRefreshList.setPreferredSize(new Dimension(30, 25));
        btnRefreshList.setBackground(GREEN_COLOR);
        btnRefreshList.setForeground(Color.WHITE);
        btnRefreshList.setFocusPainted(false);
        btnRefreshList.setToolTipText("Refresh daftar pasien");
        btnRefreshList.addActionListener(e -> {
            PasienItem selected = pasienList.getSelectedValue();
            int selectedId = (selected != null) ? selected.idPemilik : -1;
            loadPasienList();
            if (selectedId != -1) {
                for (int i = 0; i < listModel.size(); i++) {
                    if (listModel.get(i).idPemilik == selectedId) {
                        pasienList.setSelectedIndex(i);
                        break;
                    }
                }
            }
        });
        leftHeaderPanel.add(btnRefreshList, BorderLayout.EAST);
        leftPanel.add(leftHeaderPanel, BorderLayout.NORTH);

        // ================== RIGHT PANEL ==================
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createLineBorder(GREEN_COLOR, 1));

        // Header dengan judul dan tombol tutup
        JPanel chatHeader = new JPanel(new BorderLayout());
        chatHeader.setOpaque(false);
        chatHeader.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel chatTitle = new JLabel("Percakapan");
        chatTitle.setFont(new Font("Arial", Font.BOLD, 12));
        chatTitle.setForeground(GREEN_COLOR);
        chatHeader.add(chatTitle, BorderLayout.WEST);

        JButton closeChatBtn = new JButton("✖");
        closeChatBtn.setPreferredSize(new Dimension(25, 25));
        closeChatBtn.setBackground(GREEN_COLOR);
        closeChatBtn.setForeground(Color.WHITE);
        closeChatBtn.setFocusPainted(false);
        closeChatBtn.setToolTipText("Tutup chat");
        closeChatBtn.addActionListener(e -> {
            pasienList.clearSelection();
            selectedPemilik = null;
            chatPanel.removeAll();
            chatPanel.add(Box.createVerticalGlue());
            chatPanel.revalidate();
            chatPanel.repaint();
            scrollChat.getVerticalScrollBar().setValue(0);
        });
        chatHeader.add(closeChatBtn, BorderLayout.EAST);
        rightPanel.add(chatHeader, BorderLayout.NORTH);

        // Panel chat (untuk menampilkan bubble)
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        chatPanel.add(Box.createVerticalGlue());
        scrollChat = new JScrollPane(chatPanel);
        scrollChat.setBorder(null);
        scrollChat.getVerticalScrollBar().setUnitIncrement(16);
        rightPanel.add(scrollChat, BorderLayout.CENTER);

        // Panel input bawah
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        messageField = new JTextField();
        sendButton = new JButton("Kirim");
        sendButton.setBackground(GREEN_COLOR);
        sendButton.setForeground(Color.WHITE);
        sendButton.addActionListener(this::sendMessage);
        messageField.addActionListener(this::sendMessage);
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        rightPanel.add(bottomPanel, BorderLayout.SOUTH);

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

        List<Chat> allChats = chatController.getChatsForDokter(dokter.getIdDokter());
        Map<Integer, String> pemilikMap = new HashMap<>();
        Map<Integer, String> hewanInfoMap = new HashMap<>();

        for (Chat chat : allChats) {
            if (chat.getIdPemilik() != null) {
                int idPemilik = chat.getIdPemilik();
                if (!pemilikMap.containsKey(idPemilik)) {
                    Pemilik p = pemilikController.getPemilikById(idPemilik);
                    if (p != null) {
                        pemilikMap.put(idPemilik, p.getNamaPemilik());

                        // Ambil hewan dari pemesanan ONLINE terakhir
                        String hewanInfo = "Tanpa Hewan";
                        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByPemilik(idPemilik);
                        if (!onlineList.isEmpty()) {
                            // Ambil yang paling baru berdasarkan waktu pemesanan
                            PemesananOnline lastOnline = onlineList.stream()
                                    .max((a, b) -> a.getWaktuPemesanan().compareTo(b.getWaktuPemesanan()))
                                    .orElse(null);
                            if (lastOnline != null) {
                                Pets pet = petsController.getPetsById(lastOnline.getIdPet());
                                if (pet != null) {
                                    hewanInfo = pet.getNamaPet() + " (" + pet.getJenisHewan() + ")";
                                }
                            }
                        }
                        hewanInfoMap.put(idPemilik, hewanInfo);
                    }
                }
            }
        }

        for (Map.Entry<Integer, String> entry : pemilikMap.entrySet()) {
            int id = entry.getKey();
            String namaPemilik = entry.getValue();
            String hewanInfo = hewanInfoMap.getOrDefault(id, "Tanpa Hewan");
            String display = namaPemilik + " - " + hewanInfo;
            listModel.addElement(new PasienItem(id, display));
        }

        if (listModel.isEmpty()) {
            listModel.addElement(new PasienItem(-1, "Belum ada pasien"));
        }

        updateUnreadLabel();
        
        // Kembalikan seleksi jika ada
        if (selectedId != -1) {
            for (int i = 0; i < listModel.size(); i++) {
                if (listModel.get(i).idPemilik == selectedId) {
                    pasienList.setSelectedIndex(i);
                    break;
                }
            }
        }
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

            // Hitung jumlah pesan 
            int currentCount = chats.size();

            // Jika pasien berbeda, reset lastMessageCount
            if (selectedPemilik == null || selectedPemilik.getIdPemilik() != idPemilik) {
                lastMessageCount = 0;
            }

            // Jika ada pesan baru 
            if (currentCount > lastMessageCount && lastMessageCount > 0) {
                Toolkit.getDefaultToolkit().beep();
            }

            // Perbarui lastMessageCount dengan jumlah pesan terbaru
            lastMessageCount = currentCount;

            chatPanel.removeAll();

            if (chats.isEmpty()) {
                showPlaceholder("Belum ada percakapan dengan pasien ini.");
            } else {
                for (Chat c : chats) {
                    boolean isFromMe = "dokter".equals(c.getPengirim());
                    addMessageBubble(c.getPesan(), c.getWaktu(), isFromMe);
                }
                chatPanel.add(Box.createVerticalGlue());
            }

            chatPanel.revalidate();
            chatPanel.repaint();

            SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(scrollChat.getVerticalScrollBar().getMaximum()));

            for (Chat c : chats) {
                if (c.getIdPemilik() != null && "unread".equals(c.getStatusBaca())) {
                    chatController.markAsRead(c.getIdChat());
                }
            }

            updateUnreadLabel();

        } catch (Exception ex) {
            ex.printStackTrace();
            showPlaceholder("Terjadi kesalahan: " + ex.getMessage());
            selectedPemilik = null;
        }
    }

    private void showPlaceholder(String message) {
        chatPanel.removeAll();

        JPanel placeholderPanel = new JPanel(new GridBagLayout());
        placeholderPanel.setBackground(Color.WHITE);
        placeholderPanel.setOpaque(true);

        JLabel placeholder = new JLabel(message);
        placeholder.setForeground(Color.GRAY);
        placeholder.setFont(new Font("Arial", Font.ITALIC, 14));
        placeholderPanel.add(placeholder);
        placeholderPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        chatPanel.add(placeholderPanel);
        chatPanel.revalidate();
        chatPanel.repaint();

        SwingUtilities.invokeLater(() -> scrollChat.getVerticalScrollBar().setValue(0));
    }

    private void addMessageBubble(String message, Timestamp waktu, boolean isFromDokter) {
        JPanel bubbleContainer = new JPanel();
        bubbleContainer.setLayout(new BorderLayout());
        bubbleContainer.setOpaque(false);
        bubbleContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        bubbleContainer.setBorder(new EmptyBorder(5, 10, 5, 10));

        String timeStr = (waktu != null) ? waktu.toString().substring(11, 16) : "00:00";

        JPanel bubblePanel = new JPanel(new BorderLayout());
        bubblePanel.setOpaque(false);
        Color bgColor = isFromDokter ? GREEN_COLOR : new Color(240, 240, 240);
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
        if (pesan.isEmpty() || selectedPemilik == null) {
            return;
        }

        if (!isPemesananAktif(selectedPemilik.getIdPemilik())) {
            JOptionPane.showMessageDialog(this,
                    "Tidak dapat mengirim pesan. Pasien ini sudah menyelesaikan konsultasi.\nChat hanya tersedia untuk pemesanan yang masih berlangsung.",
                    "Chat Tidak Tersedia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean success = chatController.sendMessageFromDokter(dokter.getIdDokter(), selectedPemilik.getIdPemilik(), pesan);
        if (success) {
            messageField.setText("");
            loadChatHistory();
        } else {
            JOptionPane.showMessageDialog(this, "Gagal mengirim pesan.");
        }
    }

    private class PasienItem {

        int idPemilik;
        String displayText;

        PasienItem(int id, String display) {
            this.idPemilik = id;
            this.displayText = display;
        }

        @Override
        public String toString() {
            return displayText;
        }
    }

    @Override
    public void removeNotify() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        super.removeNotify();
    }

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

    private boolean isPemesananAktif(int idPemilik) {
        List<PemesananOnline> onlineList = pemesananController.getPemesananOnlineByDokter(dokter.getIdDokter());
        for (PemesananOnline po : onlineList) {
            if (po.getIdPemilik() == idPemilik) {
                String status = po.getStatus();
                if (!"selesai".equalsIgnoreCase(status) && !"batal".equalsIgnoreCase(status)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void startAutoRefresh() {
        refreshTimer = new Timer(true);
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    // Simpan id pasien yang sedang dipilih
                    PasienItem selected = pasienList.getSelectedValue();
                    int selectedId = (selected != null) ? selected.idPemilik : -1;

                    // Refresh daftar pasien (tanpa mengubah seleksi)
                    loadPasienList();

                    // Kembalikan seleksi jika pasien masih ada di daftar
                    if (selectedId != -1) {
                        for (int i = 0; i < listModel.size(); i++) {
                            if (listModel.get(i).idPemilik == selectedId) {
                                pasienList.setSelectedIndex(i);
                                break;
                            }
                        }
                    }

                    // Load chat history hanya jika masih ada pasien terpilih
                    if (pasienList.getSelectedValue() != null && pasienList.getSelectedValue().idPemilik != -1) {
                        loadChatHistory();
                    }
                });
            }
        }, 3000, 3000);
    }

    private void updateUnreadLabel() {
        List<Chat> allChats = chatController.getChatsForDokter(dokter.getIdDokter());
        Map<Integer, Boolean> validPemilik = new HashMap<>();
        for (Chat chat : allChats) {
            if (chat.getIdPemilik() != null && !validPemilik.containsKey(chat.getIdPemilik())) {
                Pemilik p = pemilikController.getPemilikById(chat.getIdPemilik());
                validPemilik.put(chat.getIdPemilik(), p != null);
            }
        }
        int totalUnread = 0;
        for (Chat chat : allChats) {
            if (chat.getIdPemilik() != null && validPemilik.getOrDefault(chat.getIdPemilik(), false) && "unread".equals(chat.getStatusBaca())) {
                totalUnread++;
            }
        }
        lblUnread.setText(totalUnread > 0 ? "🔔 " + totalUnread + " pesan baru" : "🔔 Tidak ada pesan baru");
    }
}
