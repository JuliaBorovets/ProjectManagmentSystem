package com.training.demo.dto;


import com.training.demo.entity.Project;
import com.training.demo.entity.Role;
import com.training.demo.entity.Task;
import com.training.demo.entity.Worker;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class AssignmentDTO {

    Long id;

    Task task;

    Worker worker;

    Role role;
}
