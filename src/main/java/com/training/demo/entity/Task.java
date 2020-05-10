package com.training.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
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

    @ManyToMany(mappedBy = "tasks")
    private List<Worker> workers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @OneToMany(mappedBy = "task", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<Artifact> artifacts;
}
