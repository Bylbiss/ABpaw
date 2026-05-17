/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.interfaces;

public interface Chatable {
    
    int getId();
    
    String getDisplayName();
    
    String getRole();

    boolean sendMessage(Chatable penerima, String pesan);
    
    int getUnreadCount();
}