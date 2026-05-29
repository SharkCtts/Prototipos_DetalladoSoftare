package composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 *  PATRÓN COMPOSITE — Compuesto: Estanteria
 *  Módulo: Datos / Contenido
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Nodo compuesto de tercer nivel. Contiene Productos (hojas).
 * getCantidadTotal() y getValorTotal() delegan a sus hijos y suman
 * los resultados — esto es el núcleo del patrón Composite.
 *
 * Físicamente representa una unidad de almacenamiento con sensores IoT
 * dentro de una Bodega.
 */
public class Estanteria implements InventarioComponent {

    private String nombre;
    private final String zona;
    private final List<InventarioComponent> productos = new ArrayList<>();

    public Estanteria(String nombre, String zona) {
        this.nombre = nombre;
        this.zona   = zona;
    }

    // ── Gestión de hijos ──────────────────────────────────────────────────

    public void agregarProducto(Producto p) { productos.add(p); }
    public void quitarProducto(Producto p)  { productos.remove(p); }

    // ── InventarioComponent ───────────────────────────────────────────────

    @Override public String getNombre() { return nombre; }

    @Override public void setNombre(String nombre) { this.nombre = nombre; }

    /** Delega a cada producto hijo y suma. El cliente no nota la diferencia. */
    @Override
    public int getCantidadTotal() {
        return productos.stream().mapToInt(InventarioComponent::getCantidadTotal).sum();
    }

    @Override
    public double getValorTotal() {
        return productos.stream().mapToDouble(InventarioComponent::getValorTotal).sum();
    }

    @Override
    public int contarProductos() {
        return productos.stream().mapToInt(InventarioComponent::contarProductos).sum();
    }

    @Override
    public List<InventarioComponent> getHijos() {
        return Collections.unmodifiableList(productos);
    }

    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.printf("%s[Estantería] %s | Zona: %s | SKUs: %d | Stock total: %d uds | Valor: $%.0f COP%n",
                indent, nombre, zona, contarProductos(), getCantidadTotal(), getValorTotal());
        for (InventarioComponent hijo : productos) {
            hijo.mostrar(nivel + 1);
        }
    }

    // ── Getters propios ───────────────────────────────────────────────────

    public String getZona() { return zona; }

    @Override
    public String toString() {
        return String.format("Estanteria{nombre='%s', zona='%s', productos=%d}",
                nombre, zona, contarProductos());
    }
}
