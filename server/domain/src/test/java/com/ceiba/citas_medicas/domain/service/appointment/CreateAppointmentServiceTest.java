package com.ceiba.citas_medicas.domain.service.appointment;

import com.ceiba.citas_medicas.domain.exception.EntityExistsException;
import com.ceiba.citas_medicas.domain.model.Appointment;
import com.ceiba.citas_medicas.domain.model.Client;
import com.ceiba.citas_medicas.domain.persistence.AppointmentPersistence;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateAppointmentServiceTest {

    @Test
    void create_when_not_exists() {
        // arrange
        var today = LocalDateTime.of(2019, Month.OCTOBER, 29, 12, 0);
        var client = new Client("123", "John Doe");
        var appointment = new Appointment(today, today.plusDays(1), client);
        var persistence = mock(AppointmentPersistence.class);
        doAnswer(invocation -> {
            // assert
            var appointmentArg = (Appointment) invocation.getArgument(0);
            assertNotNull(appointmentArg);
            assertNull(appointmentArg.getId());
            return null;
        }).when(persistence).save(any(Appointment.class));
        var service = new CreateAppointmentService(persistence);

        // act - assert
        assertDoesNotThrow(() -> service.execute(appointment));
    }

    @Test
    void create_when_exists() {
        // arrange
        var today = LocalDateTime.of(2019, Month.OCTOBER, 29, 12, 0);
        var client = new Client("123", "John Doe");
        var appointment = new Appointment(1L, today, today.plusDays(1), client);
        var persistence = mock(AppointmentPersistence.class);
        doAnswer(invocation -> {
            // assert
            var appointmentArg = (Appointment) invocation.getArgument(0);
            assertNotNull(appointmentArg);
            return null;
        }).when(persistence).save(any(Appointment.class));
        doReturn(Optional.of(appointment)).when(persistence).find(anyLong());
        var service = new CreateAppointmentService(persistence);

        // act - assert
        assertThrows(EntityExistsException.class, () -> service.execute(appointment));
    }
}