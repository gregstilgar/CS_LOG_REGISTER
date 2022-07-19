package com.cs.log.register.enums;

public enum StateEnum {

    STARTED("STARTED"),
    FINISHED("FINISHED");

    private StateEnum(String l) {
        label = l;
    }

    private String label;
}
