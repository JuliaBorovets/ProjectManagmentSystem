package com.training.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @ManyToMany(mappedBy = "projects")
    private List<Worker> workers;

    @ManyToOne
    private Worker admin;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "project")
    private List<Artifact> artifacts;


    public void deleteWorkerFromProject(Worker worker) {
        workers.remove(worker);
        worker.getProjects().remove(this);
    }

    @PreRemove
    public void deleteProject() {
        this.getWorkers().forEach(t -> t.getProjects().remove(this));
    }
}
