package com.cs.log.register.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDto {

    private String id;

    private String state;

    private String type;

    private String host;

    private Long timestamp;
}
