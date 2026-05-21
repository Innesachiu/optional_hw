package controller;
import model.User;import service.UserService;
/** Controller for users. */
public class UserController { private final UserService s=new UserService();
    /** @param u username @param e email @param p password @return success */ public boolean register(String u,String e,String p){return s.register(u,e,p);} 
    /** @param u username @param p password @return user */ public User login(String u,String p){return s.login(u,p);} }
