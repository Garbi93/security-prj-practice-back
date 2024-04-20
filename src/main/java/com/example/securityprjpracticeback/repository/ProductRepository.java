package com.example.securityprjpracticeback.repository;

import com.example.securityprjpracticeback.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
