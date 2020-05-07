package com.training.demo.service;

import com.training.demo.dto.AssignmentDTO;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Assignment;
import com.training.demo.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;


    public AssignmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    public Assignment findById (Long id){
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("can not find"));
    }

    public Assignment findByWorkerId (Long id){
        return assignmentRepository.findByWorkerId(id)
                .orElseThrow(() -> new RuntimeException("can not find"));
    }
    public List<AssignmentDTO> findAllAssignByWorkerId(Long id) {
        return assignmentRepository.findByWorkerId(id).stream()
                .map(assignment -> AssignmentDTO.builder()
                        .id(assignment.getId())
                        .worker(assignment.getWorker())
                        .role(assignment.getRole())
                        .task(assignment.getTask())
                        .build()).collect(Collectors.toList());

    }

    public List<Assignment> getAllAssignments(){
        return (List<Assignment>) assignmentRepository.findAll();

    }
}
