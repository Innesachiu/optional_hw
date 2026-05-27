package dto;

/**
 * Request payload for login API.
 */
public class LoginRequest {
    private String username;
    private String password;

    /** Default constructor. */
    public LoginRequest() {}
    /** @return username */
    public String getUsername() { return username; }
    /** @param username username */
    public void setUsername(String username) { this.username = username; }
    /** @return plain password */
    public String getPassword() { return password; }
    /** @param password plain password */
    public void setPassword(String password) { this.password = password; }
}
