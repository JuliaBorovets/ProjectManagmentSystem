package com.training.demo.repository;

import com.training.demo.entity.Assignment;
import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssignmentRepository extends CrudRepository<Assignment,Long> {

    Optional<Assignment> findById(Long id);
    Optional<Assignment> findByWorkerId(Long worker_id);
}
