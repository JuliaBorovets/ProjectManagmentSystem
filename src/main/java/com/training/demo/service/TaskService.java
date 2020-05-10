package com.training.demo.service;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.AddTaskDTO;
import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ArtifactRepository;
import com.training.demo.repository.ProjectRepository;
import com.training.demo.repository.TaskRepository;
import com.training.demo.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final ArtifactRepository artifactRepository;
    private final WorkerRepository workerRepository;

    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository,
                       ArtifactRepository artifactRepository, WorkerRepository workerRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.artifactRepository = artifactRepository;
        this.workerRepository = workerRepository;
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find task by id"));
    }

    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }


    public List<Task> findByProjectAndWorkers(Project project, Worker worker) {
        List<Task> tasks = taskRepository.findByProjectAndWorkers(project, worker);

        log.error(tasks.toString());
        return tasks.stream()
                .filter(t -> !t.isDone())
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());

    }

    public List<AddTaskDTO> findActiveTasksByProject(Project project) {
        return taskRepository.findByProject(project)
                .stream()
                .filter(t -> !t.isDone())
                .sorted(Comparator.comparing(Task::getId))
                .map(AddTaskDTO::new)
                .collect(Collectors.toList());
    }


    public void makeTaskDone(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("no task found"));
        task.setDone(true);
        taskRepository.save(task);
    }

    public Task createTask(AddTaskDTO taskDTO, Long projectId) throws CanNotFoundException {
        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new CanNotFoundException("can not found project with id = " + projectId));

        return Task.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .deadline(taskDTO.getDeadline())
                .artifacts(getArtifactsFromInput(taskDTO.getArtifacts()))
                .project(project)
                .workers(getWorkersFromInput(taskDTO.getWorkers()))
                .done(false)
                .build();
    }

    public void saveNewTask(AddTaskDTO taskDTO, Long projectId) throws CreateException {
        try {
            taskRepository.save(createTask(taskDTO, projectId));
        } catch (DataIntegrityViolationException | CanNotFoundException e) {
            throw new CreateException("can not save task to project with id = " + projectId);
        }
    }

    private List<Artifact> getArtifactsFromInput(String input) {
        return Arrays.stream(input.split("\\s*,\\s*"))
                .map(Long::parseLong)
                .map(t -> artifactRepository.findById(t)
                        .orElseThrow(() -> new RuntimeException("can not find artifact with id = " + t)))
                .collect(Collectors.toList());

    }

    private List<Worker> getWorkersFromInput(String input) {
        return Arrays.stream(input.split("\\s*,\\s*"))
                .map(Long::parseLong)
                .map(t -> workerRepository.findById(t)
                        .orElseThrow(() -> new RuntimeException("can not find worker with id = " + t)))
                .collect(Collectors.toList());
    }

    public void deleteTaskFromProject(Long taskID) {
        Task task = taskRepository.findById(taskID).orElseThrow(() -> new RuntimeException("can not find"));
        List<Worker> workers = task.getWorkers();

        while (!(workers.size() == 0)) {
            deleteTask(workers.get(workers.size() - 1), task);
        }

        taskRepository.delete(task);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {CanNotFoundException.class, DataIntegrityViolationException.class})
    public void deleteTask(Worker worker, Task task) {
        try {
            deleteWorkersFromTasks(task, worker);
            deleteTaskFromWorker(task, worker);
        } catch (ConcurrentModificationException e) {
            log.error("concurrent exception");
        }
    }

    public void deleteTaskFromWorker(Task task, Worker worker) {

        List<Task> workerTasks = worker.getTasks();
        workerTasks.remove(task);
        taskRepository.save(task);
    }

    public void deleteWorkersFromTasks(Task task, Worker worker) {

        List<Worker> taskWorkers = task.getWorkers();
        taskWorkers.remove(worker);
        workerRepository.save(worker);
    }

}