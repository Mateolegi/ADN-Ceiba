package com.ceiba.citas_medicas.application.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientCommandTest {

    @Test
    void getters() {
        var command = new ClientCommand();
        assertNull(command.getId());
        assertNull(command.getDocumentNumber());
        assertNull(command.getFullName());
    }

    @Test
    void one_arg_constructor() {
        var command = new ClientCommand("123");
        assertNull(command.getId());
        assertNotNull(command.getDocumentNumber());
        assertNull(command.getFullName());
    }

    @Test
    void equals() {
        var command = new ClientCommand(1L, "123", "John Doe");
        var command2 = new ClientCommand(1L, "123", "John Doe");
        assertTrue(command.equals(command2));
    }
}