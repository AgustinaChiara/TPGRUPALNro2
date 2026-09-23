package com.facturacion;
import com.facturacion.entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
public class ConsultasJPQL {
    public static void main(String[] args){
        System.setProperty("user.timezone", "UTC");

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("FacturacionPU");
        EntityManager em = emf.createEntityManager();


        List<FacturaVenta> facturas = em.createQuery(
                "SELECT f FROM FacturaVenta f", FacturaVenta.class)
                .getResultList();
        System.out.println("=== Consulta 1: Todas las facturas ===");
        for (FacturaVenta f : facturas) {
            System.out.println("Nro: " + f.getNumero() + " | Estado: " + f.getEstado() + " | Total: " + f.getImporteTotal());
        }

        List<Object[]> resultado = em.createQuery(
                "SELECT f.numero, f.fechaEmision, f.importeTotal FROM FacturaVenta f")
                .getResultList();

        System.out.println("=== Consulta 2: Número, fecha y total ===");
        for (Object[] fila : resultado) {
            Long numero = (Long) fila[0];
            java.util.Date fechaEmision = (java.util.Date) fila[1];
            Double importeTotal = (Double) fila[2];
            System.out.println("Nro: " + numero + " | Fecha: " + fechaEmision + " | Total: " + importeTotal);
        }

        List<Articulo> articulos = em.createQuery(
                        "SELECT a FROM Articulo a WHERE a.rubro.denominacion = :denominacion", Articulo.class)
                .setParameter("denominacion", "Electrónica")
                .getResultList();

        System.out.println("=== Consulta 3: Artículos por rubro ===");
        for (Articulo a : articulos) {
            System.out.println(a.getCodigo() + " - " + a.getDenominacion());
        }

        java.util.Date desde = new java.util.Date(2026 - 1900, 0, 1);   // 1 de enero de 2026
        java.util.Date hasta = new java.util.Date();                     // hoy

        List<FacturaVenta> facturasPorFecha = em.createQuery(
                        "SELECT f FROM FacturaVenta f WHERE f.fechaEmision BETWEEN :desde AND :hasta", FacturaVenta.class)
                .setParameter("desde", desde)
                .setParameter("hasta", hasta)
                .getResultList();

        System.out.println("=== Consulta 4: Facturas por rango de fechas ===");
        for (FacturaVenta f : facturasPorFecha) {
            System.out.println("Nro: " + f.getNumero() + " | Fecha: " + f.getFechaEmision());
        }


        List<FacturaVenta> facturasFiltradas = em.createQuery(
                        "SELECT f FROM FacturaVenta f WHERE f.estado = :estado AND f.importeTotal > :monto AND f.fechaAnulacion IS NULL",
                        FacturaVenta.class)
                .setParameter("estado", "EMITIDA")
                .setParameter("monto", 10000.0)
                .getResultList();

        System.out.println("=== Consulta 5: Facturas emitidas, > $10000, no anuladas ===");
        for (FacturaVenta f : facturasFiltradas) {
            System.out.println("Nro: " + f.getNumero() + " | Total: " + f.getImporteTotal());
        }

        List<Cliente> clientesPorTexto = em.createQuery(
                        "SELECT c FROM Cliente c WHERE LOWER(c.denominacion) LIKE LOWER(:texto) OR c.cuitCuil LIKE :prefijoCuit",
                        Cliente.class)
                .setParameter("texto", "%prueba%")
                .setParameter("prefijoCuit", "20-%")
                .getResultList();

        System.out.println("=== Consulta 6: Clientes por texto o CUIT ===");
        for (Cliente c : clientesPorTexto) {
            System.out.println(c.getDenominacion() + " - " + c.getCuitCuil());
        }


        List<String> estados = em.createQuery(
                        "SELECT DISTINCT f.estado FROM FacturaVenta f ORDER BY f.estado ASC", String.class)
                .getResultList();

        System.out.println("=== Consulta 7: Estados distintos ===");
        for (String estado : estados) {
            System.out.println(estado);
        }


        Object[] agregados = (Object[]) em.createQuery(
                        "SELECT COUNT(f), SUM(f.importeTotal), AVG(f.importeTotal) FROM FacturaVenta f")
                .getSingleResult();

        Long cantidad = (Long) agregados[0];
        Double suma = (Double) agregados[1];
        Double promedio = (Double) agregados[2];

        System.out.println("=== Consulta 8: Agregaciones ===");
        System.out.println("Cantidad: " + cantidad + " | Suma: " + suma + " | Promedio: " + promedio);



        List<Integer> numerosBuscados = java.util.Arrays.asList(1, 2, 5);

        List<PuntoVenta> puntosVenta = em.createQuery(
                        "SELECT p FROM PuntoVenta p WHERE p.numero IN :numeros", PuntoVenta.class)
                .setParameter("numeros", numerosBuscados)
                .getResultList();

        System.out.println("=== Consulta 9: Puntos de venta por lista de números ===");
        for (PuntoVenta p : puntosVenta) {
            System.out.println(p.getNumero() + " - " + p.getDescripcion());
        }


        List<FacturaVenta> facturasPorUsuario = em.createQuery(
                        "SELECT f FROM FacturaVenta f WHERE f.usuarioCarga.usuario = :nombreUsuario", FacturaVenta.class)
                .setParameter("nombreUsuario", "admin")
                .getResultList();

        System.out.println("=== Consulta 10: Facturas por usuario de carga ===");
        for (FacturaVenta f : facturasPorUsuario) {
            System.out.println("Nro: " + f.getNumero() + " | Cargada por: " + f.getUsuarioCarga().getUsuario());
        }


        List<FacturaVentaDetalle> detallesPorPuntoVenta = em.createQuery(
                        "SELECT d FROM FacturaVentaDetalle d JOIN d.factura f WHERE f.puntoVenta.numero = :numeroPV",
                        FacturaVentaDetalle.class)
                .setParameter("numeroPV", 1)
                .getResultList();

        System.out.println("=== Consulta 11: Detalles por punto de venta ===");
        for (FacturaVentaDetalle d : detallesPorPuntoVenta) {
            System.out.println(d.getDescripcion() + " - Cantidad: " + d.getCantidad());
        }



        List<Object[]> articulosConMarca = em.createQuery(
                        "SELECT a.denominacion, m.denominacion FROM Articulo a LEFT JOIN a.marca m")
                .getResultList();

        System.out.println("=== Consulta 12: Artículos con su marca (o sin marca) ===");
        for (Object[] fila : articulosConMarca) {
            String nombreArticulo = (String) fila[0];
            String nombreMarca = (String) fila[1];
            System.out.println(nombreArticulo + " - Marca: " + (nombreMarca != null ? nombreMarca : "SIN MARCA"));
        }


        List<FacturaVenta> facturasPorMarca = em.createQuery(
                        "SELECT DISTINCT f FROM FacturaVenta f " +
                                "JOIN f.detalles d " +
                                "JOIN d.listaPrecioArticulo lpa " +
                                "JOIN lpa.articulo a " +
                                "JOIN a.marca m " +
                                "WHERE m.denominacion = :marca", FacturaVenta.class)
                .setParameter("marca", "Samsung")
                .getResultList();

        System.out.println("=== Consulta 13: Facturas con detalle de una marca ===");
        for (FacturaVenta f : facturasPorMarca) {
            System.out.println("Nro: " + f.getNumero());
        }



        List<FacturaVenta> facturasSobrePromedio = em.createQuery(
                        "SELECT f FROM FacturaVenta f WHERE f.importeTotal > " +
                                "(SELECT AVG(f2.importeTotal) FROM FacturaVenta f2)", FacturaVenta.class)
                .getResultList();

        System.out.println("=== Consulta 14: Facturas por encima del promedio ===");
        for (FacturaVenta f : facturasSobrePromedio) {
            System.out.println("Nro: " + f.getNumero() + " | Total: " + f.getImporteTotal());
        }


        List<Object[]> resumenPorPuntoVenta = em.createQuery(
                        "SELECT p.descripcion, COUNT(f), SUM(f.importeTotal) " +
                                "FROM FacturaVenta f JOIN f.puntoVenta p " +
                                "GROUP BY p.descripcion")
                .getResultList();

        System.out.println("=== Consulta 15: Resumen por punto de venta ===");
        for (Object[] fila : resumenPorPuntoVenta) {
            String descripcion = (String) fila[0];
            Long cantidadFacturasPV = (Long) fila[1];
            Double sumaImportePV = (Double) fila[2];
            System.out.println(descripcion + " | Facturas: " + cantidadFacturasPV + " | Total: " + sumaImportePV);
        }


        List<String> usuariosConMuchasFacturas = em.createQuery(
                        "SELECT u.nombre FROM FacturaVenta f JOIN f.usuarioCarga u " +
                                "GROUP BY u.nombre " +
                                "HAVING COUNT(f) > :minimo", String.class)
                .setParameter("minimo", 5L)
                .getResultList();

        System.out.println("=== Consulta 16: Usuarios con más de 5 facturas ===");
        for (String nombre : usuariosConMuchasFacturas) {
            System.out.println(nombre);
        }



        List<Object[]> ventasPorMarca = em.createQuery(
                        "SELECT m.denominacion, SUM(d.cantidad), SUM(d.importeSubtotal) " +
                                "FROM FacturaVentaDetalle d " +
                                "JOIN d.listaPrecioArticulo lpa " +
                                "JOIN lpa.articulo a " +
                                "JOIN a.marca m " +
                                "GROUP BY m.denominacion")
                .getResultList();

        System.out.println("=== Consulta 17: Ventas por marca ===");
        for (Object[] fila : ventasPorMarca) {
            String marca = (String) fila[0];
            Double unidades = (Double) fila[1];
            Double subtotal = (Double) fila[2];
            System.out.println(marca + " | Unidades: " + unidades + " | Subtotal: " + subtotal);
        }


        List<Marca> marcasConVentas = em.createQuery(
                        "SELECT m FROM Marca m WHERE EXISTS (" +
                                "SELECT d FROM FacturaVentaDetalle d " +
                                "WHERE d.listaPrecioArticulo.articulo.marca = m)", Marca.class)
                .getResultList();

        System.out.println("=== Consulta 18: Marcas con al menos una venta ===");
        for (Marca m : marcasConVentas) {
            System.out.println(m.getDenominacion());
        }



        List<Articulo> articulosSinVentas = em.createQuery(
                        "SELECT a FROM Articulo a WHERE NOT EXISTS (" +
                                "SELECT d FROM FacturaVentaDetalle d " +
                                "WHERE d.listaPrecioArticulo.articulo = a)", Articulo.class)
                .getResultList();

        System.out.println("=== Consulta 19: Artículos nunca vendidos ===");
        for (Articulo a : articulosSinVentas) {
            System.out.println(a.getCodigo() + " - " + a.getDenominacion());
        }



        List<Object[]> facturasClasificadas = em.createQuery(
                        "SELECT f.numero, f.importeTotal, " +
                                "CASE " +
                                "  WHEN f.importeTotal > 50000 THEN 'ALTO VALOR' " +
                                "  WHEN f.importeTotal BETWEEN 10000 AND 50000 THEN 'MEDIO VALOR' " +
                                "  ELSE 'BAJO VALOR' " +
                                "END " +
                                "FROM FacturaVenta f " +
                                "ORDER BY f.importeTotal DESC")
                .getResultList();

        System.out.println("=== Consulta 20: Facturas clasificadas por valor ===");
        for (Object[] fila : facturasClasificadas) {
            Long numero = (Long) fila[0];
            Double importeTotal = (Double) fila[1];
            String categoria = (String) fila[2];
            System.out.println("Nro: " + numero + " | Total: " + importeTotal + " | Categoría: " + categoria);
        }



        em.close();
        emf.close();

    }
}
