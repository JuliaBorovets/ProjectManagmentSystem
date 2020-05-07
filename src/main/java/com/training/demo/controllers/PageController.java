package com.training.demo.controllers;

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
import com.training.demo.dto.*;

import javax.validation.Valid;
import java.util.*;

@Controller
public class PageController {

    private final ProjectService projectService;
    private final WorkerService workerService;
    private final AssignmentService assignmentService;
    private final TaskService taskService;
    private final ArtifactService artifactService;

    @Autowired
    public PageController(ProjectService projectService, WorkerService workerService, AssignmentService assignmentService, TaskService taskService, ArtifactService artifactService) {
        this.projectService = projectService;
        this.workerService = workerService;
        this.assignmentService = assignmentService;
        this.taskService = taskService;
        this.artifactService = artifactService;
    }

    @RequestMapping("/home")
    public String mainPage(Model model) {
        getAllProjects(model);
        return "index";
    }

    @RequestMapping("/log-in")
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
        return "redirect:/log-in";
    }

//    @RequestMapping("/1")
//    public String mainPage1(Model model) {
//
//        model.addAttribute("worker", workerService.getAllWorkers());
//
//        return "index1";
//    }
////
//    @ResponseBody
//    @RequestMapping("/2")
//    public List<WorkerDTO> hi(@RequestParam Project project){
//        return getWorkers(project);
//    }
//
//    @ResponseBody
//    @RequestMapping("/3")
//    public String hi(){
//        return "hi ";
//    }




    @GetMapping("/{login}")
    public String myProjectsPage( @AuthenticationPrincipal Worker worker,Model model,
                                  @PathVariable("login") String login) {
        getProjectsByWorkerId(model,worker);
        return "my_projects";
    }

    @GetMapping("/search/{id}")
    public String searchProjects(Model model,
                                 @PathVariable("id") Long id){
         getProjectById(model,id);
         return "search_proj";
    }


    @GetMapping("/{userName}/{projId}")
    public String crudProject(@AuthenticationPrincipal Worker worker, Model model,
                            @PathVariable("userName") String username, @PathVariable("projId") Long projId) throws Exception {
        getProjectsByWorkerId(model, worker);
        getProjectById(model, projId);
        Project currentProject = projectService.findProjectById(projId);
        if (currentProject.getAdminByWorkerId().equals(worker.getId())) {
            //admin page view
            List<WorkerDTO> workers = getWorkers(projectService.findProjectById(projId));
            model.addAttribute("workers",workers);


            //saveProject(currentProject);
            return "projectCrudForAdmin";
        } else {
            //worker page view
            List<Task> allTasks = taskService.getAllTasks();
            List<Task> projectTasks = new ArrayList<>();
            List<Task> workerTasks = new ArrayList<>();
            for (int i = 0; i <= allTasks.size(); i++) {
                if (currentProject.getId().equals(allTasks.get(i).getProject().getId())) {
                    projectTasks.add(allTasks.get(i));
                }}
            currentProject.setTasks(projectTasks);

            List<AssignmentDTO> workerAssign = assignmentService.findAllAssignByWorkerId(worker.getId());
            for (int i = 0; i <= workerAssign.size(); i++) {
                for (int j = 0; j <= projectTasks.size(); j++) {
                    if (workerAssign.get(i).getTask().getId().equals(projectTasks.get(j).getId())) {
                        workerTasks.add(projectTasks.get(j));
                    }}}
            model.addAttribute("tasks", workerTasks);
            return "projectCrudForWorker";
        }
    }


    private void getProjectById (Model model, Long id){
        model.addAttribute("projectById", projectService.findProjectById(id));
    }



    private void getAllProjects (Model model){
        model.addAttribute("project", projectService.getAllProjects());
    }

    private void saveProject(Project project) throws Exception {
        projectService.saveProject(project);
    }

    private void deleteProject(Project project) throws Exception {
        projectService.deleteProject(project);
    }

    private void addTask(Task task,Project project){
        taskService.addTask(task,project);
    }

    private void addWorkerToProject(Worker worker, Project project) throws Exception {
         workerService.addWorkerToProject(worker,project);
    }

    private void addArtifact(Artifact artifact) throws Exception {
        artifactService.addArtifact(artifact);
    }

    private void deleteArtifact(Artifact artifact) throws Exception {
        artifactService.deleteArtifact(artifact);
    }


    private void getWorkersByProjectId (Model model, Project project){
        List<WorkerDTO> workers = workerService.findWorkersByProjectId(project.getId());
        model.addAttribute("workers", workers);
    }

    private List<Project> getProjectsByWorkerId (Model model, Worker worker){
        List<AssignmentDTO> assignments = assignmentService.findAllAssignByWorkerId(worker.getId());
        List<Project> projects = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            Task task = assignments.get(i).getTask();
            projects.add(task.getProject());
        }
        return projects;
    }

    private List<WorkerDTO> getWorkers(Project project) {
        List<WorkerDTO> workersByProjectId = workerService.findWorkersByProjectId(project.getId());
        return workersByProjectId;

    }


}
