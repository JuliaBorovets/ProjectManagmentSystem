package com.training.demo.repository;

import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    Optional<Task> findById(Long id);

    List<Task> findByProjectAndWorkers(Project project, Worker workers);
}
