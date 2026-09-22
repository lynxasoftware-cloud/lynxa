package com.ebano.model;

import jakarta.persistence.*;

/**
 * Configuracion global del sitio: siempre hay una unica fila (id = 1).
 * Todo lo que un admin razonablemente querria cambiar sin tocar codigo
 * (textos del inicio, datos de contacto, colores, logo, y ahora tambien
 * el "Estilo Global" de las tarjetas de Servicios y Sistemas) vive aca.
 */
@Entity
@Table(name = "site_config")
public class SiteConfig {

    @Id
    private Long id = 1L;

    // ---- Inicio / Hero ----
    @Column(length = 200)
    private String tituloPrincipal = "Software que se adapta a tu negocio";

    @Column(length = 400)
    private String subtitulo = "Desarrollamos software a medida, sistemas de gestion y aplicaciones web que resuelven necesidades concretas.";

    @Column(length = 120)
    private String textoBotonPrimario = "Ver sistemas";

    @Column(length = 120)
    private String textoBotonSecundario = "Solicitar desarrollo";

    // Personalizacion del titulo y subtitulo del hero (vacio/null = usa el
    // estilo general del sitio: color de texto, tipografia de titulos/cuerpo).
    @Column(length = 20)
    private String heroTituloColor;
    private Integer heroTituloTamanoPx;
    @Column(length = 60)
    private String heroTituloFuente;

    @Column(length = 20)
    private String heroSubtituloColor;
    private Integer heroSubtituloTamanoPx;
    @Column(length = 60)
    private String heroSubtituloFuente;

    // Personalizacion general del bloque Hero.
    @Column(length = 20)
    private String heroFondoColor; // vacio = usa colorFondo general
    @Column(length = 300)
    private String heroImagenFondo; // vacio = sin imagen de fondo
    @Column(length = 20)
    private String heroAlineacion = "izquierda"; // izquierda | centro
    // Que parte de la foto queda siempre visible aunque se recorte en pantallas
    // angostas (el "foco" o protagonismo de la imagen). Valores tipo CSS
    // background-position: "left top", "center center", "right bottom", etc.
    @Column(length = 20)
    private String heroImagenPosicion = "center center";
    @Column(length = 20)
    private String heroEspaciado = "normal"; // compacto | normal | amplio

    // ---- Contacto ----
    @Column(length = 40)
    private String whatsappNumero = "";

    @Column(length = 120)
    private String email = "";

    @Column(length = 40)
    private String telefono = "";

    @Column(length = 200)
    private String instagramUrl = "";

    // ---- Apariencia ----
    @Column(length = 20)
    private String colorPrimario = "#8FA998";

    @Column(length = 20)
    private String colorSecundario = "#121412";

    @Column(length = 20)
    private String colorFondo = "#F5F7F5";

    @Column(length = 20)
    private String colorTexto = "#121412";

    @Column(length = 300)
    private String logoUrl = "";

    @Column(length = 300)
    private String faviconUrl = "";

    // Fondo y texto de las tarjetas (Servicios/Sistemas) - antes quedaban
    // hardcodeadas en blanco en el CSS, ahora se controlan desde aca.
    @Column(length = 20)
    private String colorTarjetaFondo = "#FFFFFF";

    @Column(length = 20)
    private String colorTarjetaTexto = "#121412";

    // Tipografia general: nombre de una fuente de Google Fonts (o "system"
    // para usar la fuente nativa del dispositivo, sin descargar nada).
    @Column(length = 60)
    private String fuenteTitulos = "Inter";

    @Column(length = 60)
    private String fuenteCuerpo = "Inter";

    // ---- Estilo Global de Servicios (se configura UNA vez, se aplica a TODAS
    // las tarjetas de servicio; cada servicio individual ya no tiene su propio
    // estilo - solo titulo y descripcion). Vacio/null = usa un default prolijo. ----
    @Column(length = 20)
    private String srvTituloColor;
    private Integer srvTituloTamanoPx;
    @Column(length = 60)
    private String srvTituloFuente;
    @Column(length = 10)
    private String srvTituloPeso; // 400 | 500 | 600 | 700 | 800

    @Column(length = 20)
    private String srvDescColor;
    private Integer srvDescTamanoPx;
    @Column(length = 60)
    private String srvDescFuente;

    @Column(length = 20)
    private String srvCardFondo;
    @Column(length = 20)
    private String srvCardBorde;
    private Integer srvCardRadioPx;
    private Integer srvCardPaddingPx;

