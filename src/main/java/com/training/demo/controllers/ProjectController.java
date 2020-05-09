package com.training.demo.controllers;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ProjectService;
import com.training.demo.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class ProjectController {
    private final ProjectService projectService;
    private final TaskService taskService;

    public ProjectController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        getAllProjects(model);
        return "index";
    }

    @GetMapping("/home")
    public String mainPage(Model model, @AuthenticationPrincipal Worker worker) {
        model.addAttribute("projects", projectService.findProjectsByWorker(worker));
        return "homePage";
    }

    @GetMapping("/search/{id}")
    public String searchProjects(Model model,
                                 @PathVariable("id") Long id) {
        getProjectById(model, id);
        return "search_proj";
    }


    private void getProjectById(Model model, Long id) {
        model.addAttribute("projectById", projectService.findProjectById(id));
    }


    private void getAllProjects(Model model) {
        model.addAttribute("project", projectService.getAllProjects());
    }

    private void saveProject(Project project) throws Exception {
        projectService.saveProject(project);
    }


//    // видалення проекту
//    @RequestMapping("/delete/{id}")
//    public String deleteProject(Project project, @PathVariable("id") Long id) throws Exception {
//        projectService.deleteProject(project);
//        return "redirect:/home";
//    }

    @RequestMapping("/user_projects/{id}")
    public String getProjectById(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Worker worker) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("projectName", project.getName());
        model.addAttribute("tasks", taskService.findByProjectAndWorkers(project, worker));
        return "user/projects";
    }

    @GetMapping("/do_task/{id}")
    public String makeTaskDone(Model model, @PathVariable("id") Long id) {
        taskService.makeTaskDone(id);
        return "redirect:/home";
    }

    @GetMapping("/create")
    public String createProject(@ModelAttribute("newProject") ProjectDTO projectDTO, Model model) {

        model.addAttribute("newProject", projectDTO == null ? new ProjectDTO() : projectDTO);
        return "new_project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute("newProject") @Valid ProjectDTO projectDTO,
                                @AuthenticationPrincipal Worker worker) throws CreateException {
        projectService.saveNewProject(projectDTO, worker);
        return "redirect:/home";
    }

}

