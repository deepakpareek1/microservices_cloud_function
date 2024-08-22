package com.enterprise.accounts.entity;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@Getter@Setter@ToString@AllArgsConstructor@NoArgsConstructor
public class Customer extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name="native", strategy = "native")
	@Column(name="customer_id")
	private Long customerId;
	
	private String name;
	
	private String email;
	
	private String mobileNumber;

}
