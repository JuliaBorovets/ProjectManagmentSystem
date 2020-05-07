package com.training.demo.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "admin_is")
    private Long adminByWorkerId;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Task> tasks;

    public Project(String name, String description,Long adminByWorkerId){
        this.name = name;
        this.description = description;
        this.adminByWorkerId = adminByWorkerId;
        tasks = new ArrayList<Task>();
    }

    public Project(String name, String description,Long adminByWorkerId, ArrayList<Task> tasks){
        this.name = name;
        this.description = description;
        this.adminByWorkerId = adminByWorkerId;
        this.tasks = tasks;
    }
    public void addTask(Task t) {
        t.setProject(this);
        tasks.add(t);
    }





    @Override
    public String toString() {
        return "Project \"" + name + "\":\n" +
                "id = " + id +
                "\ndescription = " + description+"\n\n";
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Project p = (Project) o;
        return name.equals(p.name) &&
                description.equals(p.description);
    }

}
