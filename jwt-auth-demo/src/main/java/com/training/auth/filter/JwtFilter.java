package com.training.auth.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.training.auth.util.JwtUtil;

@Component
public class JwtFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		// Check if JWT token is available in request header or not. If it is available check if token is valid and not expired.
		
		String authorizationHeader = request.getHeader("Authorization");
		String jwtToken = null;
		
		System.out.println("Authoriation Header => " + authorizationHeader);
		
		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer")) {
			
			jwtToken = authorizationHeader.substring(7);
			String username = jwtUtil.extractUsername(jwtToken);
			System.out.println("Username => " + username + " - " + SecurityContextHolder.getContext().getAuthentication() == null);
			
			// Load user from userdetailsservice.
			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				UserDetails user = userDetailsService.loadUserByUsername(username);
				
				// validate jwt token, If it is valid, add user in securitycontext.
				if (jwtUtil.validateToken(jwtToken, user)) {
					
					UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, user.getPassword(), user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					System.out.println("JwtToken is valid.");
				}
			}			
		}
		filterChain.doFilter(request, response);
	}

}
