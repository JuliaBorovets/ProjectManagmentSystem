package com.training.demo.repository;

import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {

    Optional<Worker> findById(Long id);

    Optional<Worker> findByLogin(String login);

    Optional<Worker> findByName(String name);

    Optional<Worker> findByProjectId(Long projectId);
}
