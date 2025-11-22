package com.clinic.security;

import com.clinic.exception.NotAuthenticatedException;
import io.jsonwebtoken.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtil {
    private String SECRET_KEY = "My$#cU%it&C@d#"; // Use a more secure secret in production
    private long TOKEN_VALIDITY = 1000 * 60 * 60; // 1 hour

    public String generateToken(UserDetails userDetails) {
        return  Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setIssuer("CMS-App-Service")
                .claim("created", Calendar.getInstance().getTime())
                .setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis()+ TOKEN_VALIDITY );
    }

    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(String token) {
        return extractAllClaims(token).getExpiration().before(new Date());
    }

    public boolean isValidFormat(String token) throws NotAuthenticatedException {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {

            throw new NotAuthenticatedException("INVALID TOKEN Signature");
        } catch (MalformedJwtException e) {

            throw new NotAuthenticatedException("INVALID TOKEN");
        } catch (ExpiredJwtException e) {

            throw new NotAuthenticatedException("Expired Token");
        } catch (IllegalArgumentException e) {
            throw new NotAuthenticatedException("EMPTY CLAIMS TOKEN");
        }
    }
}
