package org.example.system;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.example.model.User;

import java.security.Key;
import java.util.List;

public class Token {
    private static final String SECRET_KEY = "AoT3QFTTEkj16rCby/TPVBWvfSQHL3GeEz3zVwEd6LDrQDT97sgDY8HJyxgnH79jupBWFOQ1+7fRPBLZfpuA2lwwHqTgk+NJcWQnDpHn31CVm63Or5c5gb4H7/eSIdd+7hf3v+0a5qVsnyxkHbcxXquqk9ezxrUe93cFppxH4/kF/kGBBamm3kuUVbdBUY39c4U3NRkzSO+XdGs69ssK5SPzshn01axCJoNXqqj+ytebuMwF8oI9+ZDqj/XsQ1CLnChbsL+HCl68ioTeoYU9PLrO4on+rNHGPI0Cx6HrVse7M3WQBPGzOd1TvRh9eWJrvQrP/hm6kOR7KrWKuyJzrQh7OoDxrweXFH8toXeQRD8=";

    public static String gerarToken(int subject, boolean isAdmin) {
        String jwtToken = Jwts.builder()
                .setSubject(String.valueOf(subject))
                .claim("user_id", String.valueOf(subject))
                .claim("admin", isAdmin)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return jwtToken;
    }
    public static boolean isValidToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

            // Use o parser JWT para analisar o token com a chave secreta
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static Jws<Claims> parseToken(String token){
        return  Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
    }
    public static boolean isAdmin(String token, List<User> users) {
        for (User user : users) {
            if (user.getToken().equals(token)) {
                Jws<Claims> parsedToken = parseToken(token);

                return parsedToken.getBody().get("isAdmin", Boolean.class);
            }
        }
        return false;
    }
}
