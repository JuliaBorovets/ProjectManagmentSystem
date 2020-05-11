package com.training.demo.controllers;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.AddTaskDTO;
import com.training.demo.dto.AddWorkerDTO;
import com.training.demo.dto.ArtifactDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
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

@Slf4j
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final ArtifactService artifactService;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final WorkerService workerService;

    public AdminController(ArtifactService artifactService, ProjectService projectService, TaskService taskService,
                           WorkerService workerService) {
        this.artifactService = artifactService;
        this.projectService = projectService;
        this.taskService = taskService;
        this.workerService = workerService;
    }

    @RequestMapping({"/{id}", "/{id}/create_artifact", "/{id}/create_worker", "/{id}/create_task"})
    public String testAdmin(Model model, @PathVariable("id") Long id, @AuthenticationPrincipal Worker worker,
                            @ModelAttribute("newArtifact") ArtifactDTO artifactDTO,
                            @ModelAttribute("newWorker") AddWorkerDTO newWorker,
                            @ModelAttribute("newTask") AddTaskDTO newTask) throws CanNotFoundException {
        Project project = projectService.findProjectById(id);
        model.addAttribute("error", false);
        model.addAttribute("project", project);
        model.addAttribute("projects", projectService.getAllProjects());
        model.addAttribute("active_tasks", taskService.findActiveTasksByProject(project));
        model.addAttribute("artifacts", artifactService.findArtifactsByProjectId(id));
        model.addAttribute("workers", workerService.findWorkersByProjectId(project));
        return "admin/projects";
    }

    @RequestMapping("/{project}/delete_artifact/{id}")
    public String deleteArtifact(Model model, @PathVariable("id") Long id,
                                 @PathVariable("project") Long projectId) {

        artifactService.deleteArtifact(id);
        return "redirect:/admin/{project}";
    }

    @PostMapping("/{project}/create_artifact")
    public String newArtifact(@ModelAttribute("newArtifact") ArtifactDTO artifactDTO, Model model,
                              @PathVariable("project") Long projectId) throws Exception {

        Project project = projectService.findProjectById(projectId);
        model.addAttribute("project", project);
        artifactService.addArtifact(artifactDTO, project);
        return "redirect:/admin/{project}";
    }

    @RequestMapping("/{project}/delete_worker/{id}")
    public String deleteWorkerFromProject(Model model, @PathVariable("id") Long id,
                                          @PathVariable("project") Long projectId) throws CanNotFoundException {

        projectService.deleteWorkerFromProject(projectId, id);
        return "redirect:/admin/{project}";
    }

    @PostMapping("/{project}/create_worker")
    public String createNewWorkerToProject(@ModelAttribute("newWorker") AddWorkerDTO newWorker, Model model,
                                           @PathVariable("project") Long projectId) throws CanNotFoundException {

        addProjectInfo(model, projectId);
        try {
            projectService.addWorkerToProject(newWorker, projectId);
        } catch (CanNotFoundException e) {
            model.addAttribute("error", true);
            return "redirect:/admin/{project}";
        }
        return "redirect:/admin/{project}";
    }

    @PostMapping("/{project}/create_task")
    public String createNewTaskToProject(@ModelAttribute("newTask") AddTaskDTO newTask, Model model,
                                         @PathVariable("project") Long projectId) throws CanNotFoundException {

        addProjectInfo(model, projectId);

        Task task;
        try {
            task = taskService.saveNewTask(newTask, projectId);
        } catch (CreateException e) {
            model.addAttribute("error", true);
            log.error("errrror");
            return "redirect:/admin/{project}";
        }

        taskService.makeRelationship(newTask.getArtifacts(), newTask.getWorkers(), task.getId());
        return "redirect:/admin/{project}";
    }

    @RequestMapping("/{project}/delete_task/{id}")
    public String deleteTaskFromProject(Model model, @PathVariable("id") Long id,
                                        @PathVariable("project") Long projectId) throws CanNotFoundException {

        taskService.deleteTaskFromProject(id);
        return "redirect:/admin/{project}";
    }

    private void addProjectInfo(Model model, Long projectId) throws CanNotFoundException {
        Project project = projectService.findProjectById(projectId);
        model.addAttribute("project", project);
    }


}


