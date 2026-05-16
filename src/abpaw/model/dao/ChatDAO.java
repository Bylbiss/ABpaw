/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.Chat;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    public boolean insert(Chat chat) {
        String sql = "INSERT INTO chat (id_dokter, id_pemilik, pesan, status_baca) VALUES (?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (chat.getIdDokter() != null) stmt.setInt(1, chat.getIdDokter());
            else stmt.setNull(1, Types.INTEGER);
            if (chat.getIdPemilik() != null) stmt.setInt(2, chat.getIdPemilik());
            else stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, chat.getPesan());
            stmt.setString(4, chat.getStatusBaca());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) chat.setIdChat(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean update(Chat chat) {
        String sql = "UPDATE chat SET id_dokter=?, id_pemilik=?, pesan=?, status_baca=? WHERE id_chat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (chat.getIdDokter() != null) stmt.setInt(1, chat.getIdDokter());
            else stmt.setNull(1, Types.INTEGER);
            if (chat.getIdPemilik() != null) stmt.setInt(2, chat.getIdPemilik());
            else stmt.setNull(2, Types.INTEGER);
            stmt.setString(3, chat.getPesan());
            stmt.setString(4, chat.getStatusBaca());
            stmt.setInt(5, chat.getIdChat());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(int id) {
        String sql = "DELETE FROM chat WHERE id_chat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Chat getById(int id) {
        String sql = "SELECT * FROM chat WHERE id_chat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return extractChat(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Chat> getAll() {
        List<Chat> list = new ArrayList<>();
        String sql = "SELECT * FROM chat ORDER BY waktu ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(extractChat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Chat> getChatBetween(int idDokter, int idPemilik) {
        List<Chat> list = new ArrayList<>();
        String sql = "SELECT * FROM chat WHERE "
                + "(id_dokter=? AND id_pemilik IS NULL) OR "
                + "(id_dokter IS NULL AND id_pemilik=?) "
                + "ORDER BY waktu ASC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDokter);
            stmt.setInt(2, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractChat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Chat> getChatsForDokter(int idDokter) {
        List<Chat> list = new ArrayList<>();
        String sql = "SELECT * FROM chat WHERE id_dokter=? ORDER BY waktu DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDokter);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractChat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Chat> getChatsForPemilik(int idPemilik) {
        List<Chat> list = new ArrayList<>();
        String sql = "SELECT * FROM chat WHERE id_pemilik=? ORDER BY waktu DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(extractChat(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void markAsRead(int idChat) {
        String sql = "UPDATE chat SET status_baca='read' WHERE id_chat=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idChat);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getUnreadCountForDokter(int idDokter) {
        String sql = "SELECT COUNT(*) FROM chat WHERE id_dokter=? AND status_baca='unread'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idDokter);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getUnreadCountForPemilik(int idPemilik) {
        String sql = "SELECT COUNT(*) FROM chat WHERE id_pemilik=? AND status_baca='unread'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idPemilik);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private Chat extractChat(ResultSet rs) throws SQLException {
        Chat c = new Chat();
        c.setIdChat(rs.getInt("id_chat"));
        int idDokter = rs.getInt("id_dokter");
        if (!rs.wasNull()) c.setIdDokter(idDokter);
        int idPemilik = rs.getInt("id_pemilik");
        if (!rs.wasNull()) c.setIdPemilik(idPemilik);
        c.setPesan(rs.getString("pesan"));
        c.setWaktu(rs.getTimestamp("waktu"));
        c.setStatusBaca(rs.getString("status_baca"));
        return c;
    }
}