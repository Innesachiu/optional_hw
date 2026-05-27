package dto;

/**
 * Request payload for register API.
 */
public class RegisterRequest {
    private String username;
    private String email;
    private String password;

    /** Default constructor. */
    public RegisterRequest() {}
    /** @return username */
    public String getUsername() { return username; }
    /** @param username username */
    public void setUsername(String username) { this.username = username; }
    /** @return email */
    public String getEmail() { return email; }
    /** @param email email */
    public void setEmail(String email) { this.email = email; }
    /** @return plain password */
    public String getPassword() { return password; }
    /** @param password plain password */
    public void setPassword(String password) { this.password = password; }
}
