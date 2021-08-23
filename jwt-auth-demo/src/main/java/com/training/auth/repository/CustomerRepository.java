package com.training.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.training.auth.model.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	public Customer findByEmail(String email);
}
