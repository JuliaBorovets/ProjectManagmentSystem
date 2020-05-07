package com.training.demo.service;

import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;


    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task findTaskById(Long id){
        return taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Can not find task by id"));
    }
    public List<Task> getAllTasks(){
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

    public void addTask(Task task, Project project){
        project.addTask(task);
    }

}

