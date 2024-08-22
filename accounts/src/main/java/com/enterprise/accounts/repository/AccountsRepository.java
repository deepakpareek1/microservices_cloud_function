package com.enterprise.accounts.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import com.enterprise.accounts.entity.Accounts;
import com.enterprise.accounts.entity.Customer;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

	Optional<Accounts> findByCustomerId(Long customerId);
	
	@Transactional
	@Modifying
	void deleteByCustomerId(Long customerId);
}
