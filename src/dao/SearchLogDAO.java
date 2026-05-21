package dao;
import util.DBConnection;import java.sql.*;import java.util.*;
/** DAO for search logs. */
public class SearchLogDAO {
    /** @param userId user id @param keyword keyword */
    public void logKeyword(Integer userId,String keyword){String sql="INSERT INTO search_logs(user_id,keyword) VALUES(?,?)";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){if(userId==null)p.setNull(1,Types.INTEGER);else p.setInt(1,userId);p.setString(2,keyword);p.executeUpdate();}catch(SQLException e){}}
    /** @return top keywords */
    public List<String> topKeywordsLast7Days(){List<String> list=new ArrayList<>();String sql="SELECT keyword,COUNT(*) cnt FROM search_logs WHERE created_at>=NOW()-INTERVAL 7 DAY GROUP BY keyword ORDER BY cnt DESC LIMIT 10";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next())list.add(r.getString(1)+" ("+r.getInt(2)+")");}catch(SQLException e){}return list;}
}
