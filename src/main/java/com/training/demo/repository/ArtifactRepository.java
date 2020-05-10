package com.training.demo.repository;

import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, Long> {
    Optional<Artifact> findById(Long id);

    List<Artifact> findByTask(Task task);

    List<Artifact> findByProject(Project project);
}
