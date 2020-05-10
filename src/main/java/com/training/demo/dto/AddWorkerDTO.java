package com.training.demo.dto;

import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddWorkerDTO {

    Long id;

    String login;
}

