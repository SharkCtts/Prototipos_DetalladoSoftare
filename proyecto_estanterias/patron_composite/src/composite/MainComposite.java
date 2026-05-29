package composite;

/**
 * ============================================================
 *  DEMO — Patrón Composite en acción
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Demuestra cómo el cliente (este main) trata de forma uniforme
 * todos los niveles de la jerarquía sin usar instanceof ni switch.
 * La misma llamada (getCantidadTotal, getValorTotal, mostrar)
 * funciona en Empresa, Bodega, Estanteria y Producto.
 */
public class MainComposite {

    public static void main(String[] args) {

        // ── 1. Construir el árbol de inventario ───────────────────────────

        // Hojas — Estantería A-01 (Farmacia)
        Estanteria estA01 = new Estanteria("Estantería A-01", "Farmacia");
        estA01.agregarProducto(new Producto("Acetaminofén 500mg", "FAR-001", "LabCo",    2800, 320, 50));
        estA01.agregarProducto(new Producto("Ibuprofeno 400mg",   "FAR-002", "LabCo",    3500,  18, 30));
        estA01.agregarProducto(new Producto("Omeprazol 20mg",     "FAR-003", "FarmaPro", 4200,  95, 40));

        // Hojas — Estantería B-03 (Autopartes)
        Estanteria estB03 = new Estanteria("Estantería B-03", "Autopartes");
        estB03.agregarProducto(new Producto("Filtro de aceite",   "AUT-007", "AutoDist", 45000, 12, 20));
        estB03.agregarProducto(new Producto("Pastillas de freno", "AUT-012", "AutoDist", 85000,  8, 15));

        // Hojas — Estantería C-01 (Bebidas)
        Estanteria estC01 = new Estanteria("Estantería C-01", "Bebidas");
        estC01.agregarProducto(new Producto("Agua 500ml x24",  "BEB-001", "AguaPura",  28000, 200, 60));
        estC01.agregarProducto(new Producto("Jugo naranja 1L", "BEB-005", "FrutaViva",  5500,  45, 30));

        // Compuestos — Bodegas
        Bodega bodegaCentral = new Bodega("Bodega Central Cali", "Cali");
        bodegaCentral.agregarEstanteria(estA01);
        bodegaCentral.agregarEstanteria(estB03);

        Bodega darkStoreNorte = new Bodega("Dark Store Norte", "Cali");
        darkStoreNorte.agregarEstanteria(estC01);

        // Raíz — Empresa
        Empresa empresa = new Empresa("Estanterías Inteligentes INC", "900-123-456-7");
        empresa.agregarBodega(bodegaCentral);
        empresa.agregarBodega(darkStoreNorte);

        // ── 2. El cliente usa la interfaz uniforme ─────────────────────────
        // La misma llamada funciona en cualquier nivel de la jerarquía.

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("  ÁRBOL COMPLETO DE INVENTARIO (Empresa raíz)  ");
        System.out.println("═══════════════════════════════════════════════");
        empresa.mostrar(0);

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("  MÉTRICAS CONSOLIDADAS — Nodo Empresa          ");
        System.out.println("═══════════════════════════════════════════════");
        System.out.printf("  Total unidades : %d uds%n",   empresa.getCantidadTotal());
        System.out.printf("  Total SKUs     : %d%n",       empresa.contarProductos());
        System.out.printf("  Valor total    : $%.0f COP%n",empresa.getValorTotal());

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("  MÉTRICAS POR BODEGA (mismo método, otro nodo) ");
        System.out.println("═══════════════════════════════════════════════");
        // El cliente no necesita saber que bodegaCentral contiene estanterías.
        // Llama los mismos métodos que llamaría sobre un Producto.
        System.out.printf("  %s: %d uds | $%.0f COP%n",
                bodegaCentral.getNombre(),
                bodegaCentral.getCantidadTotal(),
                bodegaCentral.getValorTotal());
        System.out.printf("  %s: %d uds | $%.0f COP%n",
                darkStoreNorte.getNombre(),
                darkStoreNorte.getCantidadTotal(),
                darkStoreNorte.getValorTotal());

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("  MÉTRICAS DE UNA ESTANTERÍA INDIVIDUAL         ");
        System.out.println("═══════════════════════════════════════════════");
        System.out.printf("  %s: %d uds | $%.0f COP%n",
                estA01.getNombre(), estA01.getCantidadTotal(), estA01.getValorTotal());

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("  ESCALABILIDAD: agregar una nueva bodega       ");
        System.out.println("═══════════════════════════════════════════════");
        Bodega nuevaBodega = new Bodega("Centro de Distribución Sur", "Bogotá");
        Estanteria estNueva = new Estanteria("Estantería D-01", "Medicamentos");
        estNueva.agregarProducto(new Producto("Ampicilina 500mg", "MED-001", "FarmaBog", 8500, 150, 40));
        nuevaBodega.agregarEstanteria(estNueva);
        empresa.agregarBodega(nuevaBodega);

        // El valor consolidado de la empresa se actualiza automáticamente.
        System.out.printf("  Valor total empresa (con nueva bodega): $%.0f COP%n",
                empresa.getValorTotal());
        System.out.println("  → Se agregó un nivel completo sin cambiar ningún código existente. ✔");
    }
}
