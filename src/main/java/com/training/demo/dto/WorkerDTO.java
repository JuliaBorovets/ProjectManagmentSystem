package com.training.demo.dto;

import com.training.demo.entity.Assignment;
import lombok.*;

import com.training.demo.entity.Worker;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.List;

import static com.training.demo.dto.Regex.*;


@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WorkerDTO {

    Long id;

    @Pattern(regexp = nameRegex)
    String name;

    @Pattern(regexp = surnameRegex)
    String surname;

    @Pattern(regexp = loginRegex)
    String login;

    @Email
    String email;

    @NotNull
    String password;

    List<Assignment> assignments;


    public WorkerDTO(Worker worker) {
        this.id = worker.getId();
        this.name = worker.getName();
        this.surname = worker.getSurname();
        this.login = worker.getLogin();
        this.email = worker.getEmail();
        this.assignments = worker.getAssignments();


    }
}
