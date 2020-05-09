package com.training.demo.dto;


import com.training.demo.entity.Project;
import com.training.demo.entity.Worker;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.util.List;

import static com.training.demo.dto.Regex.deadlineRegex;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TaskDTO {
    Long id;
    String name;
    String description;

    @Pattern(regexp = deadlineRegex)
    String deadline;

    Boolean isDone;

    Project project;

    List<Worker> workers;


}
