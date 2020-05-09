package com.training.demo.service;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.controllers.exception.DeleteException;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ProjectRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

//import org.springframework.security.core.parameters.P;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findProjectById(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Немає такого проекту"));
    }

    public Project findProjectByName(String name) {
        return projectRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Немає такого проекту"));
    }

    public List<ProjectDTO> findProjectsByWorker(Worker worker) {
        return projectRepository.findByWorkers(worker).stream()
                .map(projects -> ProjectDTO.builder()
                        .id(projects.getId())
                        .name(projects.getName())
                        .description(projects.getDescription())
                        .build()).collect(Collectors.toList());

    }


    private Project createProject(ProjectDTO project) {
        return Project.builder()
                .name(project.getName())
                .description(project.getDescription())
                .tasks(project.getTasks())
                .build();
    }

    public void saveNewProject(ProjectDTO project) throws CreateException {
        try {
            projectRepository.save(createProject(project));
        } catch (DataIntegrityViolationException e) {
            throw new CreateException("saveNewProject exception");
        }
    }

    public void saveProject(Project project) throws CreateException {
        try {
            projectRepository.save(project);
        } catch (DataIntegrityViolationException e) {
            throw new CreateException("saveProject exception");
        }
    }

    public void deleteProject(Project project) throws DeleteException {
        try {
            projectRepository.delete(project);
        } catch (DataIntegrityViolationException e) {
            throw new DeleteException("saveNewProject exception");
        }
    }


    public List<Project> getAllProjects() {
        return (List<Project>) projectRepository.findAll();
    }

    public void addTask(Task task, Project project) {
        Project project1 = projectRepository.findById(project.getId()).orElseThrow(() -> new RuntimeException("no project"));
        List<Task> tasks = project1.getTasks();
        tasks.add(task);
    }

}
