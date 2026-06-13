package dao;
import model.ProductImage;import util.DBConnection;import java.sql.*;import java.util.*;
/** DAO for product images. */
public class ProductImageDAO {
    /** @param productId product id @return image list */
    public List<ProductImage> findByProductId(int productId){List<ProductImage> list=new ArrayList<>();String sql="SELECT image_id,product_id,image_url,sort_order FROM product_images WHERE product_id=? ORDER BY sort_order";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setInt(1,productId);try(ResultSet r=p.executeQuery()){while(r.next()){ProductImage i=new ProductImage();i.setImageId(r.getInt(1));i.setProductId(r.getInt(2));i.setImageUrl(r.getString(3));i.setSortOrder(r.getInt(4));list.add(i);}}}catch(SQLException e){}return list;}
}
