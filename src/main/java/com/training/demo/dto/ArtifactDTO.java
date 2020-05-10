package com.training.demo.dto;

import com.training.demo.entity.Project;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ArtifactDTO {

    Long id;

    String name;

    String type;

    String content;

}
