package com.ebano.repository;

import com.ebano.model.Sistema;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SistemaRepository extends JpaRepository<Sistema, Long> {
    List<Sistema> findByVisibleTrueOrderByOrdenAsc();
    List<Sistema> findAllByOrderByOrdenAsc();
    Optional<Sistema> findBySlugAndVisibleTrue(String slug);
    Optional<Sistema> findBySlug(String slug);
    boolean existsBySlug(String slug);
}
