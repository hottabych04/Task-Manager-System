package com.hottabych04.app.database.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
@Entity
@Table(name = "t_deactivated_token")
@Check(constraints = "c_keep_until > now()")
public class DeactivatedToken {

    @Id
    private UUID id;

    @Column(name = "c_keep_until", nullable = false)
    private LocalDateTime keepUntil;
}
