package com.ebano.config;

import com.ebano.model.*;
import com.ebano.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.List;

/**
 * Se ejecuta una vez al arrancar. Crea:
 *  - la fila de configuracion del sitio si no existe;
 *  - el usuario admin (a partir de env vars, o una clave aleatoria de emergencia);
 *  - 3 servicios y 2 sistemas de ejemplo, SOLO si la base esta completamente vacia,
 *    para que el sitio no arranque en blanco pero todo quede editable desde el panel.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final SiteConfigRepository siteConfigRepository;
    private final ServicioRepository servicioRepository;
    private final SistemaRepository sistemaRepository;
    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String envAdminUser;

    @Value("${app.admin.password}")
    private String envAdminPassword;

    public DataInitializer(SiteConfigRepository siteConfigRepository,
                            ServicioRepository servicioRepository,
                            SistemaRepository sistemaRepository,
                            AdminUserRepository adminUserRepository,
                            PasswordEncoder passwordEncoder) {
        this.siteConfigRepository = siteConfigRepository;
        this.servicioRepository = servicioRepository;
        this.sistemaRepository = sistemaRepository;
        this.adminUserRepository = adminUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        crearConfigSiNoExiste();
        crearAdminSiNoExiste();
        crearContenidoDeEjemploSiVacio();
    }

    private void crearConfigSiNoExiste() {
        if (siteConfigRepository.count() == 0) {
            siteConfigRepository.save(new SiteConfig());
            log.info("SiteConfig inicial creada con valores por defecto (editable desde /admin/apariencia e /admin/inicio).");
        }
    }

    private void crearAdminSiNoExiste() {
        if (adminUserRepository.count() > 0) {
            return; // ya existe un admin, no tocar nada
        }
        String username = (envAdminUser == null || envAdminUser.isBlank()) ? "admin" : envAdminUser;
        String password = envAdminPassword;
        boolean generada = false;
        if (password == null || password.isBlank()) {
            password = generarClaveAleatoria();
            generada = true;
        }
        AdminUser admin = new AdminUser();
        admin.setUsername(username);
        admin.setPasswordHash(passwordEncoder.encode(password));
        adminUserRepository.save(admin);

        if (generada) {
            log.warn("=====================================================================");
            log.warn(" No se configuraron ADMIN_USER / ADMIN_PASSWORD como variables de entorno.");
            log.warn(" Se genero un usuario admin temporal:");
            log.warn("   usuario:  {}", username);
            log.warn("   clave:    {}", password);
            log.warn(" Configura ADMIN_USER y ADMIN_PASSWORD como variables de entorno reales");
            log.warn(" y cambia esta clave apenas puedas ingresar al panel.");
            log.warn("=====================================================================");
        } else {
            log.info("Usuario admin '{}' creado a partir de las variables de entorno.", username);
        }
    }

    private String generarClaveAleatoria() {
        String chars = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz23456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 14; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private void crearContenidoDeEjemploSiVacio() {
        if (servicioRepository.count() == 0) {
            Servicio s1 = new Servicio();
            s1.setTitulo("Software a medida");
            s1.setDescripcion("Desarrollamos sistemas adaptados a los procesos concretos de tu negocio.");
            s1.setOrden(1);

            Servicio s2 = new Servicio();
            s2.setTitulo("Sistemas de gestion");
            s2.setDescripcion("Soluciones para administrar ventas, clientes, productos, reservas y stock.");
            s2.setOrden(2);

            Servicio s3 = new Servicio();
            s3.setTitulo("Desarrollo web");
            s3.setDescripcion("Paginas y aplicaciones web profesionales, rapidas y responsive.");
            s3.setOrden(3);

            servicioRepository.saveAll(List.of(s1, s2, s3));
            log.info("Servicios de ejemplo creados (editables/eliminables desde el panel).");
        }

        if (sistemaRepository.count() == 0) {
            Sistema demo = new Sistema();
            demo.setNombre("Sistema de gestion comercial");
            demo.setSlug("sistema-de-gestion-comercial");
            demo.setDescripcionCorta("Punto de venta, caja, stock y reportes para un comercio.");
            demo.setDescripcionCompleta("Ejemplo de ficha de sistema. Edita este texto, las imagenes, "
                    + "las caracteristicas y el estado desde el panel de administracion (/admin/sistemas).");
            demo.setCategoria("Gestion comercial");
            demo.setEstado(Sistema.Estado.PERSONALIZABLE);
            demo.setOrden(1);
            demo.setCaracteristicas(List.of(
                    "Punto de venta con caja y turnos",
                    "Control de stock e inventario",
                    "Reportes de ventas"
            ));

            sistemaRepository.save(demo);
            log.info("Sistema de ejemplo creado (editable/eliminable desde el panel).");
        }
    }
}
