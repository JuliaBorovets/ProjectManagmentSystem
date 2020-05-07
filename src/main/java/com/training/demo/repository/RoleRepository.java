package com.training.demo.repository;

import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.training.demo.entity.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long> {
    Optional<Role> findById(Long id);

    Optional<Role>  findByName(String name);
}
