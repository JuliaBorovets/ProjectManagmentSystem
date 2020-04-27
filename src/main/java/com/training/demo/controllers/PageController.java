package com.training.demo.controllers;

import com.training.demo.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    private final ProjectService projectService;

    public PageController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @RequestMapping("/")
    public String mainPage(Model model) {

        model.addAttribute("project", projectService.getAllProjects());

        return "index";
    }
}
