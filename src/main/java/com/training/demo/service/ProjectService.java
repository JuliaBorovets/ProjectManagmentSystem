package com.training.demo.service;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.CreateException;
import com.training.demo.controllers.exception.DeleteException;
import com.training.demo.dto.AddWorkerDTO;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ProjectRepository;
import com.training.demo.repository.TaskRepository;
import com.training.demo.repository.WorkerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final WorkerService workerService;
    private final WorkerRepository workerRepository;
    private final TaskService taskService;

    public ProjectService(ProjectRepository projectRepository, WorkerService workerService,
                          WorkerRepository workerRepository, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.workerService = workerService;
        this.workerRepository = workerRepository;
        this.taskService = taskService;
    }

    public Project findProjectById(Long id) throws CanNotFoundException {
        return projectRepository.findById(id)
                .orElseThrow(() -> new CanNotFoundException("Немає такого проекту"));
    }

    public Project findProjectByName(String name) throws CanNotFoundException {
        return projectRepository.findByName(name)
                .orElseThrow(() -> new CanNotFoundException("Немає такого проекту"));
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

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {CanNotFoundException.class, DataIntegrityViolationException.class})
    public void deleteWorkerFromProject(Long projectId, Long workerId) throws CanNotFoundException {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new CanNotFoundException("can not find project"));
        Worker worker = workerService.findWorkerById(workerId);
        List<Project> workerProjects = worker.getProjects();
        List<Worker> workersList = project.getWorkers();

        workerProjects.remove(project);
        workerRepository.save(worker);

        workersList.remove(worker);
        projectRepository.save(project);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {CanNotFoundException.class, DataIntegrityViolationException.class})
    public void addWorkerToProject(AddWorkerDTO workerDTO, Long projectId) throws CanNotFoundException {
        Worker worker = workerRepository
                .findByIdAndLogin(workerDTO.getId(), workerDTO.getLogin())
                .orElseThrow(() -> new CanNotFoundException("can not find worker"));

        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new CanNotFoundException("can not fnd project"));

        List<Project> workerProjects = worker.getProjects();
        List<Worker> projectWorkers = project.getWorkers();

        workerProjects.add(project);
        projectWorkers.add(worker);

        workerRepository.save(worker);
        projectRepository.save(project);
    }

}
