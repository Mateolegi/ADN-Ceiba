package com.ceiba.citas_medicas.domain.persistence;

import com.ceiba.citas_medicas.domain.model.Cita;

import java.util.List;
import java.util.Optional;

public interface CitaPersistence {

    Cita save(Cita cita);

    void delete(Cita cita);

    List<Cita> findAll();

    Optional<Cita> find(Long id);
}
