package com.training.demo.repository;

import com.training.demo.entity.Worker;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerRepository  extends CrudRepository<Worker, Long> {

}
