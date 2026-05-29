package composite;

import visitor.InventarioVisitor;

/**
 * ============================================================
 *  PATRÓN VISITOR — Elemento Concreto: Producto
 *  Módulo: Arquitectura
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Esta clase representa un artículo del inventario.
 * Su única responsabilidad es almacenar los datos del producto (SRP).
 *
 * Gracias al patrón Visitor, esta clase NUNCA necesita ser modificada
 * cuando se agrega una nueva funcionalidad (reporte, alerta, etc.).
 * Solo expone accept() para que los visitantes puedan operar sobre ella.
 */
public class Producto {

    private String nombre;
    private final String sku;
    private final String proveedor;
    private final double precio;
    private int stock;
    private final int stockMinimo;

    /**
     * @param nombre      nombre descriptivo del artículo
     * @param sku         código de referencia único
     * @param proveedor   proveedor habitual del artículo
     * @param precio      precio unitario en COP
     * @param stock       unidades disponibles actualmente
     * @param stockMinimo umbral mínimo antes de generar alerta
     */
    public Producto(String nombre, String sku, String proveedor,
                    double precio, int stock, int stockMinimo) {
        this.nombre      = nombre;
        this.sku         = sku;
        this.proveedor   = proveedor;
        this.precio      = precio;
        this.stock       = stock;
        this.stockMinimo = stockMinimo;
    }

    // ── Punto de entrada para el Visitor ──────────────────────────────────

    /**
     * Invita al visitor a operar sobre este producto.
     * El visitor decide qué hace: acumular datos, verificar stock, etc.
     * Esta clase NO necesita saber qué hace el visitor (SRP cumplido).
     *
     * @param visitor cualquier visitante que implemente InventarioVisitor
     */
    public void accept(InventarioVisitor visitor) {
        visitor.visitarProducto(this);
    }

    // ── Getters ───────────────────────────────────────────────────────────

    public String getNombre()     { return nombre; }
    public void   setNombre(String nombre) { this.nombre = nombre; }
    public String getSku()        { return sku; }
    public String getProveedor()  { return proveedor; }
    public double getPrecio()      { return precio; }
    public int    getStock()       { return stock; }
    public int    getStockMinimo() { return stockMinimo; }
    public double getValorTotal()  { return stock * precio; }

    /** true si el stock actual está por debajo del mínimo configurado. */
    public boolean tieneStockCritico() {
        return stock < stockMinimo;
    }

    /** Actualiza el stock tras una lectura del sensor IoT de la estantería. */
    public void actualizarStock(int nuevaCantidad) {
        if (nuevaCantidad < 0) throw new IllegalArgumentException("Stock no puede ser negativo.");
        this.stock = nuevaCantidad;
    }

    @Override
    public String toString() {
        return String.format("Producto{nombre='%s', sku='%s', stock=%d, minimo=%d, precio=%.0f COP}",
                nombre, sku, stock, stockMinimo, precio);
    }
}
