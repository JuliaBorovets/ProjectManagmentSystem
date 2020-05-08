package com.training.demo.controllers;

import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Artifact;
import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import com.training.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class PageController {

    private final ProjectService projectService;
    private final WorkerService workerService;
    private final TaskService taskService;
    private final ArtifactService artifactService;

    @Autowired
    public PageController(ProjectService projectService, WorkerService workerService, TaskService taskService, ArtifactService artifactService) {
        this.projectService = projectService;
        this.workerService = workerService;
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
        getAllProjects(model);
        return "homePage";
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
    public String newUser(@ModelAttribute("newWorker") @Valid WorkerDTO worker) throws Exception {
        workerService.saveNewWorker(worker);
        return "redirect:/login";
    }



//    @GetMapping("/{login}")
//    public String myProjectsPage( @AuthenticationPrincipal Worker worker,Model model,
//                                  @PathVariable("login") String login) {
//        getProjectsByWorkerId(model,worker);
//        return "my_projects";
//    }

    @GetMapping("/search/{id}")
    public String searchProjects(Model model,
                                 @PathVariable("id") Long id) {
        getProjectById(model, id);
        return "search_proj";
    }


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


    private void getProjectById(Model model, Long id) {
        model.addAttribute("projectById", projectService.findProjectById(id));
    }


    private void getAllProjects(Model model) {
        model.addAttribute("project", projectService.getAllProjects());
    }

    private void saveProject(Project project) throws Exception {
        projectService.saveProject(project);
    }

    // тест на видалення проекту
    @GetMapping("/delete_test")
    public String projectsPage(Model model, @AuthenticationPrincipal Worker worker) {
        getAllProjects(model);
        return "user/index";
    }

    // видалення проекту
    @RequestMapping("/delete/{id}")
    public String deleteProject(Project project, @PathVariable("id") Long id) throws Exception {
        projectService.deleteProject(project);
        return "redirect:/delete_test";
    }


    private void addTask(Task task, Project project) {
        projectService.addTask(task, project);
    }

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

//    private List<Project> getProjectsByWorkerId(Model model, Worker worker) {
//        List<AssignmentDTO> assignments = assignmentService.findAllAssignByWorkerId(worker.getId());
//        List<Project> projects = new ArrayList<>();
//        for (int i = 0; i < assignments.size(); i++) {
//            Task task = assignments.get(i).getTask();
//            projects.add(task.getProject());
//        }
//        return projects;
//    }

    private List<WorkerDTO> getWorkers(Project project) {
        List<WorkerDTO> workersByProjectId = workerService.findWorkersByProjectId(project);
        return workersByProjectId;

    }

    @RequestMapping("/user_projects")
    public String getUserProjectsPage(Model model) {
        getAllProjects(model);
        return "user/projects";
    }

    @RequestMapping("/user_projects/{id}")
    public String getProjectsById(Model model, @PathVariable("id") Long id) {
        Project project = projectService.findProjectById(id);
        model.addAttribute("projectName", project.getName());
        return "user/projects";
    }
}