    // ---- Estilo Global de Sistemas (mismo concepto, para el catalogo y la
    // ficha de cada sistema: nombre y descripcion corta). ----
    @Column(length = 20)
    private String sisTituloColor;
    private Integer sisTituloTamanoPx;
    @Column(length = 60)
    private String sisTituloFuente;
    @Column(length = 10)
    private String sisTituloPeso;

    @Column(length = 20)
    private String sisDescColor;
    private Integer sisDescTamanoPx;
    @Column(length = 60)
    private String sisDescFuente;

    @Column(length = 20)
    private String sisCardFondo;
    @Column(length = 20)
    private String sisCardBorde;
    private Integer sisCardRadioPx;
    private Integer sisCardPaddingPx;

    // ---- SEO basico ----
    @Column(length = 120)
    private String seoTitle = "LYNXA Software - Desarrollo de software a medida";

    @Column(length = 300)
    private String seoDescription = "Sistemas de gestion, aplicaciones web y desarrollo de software a medida para negocios.";

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTituloPrincipal() { return tituloPrincipal; }
    public void setTituloPrincipal(String v) { this.tituloPrincipal = v; }
    public String getSubtitulo() { return subtitulo; }
    public void setSubtitulo(String v) { this.subtitulo = v; }
    public String getTextoBotonPrimario() { return textoBotonPrimario; }
    public void setTextoBotonPrimario(String v) { this.textoBotonPrimario = v; }
    public String getTextoBotonSecundario() { return textoBotonSecundario; }
    public void setTextoBotonSecundario(String v) { this.textoBotonSecundario = v; }
    public String getHeroTituloColor() { return heroTituloColor; }
    public void setHeroTituloColor(String v) { this.heroTituloColor = v; }
    public Integer getHeroTituloTamanoPx() { return heroTituloTamanoPx; }
    public void setHeroTituloTamanoPx(Integer v) { this.heroTituloTamanoPx = v; }
    public String getHeroTituloFuente() { return heroTituloFuente; }
    public void setHeroTituloFuente(String v) { this.heroTituloFuente = v; }
    public String getHeroSubtituloColor() { return heroSubtituloColor; }
    public void setHeroSubtituloColor(String v) { this.heroSubtituloColor = v; }
    public Integer getHeroSubtituloTamanoPx() { return heroSubtituloTamanoPx; }
    public void setHeroSubtituloTamanoPx(Integer v) { this.heroSubtituloTamanoPx = v; }
    public String getHeroSubtituloFuente() { return heroSubtituloFuente; }
    public void setHeroSubtituloFuente(String v) { this.heroSubtituloFuente = v; }
    public String getHeroFondoColor() { return heroFondoColor; }
    public void setHeroFondoColor(String v) { this.heroFondoColor = v; }
    public String getHeroImagenFondo() { return heroImagenFondo; }
    public void setHeroImagenFondo(String v) { this.heroImagenFondo = v; }
    public String getHeroAlineacion() { return heroAlineacion; }
    public void setHeroAlineacion(String v) { this.heroAlineacion = v; }
    public String getHeroImagenPosicion() { return heroImagenPosicion; }
    public void setHeroImagenPosicion(String v) { this.heroImagenPosicion = v; }
    public String getHeroEspaciado() { return heroEspaciado; }
    public void setHeroEspaciado(String v) { this.heroEspaciado = v; }
    public String getWhatsappNumero() { return whatsappNumero; }
    public void setWhatsappNumero(String v) { this.whatsappNumero = v; }
    public String getEmail() { return email; }
    public void setEmail(String v) { this.email = v; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String v) { this.telefono = v; }
    public String getInstagramUrl() { return instagramUrl; }
    public void setInstagramUrl(String v) { this.instagramUrl = v; }
    public String getColorPrimario() { return colorPrimario; }
    public void setColorPrimario(String v) { this.colorPrimario = v; }
    public String getColorSecundario() { return colorSecundario; }
    public void setColorSecundario(String v) { this.colorSecundario = v; }
    public String getColorFondo() { return colorFondo; }
    public void setColorFondo(String v) { this.colorFondo = v; }
    public String getColorTexto() { return colorTexto; }
    public void setColorTexto(String v) { this.colorTexto = v; }
    public String getLogoUrl() { return logoUrl; }
    public void setLogoUrl(String v) { this.logoUrl = v; }
    public String getFaviconUrl() { return faviconUrl; }
    public void setFaviconUrl(String v) { this.faviconUrl = v; }
    public String getColorTarjetaFondo() { return colorTarjetaFondo; }
    public void setColorTarjetaFondo(String v) { this.colorTarjetaFondo = v; }
    public String getColorTarjetaTexto() { return colorTarjetaTexto; }
    public void setColorTarjetaTexto(String v) { this.colorTarjetaTexto = v; }
    public String getFuenteTitulos() { return fuenteTitulos; }
    public void setFuenteTitulos(String v) { this.fuenteTitulos = v; }
    public String getFuenteCuerpo() { return fuenteCuerpo; }
    public void setFuenteCuerpo(String v) { this.fuenteCuerpo = v; }
    public String getSrvTituloColor() { return srvTituloColor; }
    public void setSrvTituloColor(String v) { this.srvTituloColor = v; }
    public Integer getSrvTituloTamanoPx() { return srvTituloTamanoPx; }
    public void setSrvTituloTamanoPx(Integer v) { this.srvTituloTamanoPx = v; }
    public String getSrvTituloFuente() { return srvTituloFuente; }
    public void setSrvTituloFuente(String v) { this.srvTituloFuente = v; }
    public String getSrvTituloPeso() { return srvTituloPeso; }
    public void setSrvTituloPeso(String v) { this.srvTituloPeso = v; }
    public String getSrvDescColor() { return srvDescColor; }
    public void setSrvDescColor(String v) { this.srvDescColor = v; }
    public Integer getSrvDescTamanoPx() { return srvDescTamanoPx; }
    public void setSrvDescTamanoPx(Integer v) { this.srvDescTamanoPx = v; }
    public String getSrvDescFuente() { return srvDescFuente; }
    public void setSrvDescFuente(String v) { this.srvDescFuente = v; }
    public String getSrvCardFondo() { return srvCardFondo; }
    public void setSrvCardFondo(String v) { this.srvCardFondo = v; }
    public String getSrvCardBorde() { return srvCardBorde; }
    public void setSrvCardBorde(String v) { this.srvCardBorde = v; }
    public Integer getSrvCardRadioPx() { return srvCardRadioPx; }
    public void setSrvCardRadioPx(Integer v) { this.srvCardRadioPx = v; }
    public Integer getSrvCardPaddingPx() { return srvCardPaddingPx; }
    public void setSrvCardPaddingPx(Integer v) { this.srvCardPaddingPx = v; }
    public String getSisTituloColor() { return sisTituloColor; }
    public void setSisTituloColor(String v) { this.sisTituloColor = v; }
    public Integer getSisTituloTamanoPx() { return sisTituloTamanoPx; }
    public void setSisTituloTamanoPx(Integer v) { this.sisTituloTamanoPx = v; }
    public String getSisTituloFuente() { return sisTituloFuente; }
    public void setSisTituloFuente(String v) { this.sisTituloFuente = v; }
    public String getSisTituloPeso() { return sisTituloPeso; }
    public void setSisTituloPeso(String v) { this.sisTituloPeso = v; }
    public String getSisDescColor() { return sisDescColor; }
    public void setSisDescColor(String v) { this.sisDescColor = v; }
    public Integer getSisDescTamanoPx() { return sisDescTamanoPx; }
    public void setSisDescTamanoPx(Integer v) { this.sisDescTamanoPx = v; }
    public String getSisDescFuente() { return sisDescFuente; }
    public void setSisDescFuente(String v) { this.sisDescFuente = v; }
    public String getSisCardFondo() { return sisCardFondo; }
    public void setSisCardFondo(String v) { this.sisCardFondo = v; }
    public String getSisCardBorde() { return sisCardBorde; }
    public void setSisCardBorde(String v) { this.sisCardBorde = v; }
    public Integer getSisCardRadioPx() { return sisCardRadioPx; }
    public void setSisCardRadioPx(Integer v) { this.sisCardRadioPx = v; }
    public Integer getSisCardPaddingPx() { return sisCardPaddingPx; }
    public void setSisCardPaddingPx(Integer v) { this.sisCardPaddingPx = v; }
    public String getSeoTitle() { return seoTitle; }
    public void setSeoTitle(String v) { this.seoTitle = v; }
    public String getSeoDescription() { return seoDescription; }
    public void setSeoDescription(String v) { this.seoDescription = v; }
}
