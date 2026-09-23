package com.facturacion;

import com.facturacion.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import java.util.Calendar;

public class Main {

    public static void main(String[] args) {

        System.setProperty("user.timezone", "UTC");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            // ---------- Usuario ----------
            Usuario usuario = new Usuario();
            usuario.setUsuario("admin");
            usuario.setClave("1234");
            usuario.setNombre("Ana");
            usuario.setApellido("Perez");
            em.persist(usuario);

            // ---------- Rubro y Marca ----------
            Rubro rubro = new Rubro();
            rubro.setCodigo(1);
            rubro.setDenominacion("Electrónica");
            rubro.setFechaAlta(new Date());
            rubro.setFechaModificacion(new Date());
            rubro.setUsuarioCarga(usuario);
            rubro.setUsuarioModificacion(usuario);
            em.persist(rubro);

            Marca marca = new Marca();
            marca.setCodigo(1);
            marca.setDenominacion("Samsung");
            marca.setFechaAlta(new Date());
            marca.setFechaModificacion(new Date());
            marca.setUsuarioCarga(usuario);
            marca.setUsuarioModificacion(usuario);
            em.persist(marca);

            // ---------- Puntos de venta ----------
            PuntoVenta pv1 = new PuntoVenta();
            pv1.setNumero(1);
            pv1.setDescripcion("Casa Central");
            pv1.setTipoEmision("Electronica");
            pv1.setDomicilioComercial("Av. Siempre Viva 123");
            pv1.setFechaAlta(new Date());
            pv1.setFechaModificacion(new Date());
            pv1.setUsuarioCarga(usuario);
            pv1.setUsuarioModificacion(usuario);
            em.persist(pv1);

            PuntoVenta pv2 = new PuntoVenta();
            pv2.setNumero(2);
            pv2.setDescripcion("Sucursal Norte");
            pv2.setTipoEmision("Electronica");
            pv2.setDomicilioComercial("Belgrano 500");
            pv2.setFechaAlta(new Date());
            pv2.setFechaModificacion(new Date());
            pv2.setUsuarioCarga(usuario);
            pv2.setUsuarioModificacion(usuario);
            em.persist(pv2);

            // ---------- Artículos ----------
            // ART-001 SÍ tiene rubro, marca, y se va a vender (para consultas 3, 12, 13, 17, 18)
            Articulo articulo1 = new Articulo();
            articulo1.setCodigo("ART-001");
            articulo1.setDenominacion("Notebook 15\"");
            articulo1.setRubro(rubro);
            articulo1.setMarca(marca);
            articulo1.setFechaAlta(new Date());
            articulo1.setFechaModificacion(new Date());
            articulo1.setUsuarioCarga(usuario);
            articulo1.setUsuarioModificacion(usuario);
            em.persist(articulo1);

            // ART-002 NUNCA se vende (para probar la consulta 19, NOT EXISTS)
            Articulo articulo2 = new Articulo();
            articulo2.setCodigo("ART-002");
            articulo2.setDenominacion("Mouse Inalámbrico");
            articulo2.setFechaAlta(new Date());
            articulo2.setFechaModificacion(new Date());
            articulo2.setUsuarioCarga(usuario);
            articulo2.setUsuarioModificacion(usuario);
            em.persist(articulo2);

            // ---------- Lista de precios ----------
            ListaPrecio listaPrecio = new ListaPrecio();
            listaPrecio.setCodigo("LP-GRAL");
            listaPrecio.setDenominacion("Lista General");
            listaPrecio.setFechaAlta(new Date());
            listaPrecio.setFechaModificacion(new Date());
            listaPrecio.setUsuarioCarga(usuario);
            listaPrecio.setUsuarioModificacion(usuario);
            em.persist(listaPrecio);

            ListaPrecioArticulo lpa1 = new ListaPrecioArticulo();
            lpa1.setListaPrecio(listaPrecio);
            lpa1.setArticulo(articulo1);
            lpa1.setPrecioVenta(15000.0);
            lpa1.setFechaAlta(new Date());
            lpa1.setFechaModificacion(new Date());
            lpa1.setUsuarioCarga(usuario);
            lpa1.setUsuarioModificacion(usuario);
            em.persist(lpa1);

            // ---------- Clientes ----------
            Contacto contacto1 = new Contacto();
            contacto1.setEmail("cliente1@example.com");
            contacto1.setTelefono("0261-4123456");
            em.persist(contacto1);

            Domicilio domicilio1 = new Domicilio();
            domicilio1.setNombreCalle("San Martin");
            domicilio1.setNumeroCalle("1234");
            em.persist(domicilio1);

            Cliente cliente1 = new Cliente();
            cliente1.setCuitCuil("20-12345678-9");
            cliente1.setDenominacion("Cliente de Prueba");
            cliente1.setContacto(contacto1);
            cliente1.setDomicilio(domicilio1);
            cliente1.setFechaAlta(new Date());
            cliente1.setFechaModificacion(new Date());
            cliente1.setUsuarioCarga(usuario);
            cliente1.setUsuarioModificacion(usuario);
            em.persist(cliente1);

            Contacto contacto2 = new Contacto();
            contacto2.setEmail("cliente2@example.com");
            contacto2.setTelefono("0261-4987654");
            em.persist(contacto2);

            Domicilio domicilio2 = new Domicilio();
            domicilio2.setNombreCalle("Belgrano");
            domicilio2.setNumeroCalle("500");
            em.persist(domicilio2);

            Cliente cliente2 = new Cliente();
            cliente2.setCuitCuil("20-98765432-1");
            cliente2.setDenominacion("Comercial San Martín");
            cliente2.setContacto(contacto2);
            cliente2.setDomicilio(domicilio2);
            cliente2.setFechaAlta(new Date());
            cliente2.setFechaModificacion(new Date());
            cliente2.setUsuarioCarga(usuario);
            cliente2.setUsuarioModificacion(usuario);
            em.persist(cliente2);

            // ---------- CondicionIva y TipoMoneda ----------
            CondicionIva condicionIva = new CondicionIva();
            condicionIva.setCodigoAfip(1);
            condicionIva.setDenominacion("Responsable Inscripto");
            condicionIva.setFechaAlta(new Date());
            condicionIva.setFechaModificacion(new Date());
            condicionIva.setUsuarioCarga(usuario);
            condicionIva.setUsuarioModificacion(usuario);
            em.persist(condicionIva);

            TipoMoneda tipoMoneda = new TipoMoneda();
            tipoMoneda.setCodigoAfip("PES");
            tipoMoneda.setDenominacion("Peso Argentino");
            tipoMoneda.setSimbolo("$");
            tipoMoneda.setFechaAlta(new Date());
            tipoMoneda.setFechaModificacion(new Date());
            tipoMoneda.setUsuarioCarga(usuario);
            tipoMoneda.setUsuarioModificacion(usuario);
            em.persist(tipoMoneda);

            // ---------- 6 Facturas con datos variados ----------
            // numero, puntoVenta, cliente, importeTotal, estado, cantidadDetalle
            crearFactura(em, 1L, pv1, cliente1, usuario, condicionIva, tipoMoneda, lpa1, 150000.0, "EMITIDA", 1);
            crearFactura(em, 2L, pv2, cliente2, usuario, condicionIva, tipoMoneda, lpa1, 25000.0, "EMITIDA", 2);
            crearFactura(em, 3L, pv1, cliente1, usuario, condicionIva, tipoMoneda, lpa1, 5000.0, "PENDIENTE", 1);
            crearFactura(em, 4L, pv1, cliente2, usuario, condicionIva, tipoMoneda, lpa1, 80000.0, "EMITIDA", 3);
            crearFactura(em, 5L, pv2, cliente1, usuario, condicionIva, tipoMoneda, lpa1, 12000.0, "EMITIDA", 1);
            crearFactura(em, 6L, pv1, cliente2, usuario, condicionIva, tipoMoneda, lpa1, 3000.0, "EMITIDA", 2);

            em.getTransaction().commit();

            System.out.println("Datos de prueba cargados con éxito.");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
        }
    }

    // Método helper para no repetir 6 veces el mismo bloque de código
    private static void crearFactura(EntityManager em, Long numero, PuntoVenta pv, Cliente cliente,
                                     Usuario usuario, CondicionIva condicionIva, TipoMoneda tipoMoneda,
                                     ListaPrecioArticulo lpa, double importeTotal, String estado, int cantidad) {

        FacturaVenta factura = new FacturaVenta();
        factura.setNumero(numero);
        factura.setFechaEmision(new Date());
        factura.setPuntoVenta(pv);
        factura.setCliente(cliente);
        factura.setCondicionIva(condicionIva);
        factura.setTipoMoneda(tipoMoneda);
        factura.setImporteTotal(importeTotal);
        factura.setEstado(estado);
        factura.setFechaAlta(new Date());
        factura.setFechaModificacion(new Date());
        factura.setUsuarioCarga(usuario);
        factura.setUsuarioModificacion(usuario);

        FacturaVentaDetalle detalle = new FacturaVentaDetalle();
        detalle.setListaPrecioArticulo(lpa);
        detalle.setDescripcion("Notebook 15\"");
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(lpa.getPrecioVenta());
        detalle.setImporteSubtotal(lpa.getPrecioVenta() * cantidad);

        factura.addDetalle(detalle);

        em.persist(factura);
    }
}