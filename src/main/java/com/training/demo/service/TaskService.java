package com.training.demo.service;

import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.repository.TaskRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class TaskService {
    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
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
        return tasks;

    }
}

