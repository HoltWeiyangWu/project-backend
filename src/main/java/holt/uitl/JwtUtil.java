package holt.uitl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * @author Weiyang Wu
 * @date 2024/7/19 13:55
 */
// Usage reference: https://github.com/jwtk/jjwt
public class JwtUtil {
    // Specifies encryption algorithm
    private static final MacAlgorithm algorithm = Jwts.SIG.HS256;
    private static final SecretKey key = algorithm.key().build();
    private static final String issuer = "HoltWYW";

    // Expires in one day
    private static final long EXPIRATION_TIME =  86400000;

    /**
     * Generates a Json Web Token
     * @param username The username of a user's account
     * @param id User's ID
     * @return A signed Json Web Token in String format
     */
    public static String generateToken(String username, Long id) {
        Date now = new Date();
        return Jwts.builder()
                .issuer(issuer)
                .subject(username)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .issuedAt(new Date())
                .id(String.valueOf(id))
                .signWith(key, algorithm)
                .compact();
    }

    /**
     * Validates a Json Web Token
     * @param claims Signed Json Web Token
     * @return True or False to indicate if the token is valid
     */
    public static boolean validateToken(Claims claims) {
        return claims.getIssuer().equals(issuer) && claims.getExpiration().after(new Date());
    }

    public static Claims parseToken(String jws) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(jws)
                .getPayload();
    }
}
