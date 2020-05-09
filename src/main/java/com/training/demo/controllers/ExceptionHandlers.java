package com.training.demo.controllers;


import com.training.demo.controllers.exception.*;
import com.training.demo.dto.WorkerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


@Slf4j
@ControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleApplicationException() {
        log.error("global exception");
        ModelAndView modelAndView = new ModelAndView("index");
        modelAndView.addObject("error", true);
        return modelAndView;
    }


    @ExceptionHandler({org.springframework.validation.BindException.class, IllegalStateException.class})
    public String handleApplicationException(Model model) {
        log.error("registration exception. Binding result.");
        model.addAttribute("newWorker", new WorkerDTO());
        model.addAttribute("error", true);
        return "registration";
    }


    @ExceptionHandler(RegisterException.class)
    public String handleRegisterException(Model model) {
        log.error("registration exception. Such user already exists");
        model.addAttribute("newUser", new WorkerDTO());
        return "registration";
    }

    @ExceptionHandler(CreateException.class)
    public String handleCreateException(Model model) {
        log.error("Create Exception");
        model.addAttribute("error", true);
        return "redirect:/create?error";
    }

    @ExceptionHandler(UpdateException.class)
    public String handleUpdateException() {
        log.error("Update Exception");
        return "redirect:/home";
    }


    @ExceptionHandler(DeleteException.class)
    public String handleDeleteException() {
        log.error("Delete exception");
        return "redirect:/home";
    }

    @ExceptionHandler(CanNotFoundException.class)
    public String handleCanNotFoundException(Model model) {
        log.error("CanNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/home";
    }

}
