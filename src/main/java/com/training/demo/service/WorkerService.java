package com.training.demo.service;

import com.training.demo.controllers.exception.CreateException;
import com.training.demo.controllers.exception.UpdateException;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.repository.WorkerRepository;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkerService implements UserDetailsService {

    private final WorkerRepository workerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    final Pattern pattern = Pattern.compile("admin");

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;

    }

    public List<WorkerDTO> findWorkersByProjectId(Project project) {
        return workerRepository.findWorkersByProjects(project).stream()
                .map(worker -> WorkerDTO.builder()
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .email(worker.getEmail())
                        .login(worker.getLogin())
                        .tasks(worker.getTasks())
                        .build()).collect(Collectors.toList());
    }

    public Worker findWorkerById(Long id) {
        return workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Немає такого працівника"));
    }

    public Worker findWorkerByLogin(String login) {
        return workerRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Немає такого працівника"));
    }

    public List<Worker> getAllWorkers() {
        return (List<Worker>) workerRepository.findAll();
    }

//    public void saveNewWorker(WorkerDTO worker) throws Exception {
//        try {
//            workerRepository.save(createWorker(worker));
//        } catch (DataIntegrityViolationException e) {
//            throw new Exception("Помилка створення працівника");
//        }
//    }

    private Worker createWorker(WorkerDTO worker) {
        return Worker.builder()
                .name(worker.getName())
                .surname(worker.getSurname())
                .login(worker.getLogin())
                .email(worker.getEmail())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .password(passwordEncoder.encode(worker.getPassword()))
                .build();
    }


    private void createAdmin() throws CreateException {
//        if (!pattern.matcher("admin").matches()) {
//            throw new IllegalArgumentException("Invalid String");
//        }
        if (workerRepository.findByLogin("admin").isPresent()) {
            return;
        }

        Worker admin = Worker.builder()
                .name("Адмін")
                .surname("Адмін")
                .login("admin")
                .email("admin@gmail.com")
                // .password(passwordEncoder.encode("root"))
                .build();

        try {
            workerRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new CreateException("Помилка створення адміна");
        }
    }

    public void addWorkerToProject(Worker worker, Project project) throws UpdateException {
        Worker updatedWorker = Worker.builder()
                .name(worker.getName())
                .surname(worker.getSurname())
                .login(worker.getLogin())
                .email(worker.getEmail())
                .password(worker.getPassword())
//                .project(project)
                .build();
        try {
            workerRepository.save(updatedWorker);
        } catch (DataIntegrityViolationException e) {
            throw new UpdateException("Помилка додавання працівника");
        }

    }

    //    private Long getAdminAccount() {
//        Worker admin = workerRepository.findByLogin("admin")
//                .orElseThrow(() -> new UsernameNotFoundException("Немає адміна"));
//
//        return admin.getId();
//    }
    public void saveNewWorker(WorkerDTO worker) throws CreateException {
        try {
            workerRepository.save(createWorker(worker));
        } catch (DataIntegrityViolationException e) {
            throw new CreateException("saveNewWorker exception");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.error("loading user with login " + login);
        log.error(login);
        return workerRepository.findByLogin(login).orElseThrow(() -> new UsernameNotFoundException(login));
    }
}
