package com.ebano.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Un servicio: solo titulo y descripcion. El estilo (color/tamano/fuente de
 * texto, fondo/borde/radio/padding de la tarjeta) NO vive aca - es un Estilo
 * Global unico compartido por todos los servicios (ver SiteConfig: srv*).
 */
@Entity
@Table(name = "servicio")
public class Servicio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El servicio necesita un titulo.")
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String titulo;

    @NotBlank(message = "El servicio necesita una descripcion.")
    @Size(max = 500)
    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(nullable = false)
    private boolean visible = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer orden) { this.orden = orden; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
}
