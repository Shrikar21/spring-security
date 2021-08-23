package com.training.auth.util;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	 private static final String SECRET_KEY = "abcdjfhhhhsmdkkkfkksmddd";
	
	 public String generateToken(UserDetails customer) {
		 	
		 Map<String, Object> claims = new HashMap<>();
		 claims.put("name", "value");	 
	     
		 return Jwts.builder().setClaims(claims)
		                                 .setSubject(customer.getUsername())
		                                 .setIssuedAt(new Date(System.currentTimeMillis()))
		                                 .setExpiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
		                                 .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
		                                 .compact();
	 }
	 
	 public boolean validateToken(String jwtToken, UserDetails customer) {
		 
		 String username = extractUsername(jwtToken);
		 boolean expired = isTokenExpired(jwtToken);
		 
		 return username.equals(customer.getUsername()) && !expired;
		 
	 }
	 
	 public String extractUsername(String jwtToken) {
		 Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody();
		 return claims.getSubject();
	 }
	 
	 public boolean isTokenExpired(String jwtToken) {
		 
		 Claims claims = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken).getBody();
		 return claims.getExpiration().before(new Date());
	 }
	 


}















