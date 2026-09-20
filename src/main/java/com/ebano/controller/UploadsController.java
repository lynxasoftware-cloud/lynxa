package com.ebano.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Sirve publicamente (sin login) las imagenes que el admin subio desde el
 * panel: son fotos de productos/sistemas que deben verse en la pagina publica.
 * Esta ruta esta explicitamente permitida sin autenticacion en SecurityConfig.
 */
@RestController
public class UploadsController {

    @Value("${app.uploads.dir}")
    private String uploadsDir;

    @GetMapping("/uploads/{nombreArchivo}")
    public ResponseEntity<UrlResource> verImagen(@PathVariable String nombreArchivo) throws IOException {
        Path base = Path.of(uploadsDir).normalize();
        Path archivo = base.resolve(nombreArchivo).normalize();
        // Evita path traversal (../../etc): el archivo resuelto debe seguir dentro de la carpeta de uploads.
        if (!archivo.startsWith(base) || !Files.exists(archivo)) {
            return ResponseEntity.notFound().build();
        }
        UrlResource resource = new UrlResource(archivo.toUri());
        String contentType = Files.probeContentType(archivo);
        return ResponseEntity.ok()
                .contentType(contentType != null ? MediaType.parseMediaType(contentType) : MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
