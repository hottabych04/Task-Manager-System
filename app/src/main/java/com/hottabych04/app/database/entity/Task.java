package com.hottabych04.app.database.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "t_task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_name", nullable = false)
    private String name;

    @Column(name = "c_description")
    private String description;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_status")
    private Status status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_priority")
    private Priority priority;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_author")
    private User author;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "t_user_to_task",
            joinColumns = @JoinColumn(name = "id_task", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "id_user", nullable = false)
    )
    private List<User> performers;

}
