package abpaw.view.pemilik;

import abpaw.controller.ChatController;
import abpaw.model.entity.Chat;
import abpaw.model.entity.Dokter;
import abpaw.model.entity.Pemilik;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Timestamp;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatDetailView extends JFrame {
    private Pemilik pemilik;
    private Dokter dokter;
    private ChatController chatController;
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private Timer refreshTimer;
    private JScrollPane scrollPane;

    public ChatDetailView(Pemilik pemilik, Dokter dokter) {
        this.pemilik = pemilik;
        this.dokter = dokter;
        this.chatController = new ChatController();
        initComponents();
        setLocationRelativeTo(null);
        setVisible(true);
        loadChatHistory();
        startAutoRefresh();
    }

    private void initComponents() {
        setTitle("Chat dengan Dr. " + dokter.getNamaLengkap());
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(0, 102, 204));
        topPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        JLabel nameLabel = new JLabel("Dr. " + dokter.getNamaLengkap());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        nameLabel.setForeground(Color.WHITE);
        topPanel.add(nameLabel, BorderLayout.WEST);
        add(topPanel, BorderLayout.NORTH);

        // Panel chat dengan scroll
        chatPanel = new JPanel();
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBackground(Color.WHITE);
        scrollPane = new JScrollPane(chatPanel);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Chat"));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        // Panel input
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 5));
        bottomPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        messageField = new JTextField();
        sendButton = new JButton("Kirim");
        sendButton.setBackground(new Color(0, 102, 204));
        sendButton.setForeground(Color.WHITE);
        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(this::sendMessage);
        messageField.addActionListener(this::sendMessage);
    }

    private void loadChatHistory() {
        List<Chat> chats = chatController.getChatBetween(dokter.getIdDokter(), pemilik.getIdPemilik());
        chatPanel.removeAll();
    for (Chat c : chats) {
        boolean isFromMe = (c.getIdPemilik() != null && c.getIdDokter() == null);
        addMessageBubble(c.getPesan(), c.getWaktu(), isFromMe);
    }
        chatPanel.add(Box.createVerticalGlue());
        chatPanel.revalidate();
        chatPanel.repaint();
        SwingUtilities.invokeLater(() -> scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum()));
    }

    private void addMessageBubble(String message, Timestamp waktu, boolean isFromMe) {
        JPanel bubbleContainer = new JPanel();
        bubbleContainer.setLayout(new BorderLayout());
        bubbleContainer.setOpaque(false);
        bubbleContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        bubbleContainer.setBorder(new EmptyBorder(5, 10, 5, 10));

        String timeStr = waktu.toString().substring(11, 16);

        // Panel bubble dengan rounded border
        JPanel bubblePanel = new JPanel(new BorderLayout());
        bubblePanel.setOpaque(false);

        Color bgColor = isFromMe ? new Color(0, 102, 204) : new Color(240, 240, 240);
        int radius = 15;
        bubblePanel.setBorder(new RoundedBorder(radius, bgColor));

        // Teks bubble
        JTextArea textArea = new JTextArea(message);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBackground(bgColor);
        textArea.setForeground(isFromMe ? Color.WHITE : Color.BLACK);
        textArea.setFont(new Font("Arial", Font.PLAIN, 12));
        textArea.setBorder(new EmptyBorder(8, 12, 8, 12));
        textArea.setOpaque(false);

        int maxWidth = 300;
        textArea.setSize(maxWidth, Integer.MAX_VALUE);
        int prefHeight = textArea.getPreferredSize().height;
        textArea.setPreferredSize(new Dimension(maxWidth, prefHeight));

        bubblePanel.add(textArea, BorderLayout.CENTER);

        // Label waktu
        JLabel timeLabel = new JLabel(timeStr);
        timeLabel.setFont(new Font("Arial", Font.PLAIN, 10));
        timeLabel.setForeground(Color.GRAY);
        timeLabel.setHorizontalAlignment(isFromMe ? SwingConstants.RIGHT : SwingConstants.LEFT);

        // Wrapper untuk mengatur posisi
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setOpaque(false);
        if (isFromMe) {
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
        if (pesan.isEmpty()) return;
        boolean success = chatController.sendMessageFromPemilik(pemilik.getIdPemilik(), dokter.getIdDokter(), pesan);
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
                SwingUtilities.invokeLater(() -> loadChatHistory());
            }
        }, 3000, 3000);
    }

    @Override
    public void dispose() {
        if (refreshTimer != null) refreshTimer.cancel();
        super.dispose();
    }

    // Inner class untuk border melengkung
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