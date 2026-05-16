/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class KodePemesananGenerator {
    private static int lastCounter = 0;
    private static String lastTimestamp = "";

    public static synchronized String generate() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        if (!now.equals(lastTimestamp)) {
            lastCounter = 0;
            lastTimestamp = now;
        }
        lastCounter++;
        return String.format("ONL-%s-%03d", now, lastCounter);
    }

    public static String generateWithPrefix(String prefix) {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        return String.format("%s-%s-%d", prefix.toUpperCase(), now, System.currentTimeMillis() % 1000);
    }
}