package dao;
import model.User;import util.DBConnection;import java.sql.*;
/** DAO for users. */
public class UserDAO {
    /** @param user user @return success */
    public boolean register(User user){String sql="INSERT INTO users(username,email,password_hash,avatar_url) VALUES(?,?,?,?)";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,user.getUsername());p.setString(2,user.getEmail());p.setString(3,user.getPasswordHash());p.setString(4,user.getAvatarUrl());return p.executeUpdate()>0;}catch(SQLException e){return false;}}
    /** @param username username @param passwordHash hash @return user or null */
    public User login(String username,String passwordHash){String sql="SELECT user_id,username,email,password_hash,avatar_url FROM users WHERE username=? AND password_hash=?";try(Connection c=DBConnection.getConnection();PreparedStatement p=c.prepareStatement(sql)){p.setString(1,username);p.setString(2,passwordHash);try(ResultSet r=p.executeQuery()){if(r.next()){return map(r);}}}catch(SQLException e){}return null;}
    private User map(ResultSet r)throws SQLException{User u=new User();u.setUserId(r.getInt("user_id"));u.setUsername(r.getString("username"));u.setEmail(r.getString("email"));u.setPasswordHash(r.getString("password_hash"));u.setAvatarUrl(r.getString("avatar_url"));return u;}
}
