package composite;

/**
 * Representa un sensor IoT instalado en una Estanteria.
 *
 * Cada estantería puede tener múltiples sensores (peso, RFID, temperatura).
 * El visitante AlertaMantenimiento recorre los sensores de cada estantería
 * para detectar fallos técnicos sin que la clase Estanteria deba contener
 * esa lógica de revisión.
 *
 * Proyecto: Estanterías Inteligentes INC
 */
public class SensorIoT {

    public enum Tipo { PESO, RFID, PICK_TO_LIGHT, TEMPERATURA }
    public enum Estado { OPERATIVO, FALLO, SIN_SEÑAL }

    private final String id;
    private final Tipo tipo;
    private Estado estado;
    private double ultimaLectura;

    /**
     * @param id           identificador único del sensor (ej. "SENS-A01-PESO-1")
     * @param tipo         tipo de sensor según el enum Tipo
     * @param estado       estado actual del sensor
     * @param ultimaLectura último valor registrado por el sensor
     */
    public SensorIoT(String id, Tipo tipo, Estado estado, double ultimaLectura) {
        this.id            = id;
        this.tipo          = tipo;
        this.estado        = estado;
        this.ultimaLectura = ultimaLectura;
    }

    public String  getId()            { return id; }
    public Tipo    getTipo()          { return tipo; }
    public Estado  getEstado()        { return estado; }
    public double  getUltimaLectura() { return ultimaLectura; }

    public boolean estaFallando() {
        return estado == Estado.FALLO || estado == Estado.SIN_SEÑAL;
    }

    public void actualizarEstado(Estado nuevoEstado) {
        this.estado = nuevoEstado;
    }

    @Override
    public String toString() {
        return String.format("SensorIoT{id='%s', tipo=%s, estado=%s, lectura=%.2f}",
                id, tipo, estado, ultimaLectura);
    }
}
