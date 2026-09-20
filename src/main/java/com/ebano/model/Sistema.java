package com.ebano.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sistema")
public class Sistema {
    // El estilo del nombre/descripcion (color, tamano, fuente) y el estilo de
    // la tarjeta (fondo/borde/radio/padding) ya NO viven aca - son un Estilo
    // Global unico compartido por todos los sistemas (ver SiteConfig: sis*).

    public enum Estado { DISPONIBLE, PERSONALIZABLE, EN_DESARROLLO, PROXIMAMENTE }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El sistema necesita un nombre.")
    @Column(nullable = false, length = 120)
    private String nombre;

    // URL amigable, unica: /sistemas/{slug}
    @Column(nullable = false, unique = true, length = 140)
    private String slug;

    @Column(length = 300)
    private String descripcionCorta;

    @Column(length = 4000)
    private String descripcionCompleta;

    @Column(length = 80)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Estado estado = Estado.DISPONIBLE;

    private Double precio;

    @Column(nullable = false)
    private boolean mostrarPrecio = false;

    @Column(length = 300)
    private String imagenPrincipal;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sistema_galeria", joinColumns = @JoinColumn(name = "sistema_id"))
    @Column(name = "url_imagen", length = 300)
    @OrderColumn(name = "posicion")
    private List<String> galeria = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sistema_caracteristicas", joinColumns = @JoinColumn(name = "sistema_id"))
    @Column(name = "texto", length = 300)
    @OrderColumn(name = "posicion")
    private List<String> caracteristicas = new ArrayList<>();

    // Una o mas URLs de demo (antes era un solo campo). Cada una se muestra
    // como su propio boton en la ficha ("Ver demo" si hay una sola, "Ver demo 1",
    // "Ver demo 2"... si hay varias).
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "sistema_demo_urls", joinColumns = @JoinColumn(name = "sistema_id"))
    @Column(name = "url", length = 300)
    @OrderColumn(name = "posicion")
    private List<String> demoUrls = new ArrayList<>();

    @Column(nullable = false)
    private boolean demoActiva = false;

    @Column(length = 80)
    private String textoBotonDemo = "Ver demo";

    @Column(length = 200)
    private String textoConsulta = "Consultar este sistema";

    @Column(nullable = false)
    private Integer orden = 0;

    @Column(nullable = false)
    private boolean visible = true;

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }
    public String getSlug() { return slug; }
    public void setSlug(String v) { this.slug = v; }
    public String getDescripcionCorta() { return descripcionCorta; }
    public void setDescripcionCorta(String v) { this.descripcionCorta = v; }
    public String getDescripcionCompleta() { return descripcionCompleta; }
    public void setDescripcionCompleta(String v) { this.descripcionCompleta = v; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String v) { this.categoria = v; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado v) { this.estado = v; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double v) { this.precio = v; }
    public boolean isMostrarPrecio() { return mostrarPrecio; }
    public void setMostrarPrecio(boolean v) { this.mostrarPrecio = v; }
    public String getImagenPrincipal() { return imagenPrincipal; }
    public void setImagenPrincipal(String v) { this.imagenPrincipal = v; }
    public List<String> getGaleria() { return galeria; }
    public void setGaleria(List<String> v) { this.galeria = v; }
    public List<String> getCaracteristicas() { return caracteristicas; }
    public void setCaracteristicas(List<String> v) { this.caracteristicas = v; }
    public List<String> getDemoUrls() { return demoUrls; }
    public void setDemoUrls(List<String> v) { this.demoUrls = v; }
    public boolean isDemoActiva() { return demoActiva; }
    public void setDemoActiva(boolean v) { this.demoActiva = v; }
    public String getTextoBotonDemo() { return textoBotonDemo; }
    public void setTextoBotonDemo(String v) { this.textoBotonDemo = v; }
    public String getTextoConsulta() { return textoConsulta; }
    public void setTextoConsulta(String v) { this.textoConsulta = v; }
    public Integer getOrden() { return orden; }
    public void setOrden(Integer v) { this.orden = v; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean v) { this.visible = v; }

    /** Hay demo real solo si esta activa Y tiene al menos una URL cargada. */
    public boolean tieneDemoUtilizable() {
        return demoActiva && demoUrls != null && !demoUrls.isEmpty();
    }
}
