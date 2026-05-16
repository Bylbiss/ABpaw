/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class RupiahFormatter {
    
    private static final Locale INDONESIA = new Locale("id", "ID");
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(INDONESIA);
    
    static {
        CURRENCY_FORMAT.setMaximumFractionDigits(0);
        CURRENCY_FORMAT.setMinimumFractionDigits(0);
    }
    
    public static String format(BigDecimal amount) {
        if (amount == null) return "Rp 0";
        return CURRENCY_FORMAT.format(amount);
    }
    
    public static String format(double amount) {
        return CURRENCY_FORMAT.format(amount);
    }
    
    public static String format(int amount) {
        return CURRENCY_FORMAT.format(amount);
    }

    public static BigDecimal parse(String rupiahStr) {
        if (rupiahStr == null) return BigDecimal.ZERO;
        String clean = rupiahStr.replaceAll("[^\\d]", "");
        if (clean.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(clean);
    }
}