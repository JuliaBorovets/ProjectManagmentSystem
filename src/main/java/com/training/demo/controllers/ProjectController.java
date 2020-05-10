package com.training.demo.controllers;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.ProjectDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ArtifactService;
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
    private final ArtifactService artifactService;

    public ProjectController(ProjectService projectService, TaskService taskService, ArtifactService artifactService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.artifactService = artifactService;
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
    
    
    @GetMapping("/info")
    public String info( @AuthenticationPrincipal Worker worker,Model model) {
        model.addAttribute("user_info", worker);
        model.addAttribute("projects", projectService.findProjectsByWorker(worker));
        return "info";
    }

    @GetMapping("/done_tasks")
    public String done_tasks(@AuthenticationPrincipal Worker worker, Model model) {
        model.addAttribute("user_info", worker);
        //model.addAttribute("tasks", taskService.findDoneTasksByWorker(worker));
        //model.addAttribute("tasks", taskService.findDoneTasksByWorker((long) 1,worker));
       // model.addAttribute("tasks", taskService.findDoneTasksByWorker(worker));
        return "done_tasks";
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
    
    @GetMapping("/search/{id}")
    public String searchProjects(Model model,@PathVariable("id") Long id) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("projectName",project.getName());
        model.addAttribute("tasks", taskService.getAllTasks());
        //model.addAttribute("project", project);
        return "user/projects";
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

