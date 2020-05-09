package com.training.demo.controllers;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class PageController {

    private final WorkerService workerService;
    private final ArtifactService artifactService;

    @Autowired
    public PageController(WorkerService workerService, ArtifactService artifactService) {
        this.workerService = workerService;
        this.artifactService = artifactService;
    }


    @RequestMapping("/success")
    public String localRedirect() {
        return "redirect:/home";
    }

    @RequestMapping("/login")
    public String loginPage() {

        return "login";
    }


    @GetMapping("/reg")
    public String registerUser(@ModelAttribute("newWorker") WorkerDTO worker, Model model) {

        model.addAttribute("newWorker", worker == null ? new WorkerDTO() : worker);
        return "registration";
    }

    @PostMapping("/reg")
    public String newUser(@ModelAttribute("newWorker") @Valid WorkerDTO worker) throws CreateException {
        workerService.saveNewWorker(worker);
        return "redirect:/login";
    }


//    @GetMapping("/{login}")
//    public String myProjectsPage( @AuthenticationPrincipal Worker worker,Model model,
//                                  @PathVariable("login") String login) {
//        getProjectsByWorkerId(model,worker);
//        return "my_projects";
//    }


//    @GetMapping("/{userName}/{projId}")
//    public String crudProject(@AuthenticationPrincipal Worker worker, Model model,
//                            @PathVariable("userName") String username, @PathVariable("projId") Long projId) throws Exception {
//        getProjectsByWorkerId(model, worker);
//        getProjectById(model, projId);
//        Project currentProject = projectService.findProjectById(projId);
//        if (currentProject.getAdminByWorkerId().equals(worker.getId())) {
//            //admin page view
//            List<WorkerDTO> workers = getWorkers(projectService.findProjectById(projId));
//            model.addAttribute("workers",workers);
//
//
//            //saveProject(currentProject);
//            return "projectCrudForAdmin";
//        } else {
//            //worker page view
//            List<Task> allTasks = taskService.getAllTasks();
//            List<Task> projectTasks = new ArrayList<>();
//            List<Task> workerTasks = new ArrayList<>();
//            for (int i = 0; i <= allTasks.size(); i++) {
//                if (currentProject.getId().equals(allTasks.get(i).getProject().getId())) {
//                    projectTasks.add(allTasks.get(i));
//                }}
//            currentProject.setTasks(projectTasks);
//
//            List<AssignmentDTO> workerAssign = assignmentService.findAllAssignByWorkerId(worker.getId());
//            for (int i = 0; i <= workerAssign.size(); i++) {
//                for (int j = 0; j <= projectTasks.size(); j++) {
//                    if (workerAssign.get(i).getTask().getId().equals(projectTasks.get(j).getId())) {
//                        workerTasks.add(projectTasks.get(j));
//                    }}}
//            model.addAttribute("tasks", workerTasks);
//            return "projectCrudForWorker";
//        }
//    }


//    private void addTask(Task task, Project project) {
//        projectService.addTask(task, project);
//    }

    private void addWorkerToProject(Worker worker, Project project) throws Exception {
        workerService.addWorkerToProject(worker, project);
    }

    private void addArtifact(Artifact artifact) throws Exception {
        artifactService.addArtifact(artifact);
    }

    private void deleteArtifact(Artifact artifact) throws Exception {
        artifactService.deleteArtifact(artifact);
    }


    private void getWorkersByProjectId(Model model, Project project) {
        List<WorkerDTO> workers = workerService.findWorkersByProjectId(project);
        model.addAttribute("workers", workers);
    }


    private List<WorkerDTO> getWorkers(Project project) {
        List<WorkerDTO> workersByProjectId = workerService.findWorkersByProjectId(project);
        return workersByProjectId;
    }

}
