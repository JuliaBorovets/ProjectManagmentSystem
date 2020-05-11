package com.training.demo.controllers;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.dto.SearchDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ProjectService;
import com.training.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequestMapping("/worker")
@Controller
public class WorkerController {

    private final ProjectService projectService;
    private final TaskService taskService;

    public WorkerController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @RequestMapping("/{id}")
    public String getProjectById(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Worker worker,
                                 @ModelAttribute("searchDTO") SearchDTO searchDTO) throws CanNotFoundException {

        Project project = projectService.findProjectById(id);
        model.addAttribute("searchDTO", searchDTO == null ? new SearchDTO() : searchDTO);
        model.addAttribute("project", project);
        model.addAttribute("tasks", taskService.findByProjectAndWorkers(project, worker));
        return "user/projects";
    }


    @GetMapping("/{id}/do_task/{task_id}")
    public String makeTaskDone(Model model, @PathVariable("task_id") Long id,
                               @PathVariable("id") Long projectId) throws CanNotFoundException {
        taskService.makeTaskDone(id);
        return "redirect:/worker/{id}";

    }
}
