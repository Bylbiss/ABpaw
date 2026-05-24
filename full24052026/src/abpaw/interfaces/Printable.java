/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.interfaces;

public interface Printable {
    
    String getPrintTitle();

    String[] getPrintContent();
    
    String[] getPrintFooter();
    
    boolean print();
    
    boolean saveToFile(String filename);
}