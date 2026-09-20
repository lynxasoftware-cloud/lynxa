package com.ebano.controller;

import com.ebano.model.*;
import com.ebano.repository.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.Normalizer;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Todo lo que requiere estar logueado como admin. Spring Security ya exige
 * autenticacion HTTP Basic sobre /api/admin/** (ver SecurityConfig).
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private static final Set<String> TIPOS_IMAGEN_PERMITIDOS = Set.of(
            "image/png", "image/jpeg", "image/webp", "image/gif"
    );
    private static final Set<String> TIPOS_VIDEO_PERMITIDOS = Set.of(
            "video/mp4", "video/webm", "video/quicktime"
    );
    private static final long TAMANO_MAXIMO_IMAGEN_BYTES = 5L * 1024 * 1024;   // 5 MB
    private static final long TAMANO_MAXIMO_VIDEO_BYTES = 60L * 1024 * 1024;  // 60 MB

    private final SiteConfigRepository siteConfigRepository;
    private final ServicioRepository servicioRepository;
    private final SistemaRepository sistemaRepository;
    private final AdminUserRepository adminUserRepository;

    @Value("${app.uploads.dir}")
    private String uploadsDir;

    public AdminController(SiteConfigRepository siteConfigRepository,
                            ServicioRepository servicioRepository,
                            SistemaRepository sistemaRepository,
                            AdminUserRepository adminUserRepository) {
        this.siteConfigRepository = siteConfigRepository;
        this.servicioRepository = servicioRepository;
        this.sistemaRepository = sistemaRepository;
        this.adminUserRepository = adminUserRepository;
    }

    // NOTA: el endpoint de login se quito a pedido explicito (el panel no
    // exige autenticacion). AdminUser/AdminUserRepository quedan sin usar
    // pero se dejan en el proyecto por si mas adelante se quiere reactivar
    // el login (ver comentario en SecurityConfig.java).

    // ---------------- CONFIG / APARIENCIA / INICIO ----------------

    @GetMapping("/config")
    public SiteConfig getConfig() {
        return siteConfigRepository.findById(1L).orElseGet(SiteConfig::new);
    }

    @PutMapping("/config")
    public SiteConfig updateConfig(@Valid @RequestBody SiteConfig nuevo) {
        nuevo.setId(1L);
        return siteConfigRepository.save(nuevo);
    }

    // ---------------- SERVICIOS (CRUD) ----------------

    @GetMapping("/servicios")
    public List<Servicio> listarServicios() {
        return servicioRepository.findAllByOrderByOrdenAsc();
    }

    @PostMapping("/servicios")
    public Servicio crearServicio(@Valid @RequestBody Servicio s) {
        s.setId(null);
        return servicioRepository.save(s);
    }

    @PutMapping("/servicios/{id}")
    public ResponseEntity<Servicio> editarServicio(@PathVariable Long id, @Valid @RequestBody Servicio s) {
        if (!servicioRepository.existsById(id)) return ResponseEntity.notFound().build();
        s.setId(id);
        return ResponseEntity.ok(servicioRepository.save(s));
    }

    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<?> eliminarServicio(@PathVariable Long id) {
        if (!servicioRepository.existsById(id)) return ResponseEntity.notFound().build();
        servicioRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    // ---------------- SISTEMAS (CRUD) ----------------

    @GetMapping("/sistemas")
    public List<Sistema> listarSistemas() {
        return sistemaRepository.findAllByOrderByOrdenAsc();
    }

    @GetMapping("/sistemas/{id}")
    public ResponseEntity<Sistema> getSistema(@PathVariable Long id) {
        return sistemaRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/sistemas")
    public ResponseEntity<?> crearSistema(@Valid @RequestBody Sistema s) {
        s.setId(null);
        s.setSlug(slugUnico(generarSlug(s.getNombre()), null));
        return ResponseEntity.ok(sistemaRepository.save(s));
    }

    @PutMapping("/sistemas/{id}")
    public ResponseEntity<?> editarSistema(@PathVariable Long id, @Valid @RequestBody Sistema s) {
        if (!sistemaRepository.existsById(id)) return ResponseEntity.notFound().build();
        s.setId(id);
        // Si cambio el nombre y no mando un slug propio explicito, lo regeneramos
        // asegurando que siga siendo unico frente a los demas sistemas.
        if (s.getSlug() == null || s.getSlug().isBlank()) {
            s.setSlug(slugUnico(generarSlug(s.getNombre()), id));
        }
        return ResponseEntity.ok(sistemaRepository.save(s));
    }

    @DeleteMapping("/sistemas/{id}")
    public ResponseEntity<?> eliminarSistema(@PathVariable Long id) {
        if (!sistemaRepository.existsById(id)) return ResponseEntity.notFound().build();
        sistemaRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("ok", true));
    }

    private String generarSlug(String nombre) {
        if (nombre == null || nombre.isBlank()) return "sistema-" + UUID.randomUUID().toString().substring(0, 8);
        String normalizado = Normalizer.normalize(nombre, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "");
        String slug = normalizado.toLowerCase()
                .replaceAll("[^a-z0-9\\s-]", "")
                .trim()
                .replaceAll("\\s+", "-");
        return slug.isBlank() ? "sistema-" + UUID.randomUUID().toString().substring(0, 8) : slug;
    }

    private String slugUnico(String base, Long idAIgnorar) {
        String candidato = base;
        int i = 2;
        while (true) {
            var existente = sistemaRepository.findBySlug(candidato);
            if (existente.isEmpty() || existente.get().getId().equals(idAIgnorar)) {
                return candidato;
            }
            candidato = base + "-" + i;
            i++;
        }
    }

    // ---------------- CAMBIO DE CLAVE DEL ADMIN ----------------

    public static class CambiarClaveRequest {
        public String claveActual;
        public String claveNueva;
    }

    // (Implementacion minima: valida contra el hash guardado usando el propio
    // PasswordEncoder de Spring a traves del AuthenticationManager quedaria mas
    // prolijo, pero para no ampliar el alcance de esta primera entrega se deja
    // documentado como pendiente en el informe final.)

    // ---------------- SUBIDA DE IMAGENES O VIDEOS ----------------
    // La galeria y la imagen principal de un sistema aceptan tanto imagenes
    // como videos cortos (para mostrar una demo grabada, por ejemplo).

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirImagen(@RequestParam("archivo") MultipartFile archivo) {
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("ok", false, "mensaje", "No se recibio ningun archivo."));
        }
        String tipo = archivo.getContentType();
        boolean esImagen = TIPOS_IMAGEN_PERMITIDOS.contains(tipo);
        boolean esVideo = TIPOS_VIDEO_PERMITIDOS.contains(tipo);
        if (!esImagen && !esVideo) {
            return ResponseEntity.badRequest().body(Map.of("ok", false,
                    "mensaje", "Formato no permitido. Usa PNG, JPG, WEBP, GIF para imagenes, o MP4, WEBM, MOV para video."));
        }
        long limite = esVideo ? TAMANO_MAXIMO_VIDEO_BYTES : TAMANO_MAXIMO_IMAGEN_BYTES;
        if (archivo.getSize() > limite) {
            String limiteTexto = esVideo ? "60 MB" : "5 MB";
            return ResponseEntity.badRequest().body(Map.of("ok", false,
                    "mensaje", "El archivo supera el tamano maximo permitido (" + limiteTexto + ")."));
        }
        try {
            Path carpeta = Path.of(uploadsDir);
            Files.createDirectories(carpeta);

            String extension = switch (tipo) {
                case "image/png" -> ".png";
                case "image/jpeg" -> ".jpg";
                case "image/webp" -> ".webp";
                case "image/gif" -> ".gif";
                case "video/mp4" -> ".mp4";
                case "video/webm" -> ".webm";
                case "video/quicktime" -> ".mov";
                default -> "";
            };
            String nombreArchivo = UUID.randomUUID() + extension;
            Path destino = carpeta.resolve(nombreArchivo);
            Files.copy(archivo.getInputStream(), destino);

            String urlPublica = "/uploads/" + nombreArchivo;
            return ResponseEntity.ok(Map.of("ok", true, "url", urlPublica, "tipo", esVideo ? "video" : "imagen"));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body(Map.of("ok", false,
                    "mensaje", "No pudimos guardar el archivo. Intenta nuevamente."));
        }
    }
}
