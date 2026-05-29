package composite;

import visitor.InventarioVisitor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ============================================================
 *  PATRÓN VISITOR — Elemento Concreto: Estanteria
 *  Módulo: Arquitectura
 *  Proyecto: Estanterías Inteligentes INC
 * ============================================================
 *
 * Representa una unidad física de almacenamiento equipada con sensores IoT.
 * Su única responsabilidad es almacenar productos y mantener el estado
 * de sus sensores (SRP).
 *
 * Gracias al patrón Visitor, NUNCA necesita ser modificada cuando se
 * quiere agregar una nueva funcionalidad al sistema.
 * Solo expone accept() para invitar a los visitantes a operar sobre ella.
 */
public class Estanteria {

    private final String nombre;
    private final String zona;
    private final List<Producto> productos;
    private final List<SensorIoT> sensores;

    public Estanteria(String nombre, String zona) {
        this.nombre    = nombre;
        this.zona      = zona;
        this.productos = new ArrayList<>();
        this.sensores  = new ArrayList<>();
    }

    public void agregarProducto(Producto p) { productos.add(p); }
    public void agregarSensor(SensorIoT s)  { sensores.add(s); }

    /**
     * Invita al visitor a operar sobre esta estantería Y sobre cada producto
     * que contiene. Primero la estantería, luego sus productos.
     */
    public void accept(InventarioVisitor visitor) {
        visitor.visitarEstanteria(this);
        for (Producto p : productos) {
            p.accept(visitor);
        }
    }

    public String          getNombre()    { return nombre; }
    public String          getZona()      { return zona; }
    public List<Producto>  getProductos() { return Collections.unmodifiableList(productos); }
    public List<SensorIoT> getSensores()  { return Collections.unmodifiableList(sensores); }

    @Override
    public String toString() {
        return String.format("Estanteria{nombre='%s', zona='%s', productos=%d, sensores=%d}",
                nombre, zona, productos.size(), sensores.size());
    }
}
