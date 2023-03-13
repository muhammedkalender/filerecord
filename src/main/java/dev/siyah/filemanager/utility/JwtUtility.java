package dev.siyah.filemanager.utility;

import dev.siyah.filemanager.entity.Member;
import dev.siyah.filemanager.model.auth.MemberDetail;
import dev.siyah.filemanager.properties.JWTProperties;
import dev.siyah.filemanager.service.jwt.UserDetail;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtUtility {
    private final JWTProperties jwtProperties;

    public String generateToken(MemberDetail memberDetail) {
        return createToken(memberDetail.getUsername());
    }

    public String createToken(String subject) {
        return Jwts.builder().setSubject(subject).setIssuedAt(new Date((new Date()).getTime())).setExpiration(new Date((new Date()).getTime() + this.jwtProperties.getJwtExp())).signWith(SignatureAlgorithm.HS256, jwtProperties.getSecret()).compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception ignored) {

        }

        return false;
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token).getBody().getSubject();
    }
}
