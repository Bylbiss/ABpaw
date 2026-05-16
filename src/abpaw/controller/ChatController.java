/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.controller;

import abpaw.model.dao.ChatDAO;
import abpaw.model.entity.Chat;
import java.sql.Timestamp;
import java.util.List;

public class ChatController {
    private ChatDAO chatDAO;

    public ChatController() {
        chatDAO = new ChatDAO();
    }

    public boolean sendMessageFromPemilik(int idPemilik, int idDokter, String pesan) {
        Chat chat = new Chat();
        chat.setIdPemilik(idPemilik);
        chat.setIdDokter(null);
        chat.setPesan(pesan);
        chat.setStatusBaca("unread");
        chat.setWaktu(new Timestamp(System.currentTimeMillis()));
        return chatDAO.insert(chat);
    }

    public boolean sendMessageFromDokter(int idDokter, int idPemilik, String pesan) {
        Chat chat = new Chat();
        chat.setIdDokter(idDokter);
        chat.setIdPemilik(null);
        chat.setPesan(pesan);
        chat.setStatusBaca("unread");
        chat.setWaktu(new Timestamp(System.currentTimeMillis()));
        return chatDAO.insert(chat);
    }

    public List<Chat> getChatBetween(int idDokter, int idPemilik) {
        return chatDAO.getChatBetween(idDokter, idPemilik);
    }

    public void markAsRead(int idChat) {
        chatDAO.markAsRead(idChat);
    }

    public int getUnreadCountForDokter(int idDokter) {
        return chatDAO.getUnreadCountForDokter(idDokter);
    }

    public int getUnreadCountForPemilik(int idPemilik) {
        return chatDAO.getUnreadCountForPemilik(idPemilik);
    }
}