package com.training.demo.service;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.controllers.exception.DeleteException;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ProjectRepository;
import com.training.demo.repository.WorkerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

//import org.springframework.security.core.parameters.P;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkerService workerService;
    private final WorkerRepository workerRepository;

    public ProjectService(ProjectRepository projectRepository, WorkerService workerService, WorkerRepository workerRepository) {
        this.projectRepository = projectRepository;
        this.workerService = workerService;
        this.workerRepository = workerRepository;
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
        return projectRepository.findByWorkersOrAdmin(worker, worker).stream()
                .map(projects -> ProjectDTO.builder()
                        .id(projects.getId())
                        .name(projects.getName())
                        .description(projects.getDescription())
                        .build()).collect(Collectors.toList());

    }


    private Project createProject(ProjectDTO project, Worker worker) {

        return Project.builder()
                .name(project.getName())
                .admin(worker)
                .description(project.getDescription())
                .tasks(project.getTasks())
                .build();
    }

    public void saveNewProject(ProjectDTO project, Worker admin) throws CreateException {
        try {
            projectRepository.save(createProject(project, admin));
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

    @Transactional
    public void deleteWorkerFromProject(Long projectId, Long workerId) {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new RuntimeException("can not find project"));
        Worker worker = workerService.findWorkerById(workerId);
        List<Project> workerProjects = worker.getProjects();
        List<Worker> workersList = project.getWorkers();

        workerProjects.remove(project);
        workerRepository.save(worker);

        workersList.remove(worker);
        projectRepository.save(project);
    }

}
