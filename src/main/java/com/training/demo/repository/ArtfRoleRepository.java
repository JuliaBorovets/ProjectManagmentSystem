package com.training.demo.repository;

import com.training.demo.entity.ArtfRole;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ArtfRoleRepository extends CrudRepository<ArtfRole, Long> {
    Optional<ArtfRole> findById(Long id);
}
