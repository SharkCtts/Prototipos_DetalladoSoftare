package composite;

import java.util.List;

/**
 * ============================================================
 *  PATRÓN COMPOSITE — Interfaz Component
 *  Módulo: Datos / Contenido
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * PROBLEMA QUE RESUELVE:
 * El sistema maneja una jerarquía de 4 niveles: Empresa → Bodega →
 * Estanteria → Producto. Sin este patrón, el código cliente necesitaría
 * tratar cada nivel con lógica diferente (ifs, instanceof, switch),
 * lo que hace el sistema frágil y difícil de extender.
 *
 * SOLUCIÓN:
 * Todos los nodos — desde Empresa (raíz) hasta Producto (hoja) —
 * implementan esta misma interfaz. El cliente los trata de forma
 * uniforme sin importar si es un nodo simple o uno que contiene miles
 * de productos anidados.
 *
 * Autores:  Ana María Bautista Rodríguez (95429)
 *           Juan Andrés Veintinilla (124596)
 * USB Cali — Diseño Detallado de Software 2026-1
 */
public interface InventarioComponent {

    /** Nombre descriptivo del nodo. */
    String getNombre();

    /**
     * Cantidad total de unidades.
     * Hojas: retorna su propio stock.
     * Compuestos: suma recursiva de todos sus hijos.
     */
    int getCantidadTotal();

    /**
     * Valor monetario total en COP.
     * Hojas: stock × precio.
     * Compuestos: suma recursiva de todos sus hijos.
     */
    double getValorTotal();

    /**
     * Número de productos (hojas) contenidos en este nodo.
     * Permite saber cuántos SKUs distintos maneja cualquier nivel.
     */
    int contarProductos();

    /**
     * Hijos directos de este nodo.
     * Las hojas (Producto) retornan lista vacía.
     * Los compuestos retornan sus hijos inmediatos.
     */
    List<InventarioComponent> getHijos();

    /**
     * Muestra la información del nodo con indentación
     * para visualizar la jerarquía en consola.
     *
     * @param nivel profundidad actual en el árbol (0 = raíz)
     */
    void mostrar(int nivel);
}
