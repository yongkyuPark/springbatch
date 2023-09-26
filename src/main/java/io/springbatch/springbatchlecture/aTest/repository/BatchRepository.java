package io.springbatch.springbatchlecture.aTest.repository;

import io.springbatch.springbatchlecture.aTest.entity.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BatchRepository extends JpaRepository<Batch, Long> {

    Optional<Batch> findByJobName (String jobName);

}
