package io.springbatch.springbatchlecture.bTest.repository;

import io.springbatch.springbatchlecture.bTest.entity.TestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BTestRepository extends JpaRepository<TestEntity, Integer> {
}
