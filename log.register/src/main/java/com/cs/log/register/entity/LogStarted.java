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
public class LogStarted {

    @Id
    private String id;

    @Column(nullable = false)
    private String state;

    @Column()
    private String type;

    @Column()
    private String host;

    @Column(nullable = false)
    private Long timestamp;
}
