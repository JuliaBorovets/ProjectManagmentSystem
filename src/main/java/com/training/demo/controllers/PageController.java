package com.training.demo.controllers;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
public class PageController {

    private final WorkerService workerService;
    @Autowired
    public PageController(WorkerService workerService) {
        this.workerService = workerService;
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

}
