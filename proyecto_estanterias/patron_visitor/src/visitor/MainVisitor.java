package visitor;

import composite.Estanteria;
import composite.Producto;
import composite.SensorIoT;
import composite.SensorIoT.Tipo;
import composite.SensorIoT.Estado;

/**
 * ============================================================
 *  DEMO — Patrón Visitor en acción
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Demuestra cómo los dos Visitantes Concretos (ReporteStock y
 * AlertaMantenimiento) recorren la misma jerarquía de estanterías
 * y productos, ejecutando operaciones completamente distintas
 * sin que las clases Estanteria o Producto hayan sido modificadas.
 */
public class MainVisitor {

    public static void main(String[] args) {

        // ── 1. Construir la jerarquía de datos ────────────────────────────

        // Estantería A-01 — Zona Farmacia
        Estanteria estA01 = new Estanteria("Estantería A-01", "Farmacia");

        estA01.agregarProducto(new Producto(
                "Acetaminofén 500mg", "FAR-001", "LabCo",   2800, 320, 50));
        estA01.agregarProducto(new Producto(
                "Ibuprofeno 400mg",   "FAR-002", "LabCo",   3500,  18, 30));  // ← stock crítico
        estA01.agregarProducto(new Producto(
                "Omeprazol 20mg",     "FAR-003", "FarmaPro",4200,  95, 40));

        // Sensores de A-01
        estA01.agregarSensor(new SensorIoT("SENS-A01-PESO-1", Tipo.PESO,         Estado.OPERATIVO, 12.4));
        estA01.agregarSensor(new SensorIoT("SENS-A01-RFID-1", Tipo.RFID,         Estado.FALLO,      0.0)); // ← fallo
        estA01.agregarSensor(new SensorIoT("SENS-A01-LED-1",  Tipo.PICK_TO_LIGHT,Estado.OPERATIVO,  1.0));

        // Estantería B-03 — Zona Autopartes
        Estanteria estB03 = new Estanteria("Estantería B-03", "Autopartes");

        estB03.agregarProducto(new Producto(
                "Filtro de aceite",   "AUT-007", "AutoDist", 45000, 12, 20));  // ← stock crítico
        estB03.agregarProducto(new Producto(
                "Pastillas de freno", "AUT-012", "AutoDist", 85000,  8, 15));  // ← stock crítico

        // Sensores de B-03
        estB03.agregarSensor(new SensorIoT("SENS-B03-PESO-1", Tipo.PESO,        Estado.OPERATIVO, 8.1));
        estB03.agregarSensor(new SensorIoT("SENS-B03-RFID-1", Tipo.RFID,        Estado.SIN_SEÑAL, 0.0)); // ← sin señal
        estB03.agregarSensor(new SensorIoT("SENS-B03-TEMP-1", Tipo.TEMPERATURA, Estado.OPERATIVO,22.3));

        // Estantería C-01 — Zona Bebidas
        Estanteria estC01 = new Estanteria("Estantería C-01", "Bebidas");

        estC01.agregarProducto(new Producto(
                "Agua 500ml x24",     "BEB-001", "AguaPura", 28000, 200, 60));
        estC01.agregarProducto(new Producto(
                "Jugo naranja 1L",    "BEB-005", "FrutaViva", 5500,  45, 30));

        // Sensores de C-01 — todos operativos
        estC01.agregarSensor(new SensorIoT("SENS-C01-PESO-1", Tipo.PESO, Estado.OPERATIVO, 50.2));
        estC01.agregarSensor(new SensorIoT("SENS-C01-RFID-1", Tipo.RFID, Estado.OPERATIVO,  1.0));

        // ── 2. Visitor #1 — ReporteStock ──────────────────────────────────
        // Un único objeto ReporteStock recorre toda la jerarquía.
        // Estanteria y Producto NO fueron modificados para soportar esto.

        System.out.println("\n>>> Aplicando Visitor: ReporteStock...\n");
        ReporteStock reporteVisitor = new ReporteStock();

        estA01.accept(reporteVisitor);   // propaga a sus 3 productos
        estB03.accept(reporteVisitor);   // propaga a sus 2 productos
        estC01.accept(reporteVisitor);   // propaga a sus 2 productos

        System.out.println(reporteVisitor.generarInforme());

        // ── 3. Visitor #2 — AlertaMantenimiento ───────────────────────────
        // El MISMO árbol de datos, visitado con una operación completamente
        // distinta. Las clases del dominio no saben qué hace cada visitor.

        System.out.println("\n>>> Aplicando Visitor: AlertaMantenimiento...\n");
        AlertaMantenimiento alertaVisitor = new AlertaMantenimiento();

        estA01.accept(alertaVisitor);
        estB03.accept(alertaVisitor);
        estC01.accept(alertaVisitor);

        System.out.println(alertaVisitor.generarReporteMantenimiento());

        // ── 4. Demostración del OCP: agregar un nuevo visitor ────────────
        // Si mañana se requiere un "Calculador de Valor Total",
        // se crea una nueva clase CalculadorValor implements InventarioVisitor.
        // Estanteria, Producto y SensorIoT NO se tocan. Esto es OCP.

        System.out.println(">>> OCP demostrado: para agregar 'CalculadorValor' o");
        System.out.println("    cualquier otra función, solo se crea una nueva clase");
        System.out.println("    que implemente InventarioVisitor. Dominio intacto. ✔");
    }
}
