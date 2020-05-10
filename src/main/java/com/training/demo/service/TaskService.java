package com.training.demo.service;

import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.ProjectRepository;
import com.training.demo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;


    public TaskService(TaskRepository taskRepository, ProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public Task findTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find task by id"));
    }

    public List<Task> getAllTasks() {
        return (List<Task>) taskRepository.findAll();
    }

//    private Project createProject(ProjectDTO project) {
//        return Project.builder()
//                .name(project.getName())
//                .description(project.getDescription())
//                .adminByWorkerId(project.getAdminByWorkerId())
//                .tasks(project.getTasks())
//                .build();
//    }

//    public void addTask(Task task, Project project) {
//        Project project1 = .findById(task.getId()).orElseThrow(() -> new RuntimeException("no task"));
//        project.addTask(task);
//    }

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

        log.error(tasks.toString());
        return tasks.stream()
                .filter(Task::isDone)
                .sorted(Comparator.comparing(Task::getId))
                .collect(Collectors.toList());
    }


//
//    public List<Task> findDoneTasksByWorker(Long id,Worker worker) {
//        List<Project> projects = (List<Project>) projectRepository.findAll();
//        //Iterable<Project> project = projectRepository.findAll();
//
//        List<Task> tasks = (List<Task>) taskRepository.findAll();
//        System.out.println(tasks);
//        //for (int i=0; i<projects.size(); i++){
//        //tasks.addAll(taskRepository.findByProjectAndWorkers(projects.get(i),worker));}
//        tasks.addAll(taskRepository.findByProjectAndWorkers(projects.get(0),worker));
//
//        log.error(tasks.toString());
//        return tasks.stream()
//                .filter(Task::isDone)
//                .sorted(Comparator.comparing(Task::getId))
//                .collect(Collectors.toList());
//    }

    public void makeTaskDone(Long id) {
        Task task = taskRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("no task found"));
        task.setDone(true);
        taskRepository.save(task);
    }

}

