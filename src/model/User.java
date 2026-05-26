package model;

/** User entity. */
public class User {
    private int userId; private String username; private String email; private String passwordHash; private String avatarUrl;
    /** Default constructor. */ public User() {}
    /** Full constructor. */ public User(int userId, String username, String email, String passwordHash, String avatarUrl){this.userId=userId;this.username=username;this.email=email;this.passwordHash=passwordHash;this.avatarUrl=avatarUrl;}
    /** @return user id */ public int getUserId(){return userId;} /** @param userId id */ public void setUserId(int userId){this.userId=userId;}
    /** @return username */ public String getUsername(){return username;} /** @param username username */ public void setUsername(String username){this.username=username;}
    /** @return email */ public String getEmail(){return email;} /** @param email email */ public void setEmail(String email){this.email=email;}
    /** @return password hash */ public String getPasswordHash(){return passwordHash;} /** @param passwordHash hash */ public void setPasswordHash(String passwordHash){this.passwordHash=passwordHash;}
    /** @return avatar url */ public String getAvatarUrl(){return avatarUrl;} /** @param avatarUrl avatar */ public void setAvatarUrl(String avatarUrl){this.avatarUrl=avatarUrl;}
}
