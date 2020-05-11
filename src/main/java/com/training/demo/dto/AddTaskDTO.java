package com.training.demo.dto;

import com.training.demo.entity.Artifact;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.stream.Collectors;

@Slf4j
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddTaskDTO {

    private Long id;

    private String name;

    private String description;

    private String deadline;

    private String workers;

    private String artifacts;

    public AddTaskDTO(Task task) {
        this.id = task.getId();
        this.name = task.getName();
        this.description = task.getDescription();
//        this.deadline = task.getDeadline();
        this.workers = task.getWorkers()
                .stream()
                .map(Worker::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        this.artifacts = task.getArtifacts()
                .stream()
                .map(Artifact::getId)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }
}
