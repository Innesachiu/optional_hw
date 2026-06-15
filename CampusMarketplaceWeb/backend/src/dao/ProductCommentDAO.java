package dao;

import model.ProductComment;
import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for product_comments table.
 */
public class ProductCommentDAO {
    /**
     * Finds comments for one product, newest first.
     *
     * @param productId product id
     * @return comment list
     */
    public List<ProductComment> findByProductId(int productId) {
        List<ProductComment> list = new ArrayList<>();
        String sql = "SELECT pc.comment_id,pc.product_id,pc.user_id,u.username,pc.content,pc.created_at "
                + "FROM product_comments pc JOIN users u ON pc.user_id=u.user_id "
                + "WHERE pc.product_id=? ORDER BY pc.created_at DESC, pc.comment_id DESC";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }

    /**
     * Creates a comment.
     *
     * @param productId product id
     * @param userId user id
     * @param content comment content
     * @return generated comment id, or -1
     */
    public int create(int productId, int userId, String content) {
        String sql = "INSERT INTO product_comments(product_id,user_id,content) VALUES(?,?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, productId);
            ps.setInt(2, userId);
            ps.setString(3, content);
            int affected = ps.executeUpdate();
            if (affected == 0) return -1;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            return -1;
        } catch (SQLException e) {
            return -1;
        }
    }

    /**
     * Finds one comment by id.
     *
     * @param commentId comment id
     * @return comment or null
     */
    public ProductComment findById(int commentId) {
        String sql = "SELECT pc.comment_id,pc.product_id,pc.user_id,u.username,pc.content,pc.created_at "
                + "FROM product_comments pc JOIN users u ON pc.user_id=u.user_id WHERE pc.comment_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return map(rs);
                }
            }
        } catch (SQLException e) {
            return null;
        }
        return null;
    }

    /**
     * Deletes one comment.
     *
     * @param commentId comment id
     * @return true if deleted
     */
    public boolean deleteById(int commentId) {
        String sql = "DELETE FROM product_comments WHERE comment_id=?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, commentId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    private ProductComment map(ResultSet rs) throws SQLException {
        ProductComment comment = new ProductComment();
        comment.setCommentId(rs.getInt("comment_id"));
        comment.setProductId(rs.getInt("product_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setUsername(rs.getString("username"));
        comment.setContent(rs.getString("content"));
        comment.setCreatedAt(rs.getTimestamp("created_at"));
        return comment;
    }
}
