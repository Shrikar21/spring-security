package com.training.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HomeController {

	@GetMapping("/greet/{username}")
	public String sayHello(@PathVariable String username) {
		
		return "Hello, " + username;
	}
	
	@GetMapping("/home")	
	public String welcomePage() {
		
		return "Welcome to spring security";
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
