package com.training.demo.service;

import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.repository.WorkerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.method.P;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;
    private PasswordEncoder passwordEncoder;
    final Pattern pattern = Pattern.compile("admin");

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;

    }

    public  List<WorkerDTO> findWorkersByProjectId(Long id) {
        return workerRepository.findByProjectId(id).stream()
                .map(worker -> WorkerDTO.builder()
                        .name(worker.getName())
                        .surname(worker.getSurname())
                .email(worker.getEmail())
                .login(worker.getLogin())
                .assignments(worker.getAssignments())
                .build()).collect(Collectors.toList());
    }

    public Worker findWorkerById(Long id){
        return workerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Немає такого працівника"));
    }

    public Worker findWorkerByLogin(String login){
        return workerRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Немає такого працівника"));
    }

    public List<Worker> getAllWorkers(){
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
                .password(passwordEncoder.encode(worker.getPassword()))
                .build();
    }


    private void createAdmin() throws Exception {
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
                .password(passwordEncoder.encode("root"))
                .build();

        try {
            workerRepository.save(admin);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Помилка створення адміна");
        }
    }

    public void addWorkerToProject(Worker worker, Project project) throws Exception{
        Worker updatedWorker = Worker.builder()
                .name(worker.getName())
                .surname(worker.getSurname())
                .login(worker.getLogin())
                .email(worker.getEmail())
                .password(worker.getPassword())
                .project(project)
                .build();
        try {
            workerRepository.save(updatedWorker);
        } catch (DataIntegrityViolationException e) {
            throw new Exception("Помилка додавання працівника");
        }

    }

//    private Long getAdminAccount() {
//        Worker admin = workerRepository.findByLogin("admin")
//                .orElseThrow(() -> new UsernameNotFoundException("Немає адміна"));
//
//        return admin.getId();
//    }
    public void saveNewWorker(WorkerDTO worker) throws Exception {
        try {
            workerRepository.save(createWorker(worker));
        } catch (DataIntegrityViolationException e) {
            throw new Exception("saveNewWorker exception");
        }
    }


}