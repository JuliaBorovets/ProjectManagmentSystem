package com.training.demo.controllers;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.dto.SearchDTO;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ArtifactService;
import com.training.demo.service.ProjectService;
import com.training.demo.service.TaskService;
import com.training.demo.service.WorkerService;
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
    private final WorkerService workerService;

    public ProjectController(ProjectService projectService, TaskService taskService, WorkerService workerService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.workerService = workerService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {
        getAllProjects(model);
        return "index";
    }

    @GetMapping("/home")
    public String mainPage(Model model, @AuthenticationPrincipal Worker worker,
                           @ModelAttribute("searchDTO") SearchDTO searchDTO) {

        model.addAttribute("searchDTO", searchDTO == null ? new SearchDTO() : searchDTO);
        model.addAttribute("projects", projectService.findProjectsByWorker(worker));
        return "homePage";
    }
    
    
    @GetMapping("/info")
    public String info(@AuthenticationPrincipal Worker worker, Model model,
                       @ModelAttribute("searchDTO") SearchDTO searchDTO) throws CanNotFoundException {
        model.addAttribute("searchDTO", searchDTO == null ? new SearchDTO() : searchDTO);
        model.addAttribute("user_info", worker);
        model.addAttribute("projects", projectService.findProjectsByWorker(worker));
        model.addAttribute("tasks", taskService.findDoneTasksByWorker(worker));
        return "info";
    }

    private void getAllProjects(Model model) {
        model.addAttribute("project", projectService.getAllProjects());
    }


    @GetMapping("/done_tasks")
    public String done_tasks(@AuthenticationPrincipal Worker worker, Model model,
                             @ModelAttribute("searchDTO") SearchDTO searchDTO) throws CanNotFoundException {

        model.addAttribute("searchDTO", searchDTO == null ? new SearchDTO() : searchDTO);
        model.addAttribute("user_info", worker);
        model.addAttribute("tasks", taskService.findDoneTasksByWorker(worker));
        return "done_tasks";
    }

    @GetMapping("/search")
    public String searchProjects(@ModelAttribute("searchDTO") SearchDTO searchDTO, Model model) {
        model.addAttribute("searchDTO", searchDTO == null ? new SearchDTO() : searchDTO);
        return "homePage";
    }

    @PostMapping("/search")
    public String searchProjectByID(@ModelAttribute("searchDTO") SearchDTO searchDTO,
                                    @AuthenticationPrincipal Worker worker) throws CanNotFoundException {
        Project project = projectService.findProjectById(searchDTO.getSearchID());
        return "redirect:project_info/" + project.getId();
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


    @RequestMapping("/project_info/{id}")
    public String projectInfoPage(@AuthenticationPrincipal Worker worker, @PathVariable("id") Long projectID)
            throws CanNotFoundException {

        Project project = projectService.findProjectById(projectID);
        if (workerService.isAdmin(worker, project)) {
            return "redirect:/admin/{id}";
        } else {
            return "redirect:/worker/{id}";
        }

    }
}

