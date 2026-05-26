/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.utils;

import javax.swing.*;
import java.awt.*;
/**
 *
 * @author LOQ
 */
public class IconHelper {

    public static ImageIcon getScaledPawIcon(int width, int height) {
        try {
            java.net.URL imgURL = IconHelper.class.getResource("/images/icon.jpg");
            if (imgURL != null) {
                ImageIcon original = new ImageIcon(imgURL);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static ImageIcon getScaledIcon(String imagePath, int width, int height) {
        try {
            java.net.URL imgURL = IconHelper.class.getResource(imagePath);
            if (imgURL != null) {
                ImageIcon original = new ImageIcon(imgURL);
                Image scaled = original.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                return new ImageIcon(scaled);
            } else {
                System.err.println("Gambar tidak ditemukan: " + imagePath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
