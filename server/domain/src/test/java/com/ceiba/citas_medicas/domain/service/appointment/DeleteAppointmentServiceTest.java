package com.ceiba.citas_medicas.domain.service.appointment;

import com.ceiba.citas_medicas.domain.exception.EntityNotExistsException;
import com.ceiba.citas_medicas.domain.model.Appointment;
import com.ceiba.citas_medicas.domain.model.Client;
import com.ceiba.citas_medicas.domain.persistence.AppointmentPersistence;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class DeleteAppointmentServiceTest {

    @Test
    void delete_when_exists() {
        // arrange
        var today = LocalDateTime.of(2019, Month.OCTOBER, 29, 12, 0);
        var client = new Client("123", "John Doe");
        var appointment = new Appointment(1L, today, today.plusDays(1), client);
        var persistence = mock(AppointmentPersistence.class);
        doReturn(Optional.of(appointment)).when(persistence).find(anyLong());
        var service = new DeleteAppointmentService(persistence);

        // act - assert
        assertDoesNotThrow(() -> service.execute(1L));
    }

    @Test
    void delete_when_not_exists() {
        // arrange
        var persistence = mock(AppointmentPersistence.class);
        doReturn(Optional.empty()).when(persistence).find(anyLong());
        var service = new DeleteAppointmentService(persistence);

        // act - assert
        assertThrows(EntityNotExistsException.class, () -> service.execute(1L));
    }
}