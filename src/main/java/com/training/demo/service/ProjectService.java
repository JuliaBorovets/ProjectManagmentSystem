package com.training.demo.service;

import com.training.demo.dto.ProjectDTO;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ProjectRepository;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findProjectById(Long id){
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Немає такого проекту"));
    }

    public Project findProjectByName(String name){
        return projectRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Немає такого проекту"));
    }
 //   public List<ProjectDTO> findProjectsByWorkerId(Long id) {
//        return projectRepository.find(id).stream()
//                .map(projects -> ProjectDTO.builder()
//                        .id(projects.getId())
//                        .name(projects.getName())
//                        .surname(projects.getDescription())
//                        .build()).collect(Collectors.toList());
//
//  }
 private Project createProject(ProjectDTO project) {
     return Project.builder()
             .name(project.getName())
             .description(project.getDescription())
             .adminByWorkerId(project.getAdminByWorkerId())
             .tasks(project.getTasks())
             .build();
 }
 public void saveNewProject(ProjectDTO project) throws Exception {
        try {
            projectRepository.save(createProject(project));
        } catch (DataIntegrityViolationException e) {
            throw new Exception("saveNewProject exception");
        }
    }

    public void saveProject(Project project) throws Exception {
        try {
            projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("saveNewProject exception");
        }
    }

    public void deleteProject(Project project) throws Exception {
        try {
            projectRepository.delete(project);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("saveNewProject exception");
        }
    }



    public List<Project> getAllProjects(){
        return (List<Project>) projectRepository.findAll();
    }
}