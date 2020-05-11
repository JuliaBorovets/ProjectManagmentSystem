package com.training.demo.service;

import com.training.demo.controllers.exception.CanNotFoundException;
import com.training.demo.controllers.exception.CreateException;
import com.training.demo.controllers.util.ProjectPasswordEncoder;
import com.training.demo.dto.WorkerDTO;
import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import com.training.demo.repository.WorkerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkerService implements UserDetailsService {

    private final WorkerRepository workerRepository;
    private final ProjectPasswordEncoder passwordEncoder;

    public WorkerService(WorkerRepository workerRepository, ProjectPasswordEncoder passwordEncoder) {
        this.workerRepository = workerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<WorkerDTO> findWorkersByProjectId(Project project) {
        return workerRepository.findWorkersByProjects(project).stream()
                .map(worker -> WorkerDTO.builder()
                        .id(worker.getId())
                        .name(worker.getName())
                        .surname(worker.getSurname())
                        .email(worker.getEmail())
                        .login(worker.getLogin())
                        .tasks(worker.getTasks())
                        .build()).collect(Collectors.toList());
    }

    public Worker findWorkerById(Long id) throws CanNotFoundException {
        return workerRepository.findById(id)
                .orElseThrow(() -> new CanNotFoundException("can nor find worker with id = " + id));
    }

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

    public void saveNewWorker(WorkerDTO worker) throws CreateException {
        try {
            workerRepository.save(createWorker(worker));
        } catch (DataIntegrityViolationException e) {
            throw new CreateException("saveNewWorker exception");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        log.info("loading user with login " + login);
        log.info(login);
        return workerRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("can not find user with login = " + login));
    }


    public boolean isAdmin(Worker worker, Project project) {
        return project.getAdmin().getId().equals(worker.getId());
    }
}
