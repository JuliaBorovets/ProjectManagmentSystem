package com.training.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode

@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String deadline;

    @Column(columnDefinition = "boolean default false")
    private boolean isDone;

    @ManyToMany(mappedBy = "tasks")
    private List<Worker> workers;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "task")
    private List<Artifact> artifacts;
}
