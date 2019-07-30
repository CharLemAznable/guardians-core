package com.github.charlemaznable.guardians.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GuardianExceptionTest {

    @Test
    public void testGuardianException() {
        assertNull(new GuardianException().getMessage());
        assertEquals("TestMessage", new GuardianException("TestMessage").getMessage());
        assertEquals("TestMessage", new GuardianException("TestMessage", new RuntimeException()).getMessage());
        assertEquals("java.lang.RuntimeException", new GuardianException(new RuntimeException()).getMessage());
    }
}
