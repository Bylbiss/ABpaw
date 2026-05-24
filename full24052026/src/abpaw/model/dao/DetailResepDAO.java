/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package abpaw.model.dao;

import abpaw.model.entity.DetailResep;
import abpaw.model.db.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DetailResepDAO {

    public boolean insert(DetailResep detail) {
        String sql = "INSERT INTO detail_resep (id_resep, id_obat, takaran, jumlah, catatan) VALUES (?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, detail.getIdResep());
            stmt.setInt(2, detail.getIdObat());
            stmt.setString(3, detail.getTakaran());
            stmt.setInt(4, detail.getJumlah());
            stmt.setString(5, detail.getCatatan());
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) detail.setIdDetail(rs.getInt(1));
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<DetailResep> getByResep(int idResep) {
        List<DetailResep> list = new ArrayList<>();
        String sql = "SELECT * FROM detail_resep WHERE id_resep = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idResep);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                DetailResep d = new DetailResep();
                d.setIdDetail(rs.getInt("id_detail"));
                d.setIdResep(rs.getInt("id_resep"));
                d.setIdObat(rs.getInt("id_obat"));
                d.setTakaran(rs.getString("takaran"));
                d.setJumlah(rs.getInt("jumlah"));
                d.setCatatan(rs.getString("catatan"));
                list.add(d);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}