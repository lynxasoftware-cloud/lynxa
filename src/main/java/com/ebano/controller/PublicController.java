package com.ebano.controller;

import com.ebano.model.*;
import com.ebano.repository.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Todo lo que la pagina publica necesita leer, y el unico endpoint de
 * escritura sin autenticacion (enviar una consulta de contacto).
 */
@RestController
@RequestMapping("/api")
public class PublicController {

    private final SiteConfigRepository siteConfigRepository;
    private final ServicioRepository servicioRepository;
    private final SistemaRepository sistemaRepository;
    private final ConsultaRepository consultaRepository;

    public PublicController(SiteConfigRepository siteConfigRepository,
                             ServicioRepository servicioRepository,
                             SistemaRepository sistemaRepository,
                             ConsultaRepository consultaRepository) {
        this.siteConfigRepository = siteConfigRepository;
        this.servicioRepository = servicioRepository;
        this.sistemaRepository = sistemaRepository;
        this.consultaRepository = consultaRepository;
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

    public static class ConsultaRequest {
        @NotBlank(message = "Completa tu nombre.")
        public String nombre;
        public String telefono;
        public String email;
        public String tipoConsulta;
        @NotBlank(message = "Escribi tu mensaje.")
        public String mensaje;
        public String sistemaRelacionado;
    }

    @PostMapping("/consultas")
    public ResponseEntity<?> enviarConsulta(@Valid @RequestBody ConsultaRequest req) {
        Consulta c = new Consulta();
        c.setNombre(req.nombre.trim());
        c.setTelefono(req.telefono);
        c.setEmail(req.email);
        c.setTipoConsulta(req.tipoConsulta);
        c.setMensaje(req.mensaje.trim());
        c.setSistemaRelacionado(req.sistemaRelacionado);
        // NOTA HONESTA: aca es donde se conectaria un envio real por SMTP
        // (JavaMailSender) usando MAIL_HOST/MAIL_USER/MAIL_PASSWORD como
        // variables de entorno. Sin esas variables configuradas, el mensaje
        // queda persistido y visible en el panel (/admin/contacto -> Consultas),
        // pero NO se envia un correo real. emailEnviado queda en false para
        // reflejar eso con precision.
        consultaRepository.save(c);
        return ResponseEntity.ok(Map.of(
                "ok", true,
                "mensaje", "Consulta recibida. Te vamos a contactar a la brevedad."
        ));
    }
}
