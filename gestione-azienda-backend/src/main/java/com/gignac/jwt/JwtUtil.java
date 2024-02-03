package com.gignac.jwt;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
@SuppressWarnings("deprecation")
public class JwtUtil {

	private static final String SECRET_KEY = "foobar_123456789_foobar_123456789_foobar_123456789_foobar_123456789";

	public String issueToken(String subject) {
		return issueToken(subject, Map.of());
	}

	public String issueToken(String subject, String... scopes) {
		return issueToken(subject, Map.of("scopes", scopes));
	}

	public String issueToken(String subject, List<String> scopes) {
		return issueToken(subject, Map.of("scopes", scopes));

	}

	public String issueToken(String subject, Map<String, Object> claims) {
		return Jwts.builder()
				   .setClaims(claims)
				   .setSubject(subject)
				   .setIssuer("https://gignac.com")
				   .setIssuedAt(Date.from(Instant.now()))
				   .setExpiration(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
				   .signWith(getSigningKey(), SignatureAlgorithm.HS256)
				   .compact();
	}

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
	}

	public String getSubject(String token) {
		return this.getClaims(token)
				   .getSubject();
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
				   .setSigningKey(this.getSigningKey())
				   .build()
				   .parseClaimsJws(token)
				   .getBody();
	}

	public boolean isTokenValid(String jwt, String username) {
		String subject = this.getSubject(jwt);
		return subject.equals(username) && !this.isTokenExpired(jwt);
	}

	private boolean isTokenExpired(String jwt) {
		return this.getClaims(jwt)
				   .getExpiration()
				   .before(Date.from(Instant.now()));
	}

}
