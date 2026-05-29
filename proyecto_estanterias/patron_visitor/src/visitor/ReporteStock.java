package visitor;

import composite.Estanteria;
import composite.Producto;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *  VISITANTE CONCRETO #1 — ReporteStock
 *  Módulo: Arquitectura
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * PROPÓSITO:
 * Recorre toda la jerarquía (estanterías y productos) acumulando
 * datos para generar un informe completo del estado del inventario.
 *
 * CLAVE DEL PATRÓN:
 * Esta funcionalidad existe COMPLETAMENTE AQUÍ, en esta clase.
 * Ni Estanteria ni Producto saben nada de reportes.
 * Si mañana el formato del reporte cambia, solo se modifica este archivo.
 *
 * Autores:  Ana María Bautista Rodríguez (95429)
 *           Juan Andrés Veintinilla (124596)
 * USB Cali — Diseño Detallado de Software 2026-1
 */
public class ReporteStock implements InventarioVisitor {

    // Acumuladores internos del reporte
    private final StringBuilder reporte = new StringBuilder();
    private final List<Producto> productosCriticos = new ArrayList<>();
    private int totalProductos  = 0;
    private int totalUnidades   = 0;
    private double valorTotal   = 0.0;

    // ── Métodos visit ─────────────────────────────────────────────────────

    /**
     * Al visitar una Estanteria, escribe el encabezado de sección.
     * Los productos de esa estantería serán visitados en accept() de Estanteria,
     * justo después de que este método retorne.
     */
    @Override
    public void visitarEstanteria(Estanteria estanteria) {
        reporte.append("\n")
               .append("┌─────────────────────────────────────────────┐\n")
               .append(String.format("│  Estantería : %-30s│\n", estanteria.getNombre()))
               .append(String.format("│  Zona       : %-30s│\n", estanteria.getZona()))
               .append("└─────────────────────────────────────────────┘\n");
    }

    /**
     * Al visitar un Producto, acumula sus datos en el reporte
     * y lo agrega a la lista de críticos si su stock está bajo.
     */
    @Override
    public void visitarProducto(Producto producto) {
        totalProductos++;
        totalUnidades += producto.getStock();
        valorTotal    += producto.getValorTotal();

        String estadoStock = producto.tieneStockCritico()
                ? "⚠ CRÍTICO"
                : "✔ OK";

        reporte.append(String.format(
                "  %-28s | SKU: %-10s | Stock: %4d uds | Mín: %3d | Estado: %s\n",
                producto.getNombre(),
                producto.getSku(),
                producto.getStock(),
                producto.getStockMinimo(),
                estadoStock
        ));

        if (producto.tieneStockCritico()) {
            productosCriticos.add(producto);
        }
    }

    // ── Generación del informe final ──────────────────────────────────────

    /**
     * Retorna el informe completo formateado como texto.
     * Debe llamarse después de haber pasado este visitor por todos los
     * nodos de la jerarquía (via estanteria.accept(this)).
     *
     * @return string con el reporte completo del inventario
     */
    public String generarInforme() {
        StringBuilder informe = new StringBuilder();
        informe.append("═══════════════════════════════════════════════════\n");
        informe.append("         REPORTE DE STOCK — Estanterías INC        \n");
        informe.append("═══════════════════════════════════════════════════\n");
        informe.append(reporte);
        informe.append("\n───────────────────────────────────────────────────\n");
        informe.append(String.format("  Total de productos  : %d SKUs distintos\n", totalProductos));
        informe.append(String.format("  Total de unidades   : %d uds\n", totalUnidades));
        informe.append(String.format("  Valor total         : $%.0f COP\n", valorTotal));
        informe.append(String.format("  Productos críticos  : %d\n", productosCriticos.size()));

        if (!productosCriticos.isEmpty()) {
            informe.append("\n  ⚠ Productos que requieren reabastecimiento:\n");
            for (Producto p : productosCriticos) {
                informe.append(String.format(
                        "    → %-28s (stock: %d / mínimo: %d)\n",
                        p.getNombre(), p.getStock(), p.getStockMinimo()
                ));
            }
        }

        informe.append("═══════════════════════════════════════════════════\n");
        return informe.toString();
    }

    // ── Getters para uso programático ─────────────────────────────────────

    public int              getTotalProductos()    { return totalProductos; }
    public int              getTotalUnidades()      { return totalUnidades; }
    public double           getValorTotal()         { return valorTotal; }
    public List<Producto>   getProductosCriticos()  { return productosCriticos; }
}
