package com.training.demo.dto;


import com.training.demo.entity.Project;
import com.training.demo.entity.Task;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


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
    List<Task> tasks;
}
