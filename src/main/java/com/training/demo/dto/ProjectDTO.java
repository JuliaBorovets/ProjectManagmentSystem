package com.training.demo.dto;


import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import lombok.*;

import com.training.demo.entity.Worker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

import static com.training.demo.dto.Regex.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ProjectDTO {

    Long id;
    String name;
    String description;
    Long adminByWorkerId;
    List<Task> tasks;

    public ProjectDTO(Project project){
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.adminByWorkerId = project.getAdminByWorkerId();
        tasks = new ArrayList<Task>();
    }


}
