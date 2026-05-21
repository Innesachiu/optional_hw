package util;
import java.nio.charset.StandardCharsets;import java.security.MessageDigest;
/** Utility for password hashing. */
public class PasswordUtil {
    /** @param raw raw password @return SHA-256 hash */
    public static String hash(String raw){try{MessageDigest md=MessageDigest.getInstance("SHA-256");byte[] b=md.digest(raw.getBytes(StandardCharsets.UTF_8));StringBuilder sb=new StringBuilder();for(byte x:b){sb.append(String.format("%02x",x));}return sb.toString();}catch(Exception e){throw new RuntimeException(e);}}
}
