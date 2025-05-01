package com.example.utility;

import com.example.dto.JwtDTO;
import com.example.enums.ProfileRole;
import com.example.exceptions.UnAuthorizedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final String SECRET_KEY = "mazgikeymazgikeymazgikeymazgikeymazgikeymazgikeymazgikeymazgikey"; // At least 256-bit key
    private static final int TOKEN_LIFETIME = 1000 * 3600 * 24;
    private static final int EMAIL_TOKEN_LIFETIME = 1000 * 3600 * 2;

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));

    public static String encode(String profileId) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EMAIL_TOKEN_LIFETIME))
                .setIssuer("Kun uz test portali")
                .claim("id", profileId)
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }
    public static String encode(String profileId, ProfileRole role) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
                .setIssuer("Kun uz test portali")
                .claim("id", profileId)
                .claim("role", role)
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }
    public static String encodePhone(String phone, ProfileRole role) {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_LIFETIME))
                .setIssuer("Kun uz test portali")
                .claim("phone", phone)
                .claim("role", role)
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public static String decodeEmail(String token) {
        try {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);

        Claims claims = jws.getBody();
        return  claims.get("id", String.class);

    }catch (JwtException e){
            System.out.println("EXCEPTION 1");
            throw new UnAuthorizedException("Token not valid");
        }

    }

    public static JwtDTO decodePhone(String token) {
//        try {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);

        Claims claims = jws.getBody();
        String phone = claims.get("phone", String.class);
        String roleStr = claims.get("role", String.class);
        ProfileRole role = ProfileRole.valueOf(roleStr); // Ensure proper enum parsing

        return new JwtDTO(phone, role);
//    }catch (JwtException e){
//            System.out.println("EXCEPTION 1");
//            throw new UnAuthorizedException("Token not valid");
//        }

    }
    public static JwtDTO decode(String token) {
//        try {
        Jws<Claims> jws = Jwts.parser()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);

        Claims claims = jws.getBody();
        String profileId = claims.get("id", String.class);
        String roleStr = claims.get("role", String.class);
        ProfileRole role = ProfileRole.valueOf(roleStr); // Ensure proper enum parsing

        return new JwtDTO(profileId, role);
//    }catch (JwtException e){
//            System.out.println("EXCEPTION 1");
//            throw new UnAuthorizedException("Token not valid");
//        }

    }
}
