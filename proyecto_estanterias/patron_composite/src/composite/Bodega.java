package composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 *  PATRÓN COMPOSITE — Compuesto: Bodega
 *  Módulo: Datos / Contenido
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Nodo compuesto de segundo nivel. Agrupa Estanterías.
 * Corresponde a la entidad gestionada por la API_Bodega del sistema.
 */
public class Bodega implements InventarioComponent {

    private final String nombre;
    private final String ciudad;
    private final List<InventarioComponent> estanterias = new ArrayList<>();

    public Bodega(String nombre, String ciudad) {
        this.nombre  = nombre;
        this.ciudad  = ciudad;
    }

    // ── Gestión de hijos ──────────────────────────────────────────────────

    public void agregarEstanteria(Estanteria e) { estanterias.add(e); }
    public void quitarEstanteria(Estanteria e)  { estanterias.remove(e); }

    // ── InventarioComponent ───────────────────────────────────────────────

    @Override public String getNombre() { return nombre; }

    @Override
    public int getCantidadTotal() {
        return estanterias.stream().mapToInt(InventarioComponent::getCantidadTotal).sum();
    }

    @Override
    public double getValorTotal() {
        return estanterias.stream().mapToDouble(InventarioComponent::getValorTotal).sum();
    }

    @Override
    public int contarProductos() {
        return estanterias.stream().mapToInt(InventarioComponent::contarProductos).sum();
    }

    @Override
    public List<InventarioComponent> getHijos() {
        return Collections.unmodifiableList(estanterias);
    }

    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.printf("%s[Bodega] %s | Ciudad: %s | Estanterías: %d | Productos: %d | Valor: $%.0f COP%n",
                indent, nombre, ciudad, estanterias.size(), contarProductos(), getValorTotal());
        for (InventarioComponent hijo : estanterias) {
            hijo.mostrar(nivel + 1);
        }
    }

    // ── Getters propios ───────────────────────────────────────────────────

    public String getCiudad() { return ciudad; }

    @Override
    public String toString() {
        return String.format("Bodega{nombre='%s', ciudad='%s', estanterias=%d, productos=%d}",
                nombre, ciudad, estanterias.size(), contarProductos());
    }
}
