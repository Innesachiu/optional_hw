package dao;

import util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for search keyword logs.
 */
public class SearchLogDAO {
    public void logSearch(Integer userId, String keyword) {
        String sql = "INSERT INTO search_logs(user_id, keyword) VALUES(?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            if (userId == null) {
                ps.setNull(1, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, userId);
            }
            ps.setString(2, keyword);
            ps.executeUpdate();
        } catch (SQLException e) {
        }
    }

    public List<String> getPopularKeywordsLast7Days() {
        List<String> keywords = new ArrayList<>();
        String sql = "SELECT keyword, COUNT(*) cnt FROM search_logs WHERE searched_at >= NOW() - INTERVAL 7 DAY GROUP BY keyword ORDER BY cnt DESC, keyword ASC LIMIT 10";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                keywords.add(rs.getString("keyword") + " (" + rs.getInt("cnt") + ")");
            }
        } catch (SQLException e) {
            return keywords;
        }
        return keywords;
    }
}
