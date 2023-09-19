package io.springbatch.springbatchlecture.aTest.repository;

import io.springbatch.springbatchlecture.aTest.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
