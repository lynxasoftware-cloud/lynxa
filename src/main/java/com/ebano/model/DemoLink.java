package com.ebano.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

/**
 * Un boton de demo de un sistema: el admin elige el texto (por ejemplo
 * "Demo pagina publica", "Demo panel") y la URL a la que apunta. Un sistema
 * puede tener cero, uno o varios de estos.
 */
@Embeddable
public class DemoLink {

    @Column(name = "etiqueta", length = 80)
    private String etiqueta;

    @Column(name = "url", length = 300)
    private String url;

    public DemoLink() { }

    public DemoLink(String etiqueta, String url) {
        this.etiqueta = etiqueta;
        this.url = url;
    }

    public String getEtiqueta() { return etiqueta; }
    public void setEtiqueta(String etiqueta) { this.etiqueta = etiqueta; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
