package visitor;

import composite.Estanteria;
import composite.Producto;
import composite.SensorIoT;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 *  VISITANTE CONCRETO #2 — AlertaMantenimiento
 *  Módulo: Arquitectura
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * PROPÓSITO:
 * Recorre todas las estanterías revisando el estado de los sensores IoT
 * instalados en cada una. Si detecta sensores con fallo o sin señal,
 * genera alertas técnicas para el equipo de mantenimiento.
 *
 * CLAVE DEL PATRÓN:
 * Esta funcionalidad existe COMPLETAMENTE AQUÍ, en esta clase.
 * La clase Estanteria no tiene idea de cómo revisar sus propios sensores
 * para mantenimiento — eso es responsabilidad de este visitor.
 * Si el día de mañana se quiere enviar las alertas por correo o SMS,
 * solo se modifica este archivo, sin tocar Estanteria ni Producto.
 *
 * Autores:  Ana María Bautista Rodríguez (95429)
 *           Juan Andrés Veintinilla (124596)
 * USB Cali — Diseño Detallado de Software 2026-1
 */
public class AlertaMantenimiento implements InventarioVisitor {

    // Registro de fallos detectados durante el recorrido
    private final List<String> alertas = new ArrayList<>();
    private int estanteriasRevisadas  = 0;
    private int sensoresRevisados     = 0;
    private int sensoresConFallo      = 0;

    // ── Métodos visit ─────────────────────────────────────────────────────

    /**
     * Al visitar una Estanteria, revisa el estado de TODOS sus sensores IoT.
     * Si alguno está fallando, registra una alerta técnica con el detalle.
     *
     * Nota: los productos de esta estantería serán visitados automáticamente
     * por accept() de Estanteria, después de que este método retorne.
     */
    @Override
    public void visitarEstanteria(Estanteria estanteria) {
        estanteriasRevisadas++;

        for (SensorIoT sensor : estanteria.getSensores()) {
            sensoresRevisados++;

            if (sensor.estaFallando()) {
                sensoresConFallo++;
                String alerta = String.format(
                        "[FALLO] Estantería '%s' (zona: %s) | Sensor: %s | Tipo: %s | Estado: %s | Última lectura: %.2f",
                        estanteria.getNombre(),
                        estanteria.getZona(),
                        sensor.getId(),
                        sensor.getTipo(),
                        sensor.getEstado(),
                        sensor.getUltimaLectura()
                );
                alertas.add(alerta);
            }
        }
    }

    /**
     * Al visitar un Producto, este visitor no realiza ninguna acción.
     * AlertaMantenimiento solo le interesa el estado de los sensores
     * de la estantería, no los datos de cada artículo.
     *
     * Este método se deja vacío intencionalmente — eso es válido en Visitor:
     * cada visitante implementa solo los métodos que le son relevantes.
     */
    @Override
    public void visitarProducto(Producto producto) {
        // Este visitor no opera sobre productos individuales.
        // Solo le interesan los sensores de las estanterías.
    }

    // ── Generación del reporte de mantenimiento ───────────────────────────

    /**
     * Retorna el reporte técnico de mantenimiento.
     * Debe llamarse después de haber pasado este visitor por todos los nodos.
     *
     * @return string con el reporte de fallos de sensores
     */
    public String generarReporteMantenimiento() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append("     ALERTA DE MANTENIMIENTO — Sensores IoT         \n");
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append(String.format("  Estanterías revisadas : %d\n", estanteriasRevisadas));
        sb.append(String.format("  Sensores revisados    : %d\n", sensoresRevisados));
        sb.append(String.format("  Sensores con fallo    : %d\n", sensoresConFallo));
        sb.append("───────────────────────────────────────────────────\n");

        if (alertas.isEmpty()) {
            sb.append("  ✔ Todos los sensores operan correctamente.\n");
        } else {
            sb.append("  ⚠ Fallos detectados — requieren atención inmediata:\n\n");
            for (String alerta : alertas) {
                sb.append("  ").append(alerta).append("\n");
            }
        }

        sb.append("═══════════════════════════════════════════════════\n");
        return sb.toString();
    }

    // ── Getters para uso programático ─────────────────────────────────────

    public List<String> getAlertas()           { return alertas; }
    public boolean      hayFallos()            { return !alertas.isEmpty(); }
    public int          getSensoresConFallo()  { return sensoresConFallo; }
}
