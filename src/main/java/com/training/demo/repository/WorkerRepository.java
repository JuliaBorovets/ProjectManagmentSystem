package com.training.demo.repository;

import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends CrudRepository<Worker, Long> {

    Optional<Worker> findById(Long id);

    Optional<Worker> findByLogin(String login);

    List<Worker> findWorkersByProjects(Project project);

    Optional<Worker> findByIdAndLogin(Long id, String login);


}
