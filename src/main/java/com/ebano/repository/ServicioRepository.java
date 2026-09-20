package com.ebano.repository;

import com.ebano.model.Servicio;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServicioRepository extends JpaRepository<Servicio, Long> {
    List<Servicio> findByVisibleTrueOrderByOrdenAsc();
    List<Servicio> findAllByOrderByOrdenAsc();
}
