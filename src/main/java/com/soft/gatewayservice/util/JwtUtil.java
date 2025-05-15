package com.soft.gatewayservice.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;

//classe utiliser pour toutes les actions de JWT

@Service
public class JwtUtil{

    public static final String SECRET = "63E7E8BE05A8C6EF206C75DF2D34F60FAA2C84E7F1B2095492A0056416056583";

    public void validateToken(final String token){
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    public Key getSignKey(){
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
