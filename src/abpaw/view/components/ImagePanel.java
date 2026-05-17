/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImagePanel extends JPanel {
    private BufferedImage image;
    private Image scaledImage;
    private int imageWidth = -1;
    private int imageHeight = -1;
    private boolean keepAspectRatio = true;
    private int horizontalAlignment = SwingConstants.CENTER;
    private int verticalAlignment = SwingConstants.CENTER;

    public ImagePanel() {
        setOpaque(false);
    }

    public ImagePanel(String imagePath) {
        this();
        setImage(imagePath);
    }

    public void setImage(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
            scaleImage();
            repaint();
        } catch (IOException e) {
            System.err.println("Gagal memuat gambar: " + imagePath);
            e.printStackTrace();
        }
    }

    public void setImage(BufferedImage img) {
        this.image = img;
        scaleImage();
        repaint();
    }

    public void setImageSize(int width, int height) {
        this.imageWidth = width;
        this.imageHeight = height;
        scaleImage();
        repaint();
    }

    public void setKeepAspectRatio(boolean keep) {
        this.keepAspectRatio = keep;
        scaleImage();
        repaint();
    }

    public void setHorizontalAlignment(int alignment) {
        this.horizontalAlignment = alignment;
        repaint();
    }

    public void setVerticalAlignment(int alignment) {
        this.verticalAlignment = alignment;
        repaint();
    }

    private void scaleImage() {
        if (image == null) return;

        int targetWidth = imageWidth > 0 ? imageWidth : getWidth();
        int targetHeight = imageHeight > 0 ? imageHeight : getHeight();

        if (targetWidth <= 0 || targetHeight <= 0) {
            scaledImage = image;
            return;
        }

        if (keepAspectRatio) {
            double imgRatio = (double) image.getWidth() / image.getHeight();
            double panelRatio = (double) targetWidth / targetHeight;

            if (imgRatio > panelRatio) {
                // Lebar lebih dominan
                scaledImage = image.getScaledInstance(targetWidth, -1, Image.SCALE_SMOOTH);
            } else {
                scaledImage = image.getScaledInstance(-1, targetHeight, Image.SCALE_SMOOTH);
            }
        } else {
            scaledImage = image.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (scaledImage == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int x = 0, y = 0;
        int imgW = scaledImage.getWidth(null);
        int imgH = scaledImage.getHeight(null);

        // Hitung posisi horizontal
        switch (horizontalAlignment) {
            case SwingConstants.LEFT:
                x = 0;
                break;
            case SwingConstants.RIGHT:
                x = getWidth() - imgW;
                break;
            default: // CENTER
                x = (getWidth() - imgW) / 2;
                break;
        }

        // Hitung posisi vertikal
        switch (verticalAlignment) {
            case SwingConstants.TOP:
                y = 0;
                break;
            case SwingConstants.BOTTOM:
                y = getHeight() - imgH;
                break;
            default: // CENTER
                y = (getHeight() - imgH) / 2;
                break;
        }

        g2.drawImage(scaledImage, x, y, this);
        g2.dispose();
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        scaleImage();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        scaleImage();
    }
}