package model;

import java.sql.Timestamp;

/**
 * User entity mapping users table.
 */
public class User {
    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private String avatarUrl;
    private Timestamp createdAt;

    /** Default constructor. */
    public User() {}

    /** @return user id */
    public int getUserId() { return userId; }
    /** @param userId user id */
    public void setUserId(int userId) { this.userId = userId; }
    /** @return username */
    public String getUsername() { return username; }
    /** @param username username */
    public void setUsername(String username) { this.username = username; }
    /** @return email */
    public String getEmail() { return email; }
    /** @param email email */
    public void setEmail(String email) { this.email = email; }
    /** @return password hash */
    public String getPasswordHash() { return passwordHash; }
    /** @param passwordHash password hash */
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    /** @return avatar url */
    public String getAvatarUrl() { return avatarUrl; }
    /** @param avatarUrl avatar url */
    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    /** @return created time */
    public Timestamp getCreatedAt() { return createdAt; }
    /** @param createdAt created time */
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
