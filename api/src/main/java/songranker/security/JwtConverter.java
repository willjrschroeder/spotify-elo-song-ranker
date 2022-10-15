package songranker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import songranker.models.AppUser;

import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtConverter {

    private Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // this method creates a JWT for a passed in AppUser
    public String buildJwt(AppUser user) {
        String token = io.jsonwebtoken.Jwts.builder()
                .setId(user.getAppUserId() + "")
                .claim("display_name", user.getDisplayName())
                .claim("roles", user.getRoles())
                .claim("id", user.getAppUserId())
                .setIssuer("spotify elo ranker")
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date( new Date().getTime() + 3600000 ))
                .signWith(key)
                .compact();
        return token;
    }

    // this method returns an object containing user data, parsed from a JWT
    public Jws<Claims> parseJwt(String jwt) {

        Jws<Claims> userClaims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build().parseClaimsJws( jwt );

        return userClaims;
    }
}
