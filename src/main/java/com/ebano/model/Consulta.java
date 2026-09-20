package com.ebano.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Una consulta enviada desde el formulario de contacto. Se guarda siempre
 * en la base (aunque el envio por correo real no este configurado), para
 * que el admin pueda verla desde el panel y nunca se pierda un contacto.
 */
@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 40)
    private String telefono;

    @Column(length = 120)
    private String email;

    @Column(length = 80)
    private String tipoConsulta;

    @Column(nullable = false, length = 2000)
    private String mensaje;

    @Column(length = 140)
    private String sistemaRelacionado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    @Column(nullable = false)
    private boolean leida = false;

    // El envio de correo real (SMTP) se intento en este orden?
    @Column(nullable = false)
    private boolean emailEnviado = false;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String v) { this.nombre = v; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { this.telefono = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getTipoConsulta() { return tipoConsulta; }
    public void setTipoConsulta(String v) { this.tipoConsulta = v; }
    public String getMensaje() { return mensaje; }
    public void setMensaje(String v) { this.mensaje = v; }
    public String getSistemaRelacionado() { return sistemaRelacionado; }
    public void setSistemaRelacionado(String v) { this.sistemaRelacionado = v; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime v) { this.fechaCreacion = v; }
    public boolean isLeida() { return leida; }
    public void setLeida(boolean v) { this.leida = v; }
    public boolean isEmailEnviado() { return emailEnviado; }
    public void setEmailEnviado(boolean v) { this.emailEnviado = v; }
}
