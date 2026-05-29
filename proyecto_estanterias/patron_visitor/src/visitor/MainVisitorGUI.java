package visitor;

import composite.Estanteria;
import composite.Producto;
import composite.SensorIoT;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Prototipo de interfaz gráfica para el demo del Patrón Visitor.
 * Permite ejecutar los visitantes de reporte y alerta desde botones.
 */
public class MainVisitorGUI extends JFrame {

    private final JTextArea outputArea = new JTextArea(24, 80);
    private final List<Estanteria> estanterias = new ArrayList<>();
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private final JPanel detailPanel = new JPanel(new BorderLayout(6,6));

    public MainVisitorGUI() {
        super("Estanterías Inteligentes - Demo Visitor");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        estanterias.add(construirEstanteriaA01());
        estanterias.add(construirEstanteriaB03());
        estanterias.add(construirEstanteriaC01());

        DefaultMutableTreeNode root = construirRaiz();
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                Object node = e.getPath().getLastPathComponent();
                actualizarDetalle(node);
            }
        });

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), new JScrollPane(outputArea));
        split.setResizeWeight(0.3);
        add(split, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnReporte = new JButton("Generar reporte de stock");
        btnReporte.addActionListener(e -> generarReporteStock());
        controls.add(btnReporte);

        JButton btnAlerta = new JButton("Ver alertas de mantenimiento");
        btnAlerta.addActionListener(e -> generarAlertaMantenimiento());
        controls.add(btnAlerta);

        JButton btnActualizar = new JButton("Actualizar vista");
        btnActualizar.addActionListener(e -> refrescarArbol());
        controls.add(btnActualizar);

        JButton btnAgregarSensor = new JButton("Agregar sensor demo");
        btnAgregarSensor.addActionListener(e -> {
            agregarSensorDemo();
            refrescarArbol();
        });
        controls.add(btnAgregarSensor);

        add(controls, BorderLayout.NORTH);

        detailPanel.setBorder(BorderFactory.createTitledBorder("Detalle"));
        detailPanel.add(new JLabel("Seleccione un sensor o producto en el árbol"), BorderLayout.CENTER);
        add(detailPanel, BorderLayout.EAST);

        mostrarDatosIniciales();
        pack();
        setLocationRelativeTo(null);
    }

    private Estanteria construirEstanteriaA01() {
        Estanteria est = new Estanteria("Estantería A-01", "Farmacia");
        est.agregarProducto(new Producto("Acetaminofén 500mg", "FAR-001", "LabCo", 2800, 320, 50));
        est.agregarProducto(new Producto("Ibuprofeno 400mg", "FAR-002", "LabCo", 3500, 18, 30));
        est.agregarProducto(new Producto("Omeprazol 20mg", "FAR-003", "FarmaPro", 4200, 95, 40));
        est.agregarSensor(new SensorIoT("SENS-A01-PESO-1", SensorIoT.Tipo.PESO, SensorIoT.Estado.OPERATIVO, 12.4));
        est.agregarSensor(new SensorIoT("SENS-A01-RFID-1", SensorIoT.Tipo.RFID, SensorIoT.Estado.FALLO, 0.0));
        est.agregarSensor(new SensorIoT("SENS-A01-LED-1", SensorIoT.Tipo.PICK_TO_LIGHT, SensorIoT.Estado.OPERATIVO, 1.0));
        return est;
    }

    private Estanteria construirEstanteriaB03() {
        Estanteria est = new Estanteria("Estantería B-03", "Autopartes");
        est.agregarProducto(new Producto("Filtro de aceite", "AUT-007", "AutoDist", 45000, 12, 20));
        est.agregarProducto(new Producto("Pastillas de freno", "AUT-012", "AutoDist", 85000, 8, 15));
        est.agregarSensor(new SensorIoT("SENS-B03-PESO-1", SensorIoT.Tipo.PESO, SensorIoT.Estado.OPERATIVO, 8.1));
        est.agregarSensor(new SensorIoT("SENS-B03-RFID-1", SensorIoT.Tipo.RFID, SensorIoT.Estado.SIN_SEÑAL, 0.0));
        est.agregarSensor(new SensorIoT("SENS-B03-TEMP-1", SensorIoT.Tipo.TEMPERATURA, SensorIoT.Estado.OPERATIVO, 22.3));
        return est;
    }

    private Estanteria construirEstanteriaC01() {
        Estanteria est = new Estanteria("Estantería C-01", "Bebidas");
        est.agregarProducto(new Producto("Agua 500ml x24", "BEB-001", "AguaPura", 28000, 200, 60));
        est.agregarProducto(new Producto("Jugo naranja 1L", "BEB-005", "FrutaViva", 5500, 45, 30));
        est.agregarSensor(new SensorIoT("SENS-C01-PESO-1", SensorIoT.Tipo.PESO, SensorIoT.Estado.OPERATIVO, 50.2));
        est.agregarSensor(new SensorIoT("SENS-C01-RFID-1", SensorIoT.Tipo.RFID, SensorIoT.Estado.OPERATIVO, 1.0));
        return est;
    }

    private void mostrarDatosIniciales() {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════════════════════════════════════════════\n");
        sb.append("               DEMO GRÁFICO — Patrón Visitor\n");
        sb.append("════════════════════════════════════════════════════════════════════════════════\n\n");
        for (Estanteria est : estanterias) {
            sb.append(est.getNombre()).append(":\n");
            sb.append(describirEstanteria(est));
            sb.append('\n');
        }
        sb.append("Pulse cualquiera de los botones para ejecutar un visitor sobre la jerarquía.\n");
        outputArea.setText(sb.toString());
    }

    private DefaultMutableTreeNode construirNodoEstanteria(Estanteria e) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(new NodoWrapper(e));
        DefaultMutableTreeNode sensores = new DefaultMutableTreeNode("Sensores");
        for (SensorIoT s : e.getSensores()) {
            sensores.add(new DefaultMutableTreeNode(s));
        }
        node.add(sensores);
        DefaultMutableTreeNode productos = new DefaultMutableTreeNode("Productos");
        for (Producto p : e.getProductos()) {
            productos.add(new DefaultMutableTreeNode(new NodoWrapper(p)));
        }
        node.add(productos);
        return node;
    }

    private DefaultMutableTreeNode construirRaiz() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Inventario Visitor");
        for (Estanteria estanteria : estanterias) {
            root.add(construirNodoEstanteria(estanteria));
        }
        return root;
    }

    private void refrescarArbol() {
        DefaultMutableTreeNode root = construirRaiz();
        treeModel.setRoot(root);
        treeModel.reload();
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private void agregarSensorDemo() {
        if (!estanterias.isEmpty()) {
            Estanteria est = estanterias.get(0);
            int nuevoIndice = est.getSensores().size() + 1;
            est.agregarSensor(new SensorIoT("SENS-A01-TEMP-" + nuevoIndice,
                    SensorIoT.Tipo.TEMPERATURA,
                    SensorIoT.Estado.OPERATIVO,
                    18.0 + nuevoIndice));
        }
    }

    private void actualizarDetalle(Object nodeObj) {
        detailPanel.removeAll();
        if (!(nodeObj instanceof DefaultMutableTreeNode)) {
            detailPanel.add(new JLabel("Seleccione un nodo"), BorderLayout.CENTER);
        } else {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeObj;
            Object user = node.getUserObject();
            JPanel panel = new JPanel(new BorderLayout(8, 8));
            panel.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

            if (user instanceof NodoWrapper) {
                NodoWrapper wrapper = (NodoWrapper) user;
                Object element = wrapper.getElemento();
                JLabel title = new JLabel("► " + wrapper.getTipo());
                title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
                title.setForeground(new Color(0, 102, 204));
                JPanel titlePanel = new JPanel(new BorderLayout());
                titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
                titlePanel.add(title, BorderLayout.WEST);
                panel.add(titlePanel, BorderLayout.NORTH);
                
                JPanel scrollableInfo = new JPanel(new GridLayout(0, 1, 6, 6));
                scrollableInfo.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

                if (element instanceof Estanteria) {
                    Estanteria est = (Estanteria) element;
                    addLabelEstilo(scrollableInfo, "Nombre:", est.getNombre());
                    addLabelEstilo(scrollableInfo, "Zona:", est.getZona());
                    addLabelEstilo(scrollableInfo, "Productos:", String.valueOf(est.getProductos().size()));
                    addLabelEstilo(scrollableInfo, "Sensores:", String.valueOf(est.getSensores().size()));

                    JPanel rename = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
                    rename.add(new JLabel("Nuevo nombre:"));
                    JTextField nameField = new JTextField(est.getNombre(), 15);
                    JButton renameBtn = new JButton("✓ Renombrar");
                    renameBtn.setBackground(new Color(76, 175, 80));
                    renameBtn.setForeground(Color.WHITE);
                    renameBtn.setBorderPainted(false);
                    renameBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            String nuevo = nameField.getText().trim();
                            if (!nuevo.isEmpty()) {
                                est.setNombre(nuevo);
                                treeModel.nodeChanged(node);
                                actualizarDetalle(node);
                            }
                        }
                    });
                    rename.add(nameField);
                    rename.add(renameBtn);
                    scrollableInfo.add(rename);

                    DefaultListModel<String> lista = new DefaultListModel<>();
                    for (Producto p : est.getProductos()) {
                        lista.addElement("  📦 " + p.getNombre());
                    }
                    for (SensorIoT s : est.getSensores()) {
                        lista.addElement("  📡 " + s.getId());
                    }
                    JList<String> list = new JList<>(lista);
                    list.setFont(list.getFont().deriveFont(11f));
                    list.setBorder(BorderFactory.createTitledBorder("Contenido"));
                    panel.add(new JScrollPane(scrollableInfo), BorderLayout.CENTER);
                    panel.add(new JScrollPane(list), BorderLayout.SOUTH);
                } else if (element instanceof Producto) {
                    Producto prod = (Producto) element;
                    addLabelEstilo(scrollableInfo, "Nombre:", prod.getNombre());
                    addLabelEstilo(scrollableInfo, "SKU:", prod.getSku());
                    addLabelEstilo(scrollableInfo, "Proveedor:", prod.getProveedor());
                    addLabelEstilo(scrollableInfo, "Stock:", String.valueOf(prod.getStock()));
                    addLabelEstilo(scrollableInfo, "Mínimo:", String.valueOf(prod.getStockMinimo()));
                    addLabelEstilo(scrollableInfo, "Precio:", "$" + prod.getPrecio());
                    
                    if (prod.tieneStockCritico()) {
                        JLabel alerta = new JLabel("⚠ STOCK CRÍTICO");
                        alerta.setForeground(new Color(244, 67, 54));
                        alerta.setFont(alerta.getFont().deriveFont(Font.BOLD));
                        scrollableInfo.add(alerta);
                    }

                    JPanel stockPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
                    stockPanel.add(new JLabel("Nuevo stock:"));
                    JSpinner spinner = new JSpinner(new SpinnerNumberModel(prod.getStock(), 0, Integer.MAX_VALUE, 1));
                    JButton updateBtn = new JButton("↻ Actualizar");
                    updateBtn.setBackground(new Color(33, 150, 243));
                    updateBtn.setForeground(Color.WHITE);
                    updateBtn.setBorderPainted(false);
                    updateBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            prod.actualizarStock((Integer) spinner.getValue());
                            mostrarDatosIniciales();
                            actualizarDetalle(node);
                        }
                    });
                    stockPanel.add(spinner);
                    stockPanel.add(updateBtn);
                    scrollableInfo.add(stockPanel);

                    JPanel rename = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
                    rename.add(new JLabel("Nuevo nombre:"));
                    JTextField nameField = new JTextField(prod.getNombre(), 15);
                    JButton renameBtn = new JButton("✓ Renombrar");
                    renameBtn.setBackground(new Color(76, 175, 80));
                    renameBtn.setForeground(Color.WHITE);
                    renameBtn.setBorderPainted(false);
                    renameBtn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            String nuevo = nameField.getText().trim();
                            if (!nuevo.isEmpty()) {
                                prod.setNombre(nuevo);
                                treeModel.nodeChanged(node);
                                actualizarDetalle(node);
                            }
                        }
                    });
                    rename.add(nameField);
                    rename.add(renameBtn);
                    scrollableInfo.add(rename);
                    
                    panel.add(new JScrollPane(scrollableInfo), BorderLayout.CENTER);
            } else if (user instanceof SensorIoT) {
                SensorIoT sensor = (SensorIoT) user;
                JLabel title = new JLabel("► Sensor IoT");
                title.setFont(title.getFont().deriveFont(Font.BOLD, 15f));
                title.setForeground(new Color(0, 102, 204));
                JPanel titlePanel = new JPanel(new BorderLayout());
                titlePanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(200, 200, 200)));
                titlePanel.add(title, BorderLayout.WEST);
                panel.add(titlePanel, BorderLayout.NORTH);

                JPanel sensorInfo = new JPanel(new GridLayout(0, 1, 6, 6));
                sensorInfo.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
                addLabelEstilo(sensorInfo, "ID:", sensor.getId());
                addLabelEstilo(sensorInfo, "Tipo:", sensor.getTipo().toString());
                addLabelEstilo(sensorInfo, "Estado:", sensor.getEstado().toString());
                addLabelEstilo(sensorInfo, "Lectura:", String.format("%.2f", sensor.getUltimaLectura()));

                JPanel estadoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
                estadoPanel.add(new JLabel("Cambiar estado:"));
                JComboBox<SensorIoT.Estado> estadoBox = new JComboBox<>(SensorIoT.Estado.values());
                estadoBox.setSelectedItem(sensor.getEstado());
                estadoBox.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        sensor.actualizarEstado((SensorIoT.Estado) estadoBox.getSelectedItem());
                    }
                });
                estadoPanel.add(estadoBox);
                sensorInfo.add(estadoPanel);
                panel.add(new JScrollPane(sensorInfo), BorderLayout.CENTER);
            } else {
                panel.add(new JLabel(user.toString()), BorderLayout.CENTER);
            }
            detailPanel.add(panel, BorderLayout.CENTER);
        }
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private String describirEstanteria(Estanteria estanteria) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  %s (zona: %s)\n", estanteria.getNombre(), estanteria.getZona()));
        sb.append("    Productos:\n");
        for (Producto p : estanteria.getProductos()) {
            sb.append(String.format("      - %s | SKU: %s | Stock: %d | Mínimo: %d | %s\n",
                    p.getNombre(), p.getSku(), p.getStock(), p.getStockMinimo(),
                    p.tieneStockCritico() ? "CRÍTICO" : "OK"));
        }
        sb.append("    Sensores:\n");
        for (SensorIoT s : estanteria.getSensores()) {
            sb.append(String.format("      - %s | Tipo: %s | Estado: %s | Lectura: %.1f\n",
                    s.getId(), s.getTipo(), s.getEstado(), s.getUltimaLectura()));
        }
        return sb.toString();
    }

    private void generarReporteStock() {
        ReporteStock visitor = new ReporteStock();
        for (Estanteria est : estanterias) {
            est.accept(visitor);
        }
        outputArea.setText(visitor.generarInforme());
    }

    private void generarAlertaMantenimiento() {
        AlertaMantenimiento visitor = new AlertaMantenimiento();
        for (Estanteria est : estanterias) {
            est.accept(visitor);
        }
        outputArea.setText(visitor.generarReporteMantenimiento());
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainVisitorGUI gui = new MainVisitorGUI();
            gui.setVisible(true);
        });
    }

    private static class NodoWrapper {
        private final Object elemento;

        public NodoWrapper(Object elemento) {
            this.elemento = elemento;
        }

        public Object getElemento() {
            return elemento;
        }

        public String getTipo() {
            return elemento.getClass().getSimpleName();
        }

        @Override
        public String toString() {
            if (elemento instanceof Estanteria) {
                return String.format("Estantería: %s", ((Estanteria) elemento).getNombre());
            } else if (elemento instanceof Producto) {
                return String.format("Producto: %s", ((Producto) elemento).getNombre());
            }
            return elemento.toString();
        }
    }
}
