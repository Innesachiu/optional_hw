package service;
import dao.UserDAO;import model.User;import util.PasswordUtil;
/** Service for user flow. */
public class UserService { private final UserDAO dao=new UserDAO();
    /** @param username username @param email email @param password password @return success */
    public boolean register(String username,String email,String password){if(blank(username)||blank(email)||blank(password))return false;User u=new User();u.setUsername(username.trim());u.setEmail(email.trim());u.setPasswordHash(PasswordUtil.hash(password));u.setAvatarUrl(null);return dao.register(u);} 
    /** @param username username @param password password @return user */
    public User login(String username,String password){if(blank(username)||blank(password))return null;return dao.login(username.trim(),PasswordUtil.hash(password));}
    private boolean blank(String s){return s==null||s.trim().isEmpty();}
}
