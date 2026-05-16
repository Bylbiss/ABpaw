/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package abpaw;

import abpaw.view.LandingPageView;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;

public class ABpaw {
    
    public static void main(String[] args) {
        // Set look and feel FlatLaf (modern UI)
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new LandingPageView();
        });
    }
}