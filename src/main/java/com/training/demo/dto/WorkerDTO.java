package com.training.demo.dto;

import com.training.demo.entity.Assignment;
import com.training.demo.entity.Worker;
import lombok.*;

import java.util.List;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkerDTO {

    Long id;

    // @Pattern(regexp = nameRegex)
    String name;

    //@Pattern(regexp = surnameRegex)
    String surname;

    //@Pattern(regexp = loginRegex)
    String login;

    //@Email
    String email;

    //@NotNull
    String password;

    List<Assignment> assignments;


    public WorkerDTO(Worker worker) {
        //this.id = worker.getId();
        this.name = worker.getName();
        this.surname = worker.getSurname();
        this.login = worker.getLogin();
        this.email = worker.getEmail();
        // this.assignments = worker.getAssignments();


    }
}
