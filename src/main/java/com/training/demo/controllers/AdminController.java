package com.training.demo.controllers;

import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.service.ArtifactService;
import com.training.demo.service.ProjectService;
import com.training.demo.service.TaskService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/admin")
@Controller
public class AdminController {
    private final ArtifactService artifactService;
    private final ProjectService projectService;
    private final TaskService taskService;

    public AdminController(ArtifactService artifactService, ProjectService projectService, TaskService taskService) {
        this.artifactService = artifactService;
        this.projectService = projectService;
        this.taskService = taskService;
    }


    @RequestMapping("/{id}")
    public String testAdmin(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Worker worker) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("project", project);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("tasks", taskService.findByProjectAndWorkers(project, worker));
        model.addAttribute("artifacts", artifactService.findArtifactsByProjectId(id));
        return "admin/projects";
    }

    @RequestMapping("/{project}/delete_artifact/{id}")
    public String deleteArtifact(Model model, @PathVariable("id") Long id,
                                 @PathVariable("project") Long projectId) {

        artifactService.deleteArtifact(id);
        return "redirect:/admin/{project}";
    }

}
