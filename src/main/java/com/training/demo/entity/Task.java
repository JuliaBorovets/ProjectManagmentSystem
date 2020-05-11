package com.training.demo.entity;

import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private LocalDate deadline;

    @Column(columnDefinition = "boolean default false")
    private boolean done;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Worker> workers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToMany(mappedBy = "tasks", fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @Fetch(value = FetchMode.SUBSELECT)
    private List<Artifact> artifacts = new ArrayList<>();


    public void deleteWorker(Worker worker) {
        workers.remove(worker);
        worker.getTasks().remove(this);
    }


    public void addWorker(Worker worker) {
        workers.add(worker);
        worker.getTasks().add(this);
    }

    public void addArtifacts(Artifact artifact) {
        artifacts.add(artifact);
        artifact.getTasks().add(this);
    }


    public void setDone() {
        this.done = true;
    }

    @PreRemove
    public void removeAllWorkers() {
        workers.forEach(t -> t.getTasks().remove(this));
        artifacts.forEach(t -> t.getTasks().remove(this));
    }
}

