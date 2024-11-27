package com.hottabych04.app.database.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "t_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "c_message", nullable = false)
    private String message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_author", nullable = false)
    private User author;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_task", nullable = false)
    private Task task;

}
