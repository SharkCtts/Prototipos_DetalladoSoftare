package visitor;

import composite.Estanteria;
import composite.Producto;

/**
 * ============================================================
 *  PATRÓN VISITOR — Interfaz principal
 *  Módulo: Arquitectura
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * PROBLEMA QUE RESUELVE:
 * Queremos agregar funcionalidades constantemente (Calculador de Alertas,
 * Generador de Reportes, etc.) sin modificar las clases Estanteria o
 * Producto cada vez que se nos ocurra una nueva función.
 * Esto protege el Principio de Responsabilidad Única (SRP): Estanteria
 * y Producto solo saben almacenar sus datos, no saben generar reportes
 * ni revisar sensores.
 *
 * SOLUCIÓN:
 * Esta interfaz define el "contrato" que debe cumplir cualquier nueva
 * funcionalidad. Cada vez que se quiera agregar una operación nueva,
 * se crea una clase que implemente este contrato — sin tocar el dominio.
 *
 * Autores:  Ana María Bautista Rodríguez (95429)
 *           Juan Andrés Veintinilla (124596)
 * USB Cali — Diseño Detallado de Software 2026-1
 */
public interface InventarioVisitor {

    /**
     * Opera sobre un nodo Producto de la jerarquía.
     * Cada Visitante Concreto decide qué hace con los datos del producto:
     * acumularlos para un reporte, verificar su stock, etc.
     *
     * @param producto hoja de la jerarquía Composite
     */
    void visitarProducto(Producto producto);

    /**
     * Opera sobre un nodo Estanteria de la jerarquía.
     * Cada Visitante Concreto decide qué hace con la estantería:
     * revisar sus sensores IoT, calcular su valor total, etc.
     *
     * @param estanteria nodo compuesto que contiene productos y sensores IoT
     */
    void visitarEstanteria(Estanteria estanteria);
}
