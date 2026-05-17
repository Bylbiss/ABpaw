/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.components;

import javax.swing.*;
import java.awt.*;

public class PlaceholderTextField extends JTextField {
    private String placeholder;

    public PlaceholderTextField() {
        super();
    }

    public PlaceholderTextField(String placeholder) {
        super();
        this.placeholder = placeholder;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (placeholder == null || placeholder.isEmpty() || getText().isEmpty()) {
            return;
        }

        // Gambar placeholder jika field kosong dan tidak memiliki focus
        if (getText().isEmpty() && !isFocusOwner()) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getDisabledTextColor());
            g2.setFont(getFont().deriveFont(Font.ITALIC));
            int x = getInsets().left + 5;
            int y = (getHeight() - g2.getFontMetrics().getHeight()) / 2 + g2.getFontMetrics().getAscent();
            g2.drawString(placeholder, x, y);
            g2.dispose();
        }
    }

    @Override
    public Color getDisabledTextColor() {
        return UIManager.getColor("TextField.disabledForeground");
    }
}