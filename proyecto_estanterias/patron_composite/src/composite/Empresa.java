package composite;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 *  PATRÓN COMPOSITE — Compuesto raíz: Empresa
 *  Módulo: Datos / Contenido
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Nodo raíz de la jerarquía. Agrupa todas las Bodegas del cliente.
 * Una sola llamada a getCantidadTotal() o getValorTotal() sobre la
 * Empresa retorna el consolidado de absolutamente todo el inventario,
 * recorriendo la jerarquía recursivamente sin que el cliente escriba
 * una sola línea de código adicional.
 *
 * Corresponde a la entidad gestionada por la API_Empresas del sistema.
 */
public class Empresa implements InventarioComponent {

    private final String nombre;
    private final String nit;
    private final List<InventarioComponent> bodegas = new ArrayList<>();

    public Empresa(String nombre, String nit) {
        this.nombre = nombre;
        this.nit    = nit;
    }

    // ── Gestión de hijos ──────────────────────────────────────────────────

    public void agregarBodega(Bodega b) { bodegas.add(b); }
    public void quitarBodega(Bodega b)  { bodegas.remove(b); }

    // ── InventarioComponent ───────────────────────────────────────────────

    @Override public String getNombre() { return nombre; }

    /** Un único getCantidadTotal() consolida todo el inventario de la empresa. */
    @Override
    public int getCantidadTotal() {
        return bodegas.stream().mapToInt(InventarioComponent::getCantidadTotal).sum();
    }

    @Override
    public double getValorTotal() {
        return bodegas.stream().mapToDouble(InventarioComponent::getValorTotal).sum();
    }

    @Override
    public int contarProductos() {
        return bodegas.stream().mapToInt(InventarioComponent::contarProductos).sum();
    }

    @Override
    public List<InventarioComponent> getHijos() {
        return Collections.unmodifiableList(bodegas);
    }

    /**
     * Imprime el árbol completo de inventario con indentación jerárquica.
     * Llamar con nivel=0 desde el código cliente.
     */
    @Override
    public void mostrar(int nivel) {
        String indent = "  ".repeat(nivel);
        System.out.printf("%s[Empresa] %s | NIT: %s | Bodegas: %d | Productos: %d | Valor total: $%.0f COP%n",
                indent, nombre, nit, bodegas.size(), contarProductos(), getValorTotal());
        for (InventarioComponent hijo : bodegas) {
            hijo.mostrar(nivel + 1);
        }
    }

    // ── Getters propios ───────────────────────────────────────────────────

    public String getNit() { return nit; }

    @Override
    public String toString() {
        return String.format("Empresa{nombre='%s', nit='%s', bodegas=%d, productos=%d, valor=$%.0f COP}",
                nombre, nit, bodegas.size(), contarProductos(), getValorTotal());
    }
}
