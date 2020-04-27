package com.training.demo.service;

import com.training.demo.entity.Project;
import com.training.demo.repository.ProjectRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findOrderById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("can not find"));
    }

    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }
}
