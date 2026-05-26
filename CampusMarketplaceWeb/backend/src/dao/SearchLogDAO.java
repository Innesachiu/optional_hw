package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data access for search_logs table.
 */
public class SearchLogDAO {
    /**
     * Inserts one search keyword log.
     *
     * @param userId user id nullable
     * @param keyword keyword
     */
    public void log(Integer userId, String keyword) {
        String sql = "INSERT INTO search_logs(user_id,keyword) VALUES(?,?)";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, userId);
            }
            ps.setString(2, keyword);
            ps.executeUpdate();
        } catch (SQLException e) {
            // keep silent for first runnable version
        }
    }

    /**
     * Gets top 10 keywords in last 7 days.
     *
     * @return keyword result list like "keyword (count)"
     */
    public List<String> popularKeywordsLast7Days() {
        List<String> list = new ArrayList<>();
        String sql = "SELECT keyword,COUNT(*) AS cnt FROM search_logs WHERE created_at >= NOW() - INTERVAL 7 DAY GROUP BY keyword ORDER BY cnt DESC, keyword ASC LIMIT 10";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(rs.getString("keyword") + " (" + rs.getInt("cnt") + ")");
            }
        } catch (SQLException e) {
            return list;
        }
        return list;
    }
}
