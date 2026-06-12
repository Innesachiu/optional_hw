package dao;
import model.Category;import util.DBConnection;import java.sql.*;import java.util.*;
/** DAO for categories. */
public class CategoryDAO {
    /** @return all categories */
    public List<Category> findAll(){List<Category> list=new ArrayList<>();String sql="SELECT category_id,name FROM categories ORDER BY category_id";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql);ResultSet r=p.executeQuery()){while(r.next()){list.add(new Category(r.getInt(1),r.getString(2)));}}catch(SQLException e){}return list;}
}
