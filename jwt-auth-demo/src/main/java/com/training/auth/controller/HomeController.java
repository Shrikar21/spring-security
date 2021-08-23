package com.training.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.auth.exception.CustomAuthenticationException;
import com.training.auth.model.AuthenticateRequest;
import com.training.auth.model.AuthenticateResponse;
import com.training.auth.model.Customer;
import com.training.auth.repository.CustomerRepository;
import com.training.auth.util.JwtUtil;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtUtil;

	@PostMapping("/authenticate")
	public ResponseEntity<AuthenticateResponse> authenticate(@RequestBody AuthenticateRequest request) {
		
		// First authenticate user and once user is authenticated we will generate jwt token and send in response.
		
		String email = request.getEmail();
		String password = request.getPassword();
		
		try {
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
	     	authenticationManager.authenticate(authentication);
		}
		catch(BadCredentialsException ex) {
			throw new CustomAuthenticationException("Invalid email or password");
		}
		catch(LockedException ex) {
			 throw new CustomAuthenticationException("Your accorunt is locked.");
		}
		catch(DisabledException ex) {
			throw new CustomAuthenticationException("Your account is disabled.");
		}
		
		// Generate jwt token
		UserDetails user = userDetailsService.loadUserByUsername(email);		
		String jwtToken = jwtUtil.generateToken(user);
		
		AuthenticateResponse response = new AuthenticateResponse(jwtToken);
		return ResponseEntity.ok(response);
	}
	
	@ExceptionHandler(CustomAuthenticationException.class)
	public ResponseEntity<String> handleAuthenticationException(CustomAuthenticationException ex) {
		
		return new ResponseEntity<String>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
	}

	@GetMapping("/greet/{username}")
	public String sayHello(@PathVariable String username) {

		return "Hello, " + username;
	}

	@GetMapping("/home")
	public String welcomePage() {

		return "Welcome to spring security";
	}

	@PostMapping("/customers")
	public ResponseEntity<Customer> addNewCustomer(@RequestBody Customer customer) {

		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		Customer savedCustomer = customerRepository.save(customer);
		return ResponseEntity.ok(savedCustomer);
	}

}