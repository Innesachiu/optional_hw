package dao;

import model.Category;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for categories table.
 */
public class CategoryDAO {
    /**
     * Returns all categories.
     *
     * @return category list
     */
    public List<Category> findAll() {
        List<Category> list = new ArrayList<>();
        String sql = "SELECT category_id,name FROM categories ORDER BY category_id";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setName(rs.getString("name"));
                list.add(c);
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }
}
