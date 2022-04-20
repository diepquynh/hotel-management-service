package vn.utc.hotelmanager.jwt;

import vn.utc.hotelmanager.jwt.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

@Component
public class JwtUtils {

    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Autowired
    public JwtUtils(JwtConfig jwtConfig, SecretKey secretKey) {
        this.jwtConfig = jwtConfig;
        this.secretKey = secretKey;
    }

    public String generateToken(String subject,
                                Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("authorities", authorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(jwtConfig.getTokenExpirationAfterDays())))
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
    }
}
