package io.springbatch.springbatchlecture.aTest.repository;

import io.springbatch.springbatchlecture.aTest.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDetailRepository extends JpaRepository<ProductDetail, Long> {
}
