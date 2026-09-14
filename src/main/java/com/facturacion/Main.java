package com.facturacion;

import com.facturacion.entities.Articulo;
import com.facturacion.entities.FacturaVenta;
import com.facturacion.entities.FacturaVentaDetalle;
import com.facturacion.entities.ListaPrecio;
import com.facturacion.entities.ListaPrecioArticulo;
import com.facturacion.entities.PuntoVenta;
import com.facturacion.entities.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Date;
import com.facturacion.entities.Cliente;
import com.facturacion.entities.Contacto;
import com.facturacion.entities.Domicilio;
import com.facturacion.entities.CondicionIva;
import com.facturacion.entities.TipoMoneda;

public class Main {

    public static void main(String[] args) {

        System.setProperty("user.timezone", "UTC");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();

        try {
            em.getTransaction().begin();

            Usuario usuario = new Usuario();
            usuario.setUsuario("admin");
            usuario.setClave("1234");
            usuario.setNombre("Ana");
            usuario.setApellido("Perez");
            em.persist(usuario);

            PuntoVenta puntoVenta = new PuntoVenta();
            puntoVenta.setNumero(1);
            puntoVenta.setDescripcion("Casa Central");
            puntoVenta.setTipoEmision("Electronica");
            puntoVenta.setDomicilioComercial("Av. Siempre Viva 123");
            puntoVenta.setFechaAlta(new Date());
            puntoVenta.setFechaModificacion(new Date());
            puntoVenta.setUsuarioCarga(usuario);
            puntoVenta.setUsuarioModificacion(usuario);
            em.persist(puntoVenta);

            Articulo articulo = new Articulo();
            articulo.setCodigo("ART-001");
            articulo.setDenominacion("Notebook 15\"");
            articulo.setFechaAlta(new Date());
            articulo.setFechaModificacion(new Date());
            articulo.setUsuarioCarga(usuario);
            articulo.setUsuarioModificacion(usuario);
            em.persist(articulo);

            ListaPrecio listaPrecio = new ListaPrecio();
            listaPrecio.setCodigo("LP-GRAL");
            listaPrecio.setDenominacion("Lista General");
            listaPrecio.setFechaAlta(new Date());
            listaPrecio.setFechaModificacion(new Date());
            listaPrecio.setUsuarioCarga(usuario);
            listaPrecio.setUsuarioModificacion(usuario);
            em.persist(listaPrecio);

            ListaPrecioArticulo listaPrecioArticulo = new ListaPrecioArticulo();
            listaPrecioArticulo.setListaPrecio(listaPrecio);
            listaPrecioArticulo.setArticulo(articulo);
            listaPrecioArticulo.setPrecioVenta(150000.0);
            listaPrecioArticulo.setFechaAlta(new Date());
            listaPrecioArticulo.setFechaModificacion(new Date());
            listaPrecioArticulo.setUsuarioCarga(usuario);
            listaPrecioArticulo.setUsuarioModificacion(usuario);
            em.persist(listaPrecioArticulo);

            Contacto contacto = new Contacto();
            contacto.setEmail("cliente@example.com");
            contacto.setTelefono("0261-4123456");
            contacto.setCelular("261-5551234");
            em.persist(contacto);

            Domicilio domicilio = new Domicilio();
            domicilio.setNombreCalle("San Martin");
            domicilio.setNumeroCalle("1234");
            em.persist(domicilio);

            Cliente cliente = new Cliente();
            cliente.setCuitCuil("20-12345678-9");
            cliente.setDenominacion("Cliente de Prueba");
            cliente.setContacto(contacto);
            cliente.setDomicilio(domicilio);
            cliente.setFechaAlta(new Date());
            cliente.setFechaModificacion(new Date());
            cliente.setUsuarioCarga(usuario);
            cliente.setUsuarioModificacion(usuario);
            em.persist(cliente);

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

            FacturaVenta facturaVenta = new FacturaVenta();
            facturaVenta.setNumero(1L);
            facturaVenta.setFechaEmision(new Date());
            facturaVenta.setPuntoVenta(puntoVenta);
            facturaVenta.setCliente(cliente);
            facturaVenta.setCondicionIva(condicionIva);
            facturaVenta.setTipoMoneda(tipoMoneda);
            facturaVenta.setImporteTotal(150000.0);
            facturaVenta.setEstado("PENDIENTE");
            facturaVenta.setFechaAlta(new Date());
            facturaVenta.setFechaModificacion(new Date());
            facturaVenta.setUsuarioCarga(usuario);
            facturaVenta.setUsuarioModificacion(usuario);

            FacturaVentaDetalle detalle1 = new FacturaVentaDetalle();
            detalle1.setListaPrecioArticulo(listaPrecioArticulo);
            detalle1.setDescripcion("Notebook 15\"");
            detalle1.setCantidad(1);
            detalle1.setPrecioUnitario(150000.0);
            detalle1.setImporteSubtotal(150000.0);

            facturaVenta.addDetalle(detalle1);


            em.persist(facturaVenta);

            em.getTransaction().commit();

            System.out.println("Factura persistida con id=" + facturaVenta.getId()
                    + " y " + facturaVenta.getDetalles().size() + " detalle(s).");

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
}