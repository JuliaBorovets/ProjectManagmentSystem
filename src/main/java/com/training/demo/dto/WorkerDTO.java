package com.training.demo.dto;

import com.training.demo.entity.Task;
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

    List<Task> tasks;

}
