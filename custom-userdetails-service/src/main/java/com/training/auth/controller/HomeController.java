package com.training.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.training.auth.model.Customer;
import com.training.auth.repository.CustomerRepository;

@RestController
@RequestMapping("/api/v1")
public class HomeController {
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
	
	@GetMapping("/user/dashboard")
	public String userDashboard() {
		
		return "This is user dashboard";
	}
	
	@GetMapping("/admin/dashboard")
	public String adminDashboard() {
		
		return "This is admin dashboard";
	}
}