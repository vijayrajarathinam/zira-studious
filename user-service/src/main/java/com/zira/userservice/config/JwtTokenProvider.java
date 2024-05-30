package com.zira.userservice.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class JwtTokenProvider {

//    @Value("${auth.token.secret}")
    private static String SECRET_KEY = "36763979244226452948404D635166546A576D5A7134743777217A25432A462D";


//    @Value("${auth.token.expirationTime}")
    private static Long EXPIRATION_TIME = 3600000L;
    private static SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

//    @Autowired
//    private Environment env;
    // env.getProperty("app.max.size")

    public static String generateToken(Authentication auth){
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();

        String roles = populateAuthorities(authorities);
        String jwt = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + EXPIRATION_TIME))
                .claim("email", auth.getName())
                .claim("authorities", roles)
                .signWith(key).compact();

        return jwt;
    }

    public static String getEmailFromJwtToken(String jwt){
            jwt = jwt.substring(7);
            Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
            return String.valueOf(claims.get("email"));
    }

    private static String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority: authorities){
            auths.add(authority.getAuthority());
        }
        return String.join(",",auths);
    }
}
