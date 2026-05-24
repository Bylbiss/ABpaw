/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AntrianGenerator {
    private static int lastCounter = 0;
    private static String lastDate = "";

    public static synchronized String generate() {
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
        // Reset counter jika hari berubah
        if (!today.equals(lastDate)) {
            lastCounter = 0;
            lastDate = today;
        }
        
        lastCounter++;
        
        // Format: ANT-20260516-001
        return String.format("ANT-%s-%03d", today, lastCounter);
    }

    public static synchronized void reset() {
        lastCounter = 0;
        lastDate = "";
    }
}