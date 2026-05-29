package composite;

import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 *  PATRÓN COMPOSITE — Hoja: Producto
 *  Módulo: Datos / Contenido
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Nodo hoja de la jerarquía. No tiene hijos.
 * getCantidadTotal() y getValorTotal() operan directamente
 * sobre sus propios datos, sin delegación recursiva.
 *
 * El cliente lo trata exactamente igual que a Empresa, Bodega
 * o Estanteria — esa es la ventaja del patrón Composite.
 */
public class Producto implements InventarioComponent {

    private final String nombre;
    private final String sku;
    private final String proveedor;
    private final double precio;
    private int    stock;
    private final int    stockMinimo;

    public Producto(String nombre, String sku, String proveedor,
                    double precio, int stock, int stockMinimo) {
        this.nombre      = nombre;
        this.sku         = sku;
        this.proveedor   = proveedor;
        this.precio      = precio;
        this.stock       = stock;
        this.stockMinimo = stockMinimo;
    }

    @Override public String getNombre()       { return nombre; }
    @Override public int getCantidadTotal()   { return stock; }
    @Override public double getValorTotal()   { return stock * precio; }
    @Override public int contarProductos()    { return 1; }

    @Override public List<InventarioComponent> getHijos() {
        return Collections.emptyList();
    }

    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        String alerta = tieneStockCritico() ? " ⚠ STOCK CRÍTICO" : "";
        System.out.printf("%s[Producto] %-28s | SKU: %-10s | Stock: %4d uds | Precio: $%.0f COP%s%n",
                indent, nombre, sku, stock, precio, alerta);
    }

    public String getSku()         { return sku; }
    public String getProveedor()   { return proveedor; }
    public double getPrecio()      { return precio; }
    public int    getStock()       { return stock; }
    public int    getStockMinimo() { return stockMinimo; }

    public boolean tieneStockCritico() { return stock < stockMinimo; }

    public void actualizarStock(int nuevaCantidad) {
        if (nuevaCantidad < 0) throw new IllegalArgumentException("Stock no puede ser negativo.");
        this.stock = nuevaCantidad;
    }

    @Override
    public String toString() {
        return String.format("Producto{nombre='%s', sku='%s', stock=%d, minimo=%d}",
                nombre, sku, stock, stockMinimo);
    }
}
