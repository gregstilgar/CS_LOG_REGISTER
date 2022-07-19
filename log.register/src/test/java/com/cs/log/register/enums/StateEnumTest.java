package com.cs.log.register.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StateEnumTest {
    private final String STARTED = "STARTED";
    private final String FINISHED = "FINISHED";

    @Test
    void enumStateTest() {
        Assertions.assertTrue(StateEnum.STARTED.name().equals(STARTED));
        Assertions.assertTrue(StateEnum.FINISHED.name().equals(FINISHED));
    }
}
