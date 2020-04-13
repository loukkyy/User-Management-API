package com.demo.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.Base64UrlCodec;

@Component
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:60000}") // 1 minute
    private long validityInMilliseconds;
    
    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @PostConstruct
    protected void init() {
        secretKey = Base64UrlCodec.BASE64URL.encode(secretKey.getBytes());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    	final Claims claims = extractAllClaims(token);
    	return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) throws JwtException {
    	return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }
    private Boolean isTokenExpired(String token) {
    	return extractExpiration(token).before(new Date());
    }
    public String generateToken(UserDetails userDetails) {
    	Map<String, Object> claims = new HashMap<>();
    	return createToken(userDetails.getUsername(), claims);
    }
    private String createToken(String username, Map<String, Object> claims) {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact();
    }
    public Boolean validateToken(String token, UserDetails userDetails) throws ExpiredJwtException {
    	final String username = extractUsername(token);
    	return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    public String resolveToken(String authorizationHeader) throws UnsupportedJwtException {
    	if (!authorizationHeader.startsWith(prefix)) {
    		throw new UnsupportedJwtException("The token should start with the prefix '" + prefix + "'");
    	}
		return authorizationHeader.substring(prefix.length());
    }
}
