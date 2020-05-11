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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    public List<Task> findDoneTasksByWorker(Worker worker) {

        List<Task> tasks = worker.getTasks();
        return tasks.stream()
                .filter(Task::isDone)
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }

    public List<AddTaskDTO> findActiveTasksByProject(Project project) {

        return taskRepository.findByProject(project)
                .stream()
                .filter(t -> !t.isDone())
                .sorted(Comparator.comparing(Task::getId).reversed())
                .map(AddTaskDTO::new)
                .collect(Collectors.toList());
    }


    public void makeTaskDone(Long id) {

        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("no task found"));
        task.setDone();
        taskRepository.save(task);
    }

    public Task saveNewTask(AddTaskDTO taskDTO, Long projectId) throws CreateException {

        try {
            Task task = createTask(taskDTO, projectId);
            taskRepository.save(task);
            return task;
        } catch (DataIntegrityViolationException | CanNotFoundException e) {
            throw new CreateException("can not save task to project with id = " + projectId);
        }
    }

    public Task createTask(AddTaskDTO taskDTO, Long projectId) throws CanNotFoundException {

        Project project = projectRepository
                .findById(projectId)
                .orElseThrow(() -> new CanNotFoundException("can not found project with id = " + projectId));

        return Task.builder()
                .name(taskDTO.getName())
                .description(taskDTO.getDescription())
                .deadline(convertToLocalDate(taskDTO.getDeadline()).plusDays(1))
                .project(project)
                .done(false)
                .build();

    }

    private LocalDate convertToLocalDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d").withLocale(LocaleContextHolder.getLocale());
        return LocalDate.parse(date, formatter);
    }

    /**
     * makes relationship task-workers, task-artifacts
     *
     * @param artifacts - input artifacts from user
     * @param workers   - input workers from user
     * @param taskId    - id of task
     */

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {CanNotFoundException.class, DataIntegrityViolationException.class})
    public void makeRelationship(String artifacts, String workers, Long taskId) throws CanNotFoundException {

        Task task = taskRepository
                .findById(taskId)
                .orElseThrow(() -> new CanNotFoundException("can not found task with id = " + taskId));
        getArtifactsFromInput(artifacts, task);
        getWorkersFromInput(workers, task);
    }

    /**
     * add relationship task-artifacts from user input
     *
     * @param input - input string with artifacts from user
     * @param task  - task to add artifacts
     */
    private void getArtifactsFromInput(String input, Task task) {

        List<Artifact> artifacts = Arrays.stream(input.split("\\s*,\\s*"))
                .map(Long::parseLong)
                .map(t -> artifactRepository.findById(t)
                        .orElseThrow(() -> new RuntimeException("can not find artifact with id = " + t)))
                .collect(Collectors.toList());

        artifacts.forEach(task::addArtifacts);
    }

    /**
     * add relationship task-worker from user input
     *
     * @param input - input string with workers from user
     * @param task  - task to add workers
     */
    private void getWorkersFromInput(String input, Task task) {

        List<Worker> workers = Arrays.stream(input.split("\\s*,\\s*"))
                .map(Long::parseLong)
                .map(t -> workerRepository.findById(t)
                        .orElseThrow(() -> new RuntimeException("can not find worker with id = " + t)))
                .collect(Collectors.toList());

        workers.forEach(task::addWorker);
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {CanNotFoundException.class, DataIntegrityViolationException.class})
    public void deleteTaskFromProject(Long taskID) {

        Task task = taskRepository
                .findById(taskID)
                .orElseThrow(() -> new RuntimeException("can not find"));
        taskRepository.delete(task);
    }

}
