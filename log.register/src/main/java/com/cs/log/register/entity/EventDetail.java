package com.cs.log.register.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventDetail {
    @Id
    private String id;

    @Column()
    private String type;

    @Column()
    private String host;

    @Column(nullable = false)
    private Long duration;

    @Column(nullable = false)
    private boolean alert;
}
