package com.training.demo.repository;

import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

    Optional<Project> findById(Long id);

    List<Project> findByWorkersOrAdmin(Worker workers, Worker admin);

    List<Project> findAll();

}
