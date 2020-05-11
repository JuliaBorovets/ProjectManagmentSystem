package com.training.demo.repository;

import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtifactRepository extends CrudRepository<Artifact, Long> {

    Optional<Artifact> findById(Long id);

    List<Artifact> findByProject(Project project);
}
