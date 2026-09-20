package com.ebano.controller;

import com.ebano.model.*;
import com.ebano.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Todo lo que la pagina publica necesita leer. Ya no hay un endpoint de
 * contacto por formulario: los medios de contacto son enlaces directos
 * (WhatsApp/mailto/Instagram) construidos en el frontend a partir de
 * /api/public/config.
 */
@RestController
@RequestMapping("/api")
public class PublicController {

    private final SiteConfigRepository siteConfigRepository;
    private final ServicioRepository servicioRepository;
    private final SistemaRepository sistemaRepository;

    public PublicController(SiteConfigRepository siteConfigRepository,
                             ServicioRepository servicioRepository,
                             SistemaRepository sistemaRepository) {
        this.siteConfigRepository = siteConfigRepository;
        this.servicioRepository = servicioRepository;
        this.sistemaRepository = sistemaRepository;
    }

    @GetMapping("/public/config")
    public SiteConfig config() {
        return siteConfigRepository.findById(1L).orElseGet(SiteConfig::new);
    }

    @GetMapping("/public/servicios")
    public List<Servicio> servicios() {
        return servicioRepository.findByVisibleTrueOrderByOrdenAsc();
    }

    @GetMapping("/public/sistemas")
    public List<Sistema> sistemas() {
        return sistemaRepository.findByVisibleTrueOrderByOrdenAsc();
    }

    @GetMapping("/public/sistemas/{slug}")
    public ResponseEntity<Sistema> sistemaPorSlug(@PathVariable String slug) {
        return sistemaRepository.findBySlugAndVisibleTrue(slug)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
