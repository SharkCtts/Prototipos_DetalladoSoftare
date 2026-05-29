package composite;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Prototipo de interfaz gráfica para el demo del Patrón Composite.
 * Permite mostrar el árbol de inventario y agregar una bodega demo
 * para probar la escalabilidad del patrón sin modificar el código base.
 */
public class MainCompositeGUI extends JFrame {
    private final JTextArea outputArea = new JTextArea(14, 80);
    private final Empresa empresa;
    private final JTree tree;
    private final DefaultTreeModel treeModel;
    private final JPanel detailPanel = new JPanel(new BorderLayout(6,6));

    public MainCompositeGUI() {
        super("Estanterías Inteligentes - Demo Composite");
        this.empresa = construirInventarioInicial();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Árbol interactivo
        DefaultMutableTreeNode root = construirNodo(empresa);
        treeModel = new DefaultTreeModel(root);
        tree = new JTree(treeModel);
        tree.setRootVisible(true);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getPath();
                Object node = path.getLastPathComponent();
                actualizarPanelDetalle(node);
            }
        });

        outputArea.setEditable(false);
        outputArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                new JScrollPane(tree), new JScrollPane(outputArea));
        split.setResizeWeight(0.5);
        add(split, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton btnMostrar = new JButton("Actualizar vista");
        btnMostrar.addActionListener(e -> {
            refrescarArbol();
            mostrarInventario();
        });
        controls.add(btnMostrar);

        JButton btnAgregar = new JButton("Agregar bodega demo");
        btnAgregar.addActionListener(e -> { agregarBodegaDemo(); refrescarArbol(); mostrarInventario(); });
        controls.add(btnAgregar);

        add(controls, BorderLayout.NORTH);

        // Panel lateral con detalles y acciones sobre el nodo seleccionado
        detailPanel.setBorder(BorderFactory.createTitledBorder("Detalle"));
        detailPanel.add(new JLabel("Seleccione un nodo en el árbol"), BorderLayout.CENTER);
        add(detailPanel, BorderLayout.EAST);

        mostrarInventario();
        pack();
        setLocationRelativeTo(null);
    }

    private Empresa construirInventarioInicial() {
        Estanteria estA01 = new Estanteria("Estantería A-01", "Farmacia");
        estA01.agregarProducto(new Producto("Acetaminofén 500mg", "FAR-001", "LabCo", 2800, 320, 50));
        estA01.agregarProducto(new Producto("Ibuprofeno 400mg", "FAR-002", "LabCo", 3500, 18, 30));
        estA01.agregarProducto(new Producto("Omeprazol 20mg", "FAR-003", "FarmaPro", 4200, 95, 40));

        Estanteria estB03 = new Estanteria("Estantería B-03", "Autopartes");
        estB03.agregarProducto(new Producto("Filtro de aceite", "AUT-007", "AutoDist", 45000, 12, 20));
        estB03.agregarProducto(new Producto("Pastillas de freno", "AUT-012", "AutoDist", 85000, 8, 15));

        Estanteria estC01 = new Estanteria("Estantería C-01", "Bebidas");
        estC01.agregarProducto(new Producto("Agua 500ml x24", "BEB-001", "AguaPura", 28000, 200, 60));
        estC01.agregarProducto(new Producto("Jugo naranja 1L", "BEB-005", "FrutaViva", 5500, 45, 30));

        Bodega bodegaCentral = new Bodega("Bodega Central Cali", "Cali");
        bodegaCentral.agregarEstanteria(estA01);
        bodegaCentral.agregarEstanteria(estB03);

        Bodega darkStoreNorte = new Bodega("Dark Store Norte", "Cali");
        darkStoreNorte.agregarEstanteria(estC01);

        Empresa empresa = new Empresa("Estanterías Inteligentes INC", "900-123-456-7");
        empresa.agregarBodega(bodegaCentral);
        empresa.agregarBodega(darkStoreNorte);
        return empresa;
    }

    private void mostrarInventario() {
        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════════════════════════════════════════════\n");
        sb.append("              DEMO GRÁFICO — Patrón Composite\n");
        sb.append("════════════════════════════════════════════════════════════════════════════════\n\n");
        sb.append(construirTextoInventario(empresa, 0));
        sb.append("\n");
        sb.append(String.format("Total unidades: %d uds\n", empresa.getCantidadTotal()));
        sb.append(String.format("Total SKUs    : %d\n", empresa.contarProductos()));
        sb.append(String.format("Valor total   : $%.0f COP\n", empresa.getValorTotal()));
        sb.append("\nPulse 'Agregar bodega demo' para probar la escalabilidad del patrón.\n");
        outputArea.setText(sb.toString());
    }

    private String construirTextoInventario(InventarioComponent nodo, int nivel) {
        StringBuilder sb = new StringBuilder();
        if (nodo instanceof Empresa) {
            Empresa e = (Empresa) nodo;
            sb.append("  ".repeat(nivel))
              .append(String.format("[Empresa] %s | NIT: %s | Bodegas: %d | Productos: %d | Valor total: $%.0f COP\n",
                      e.getNombre(), e.getNit(), e.getHijos().size(), e.contarProductos(), e.getValorTotal()));
            for (InventarioComponent hijo : e.getHijos()) {
                sb.append(construirTextoInventario(hijo, nivel + 1));
            }
        } else if (nodo instanceof Bodega) {
            Bodega b = (Bodega) nodo;
            sb.append("  ".repeat(nivel))
              .append(String.format("[Bodega] %s | Ciudad: %s | Estanterías: %d | Productos: %d | Valor: $%.0f COP\n",
                      b.getNombre(), b.getCiudad(), b.getHijos().size(), b.contarProductos(), b.getValorTotal()));
            for (InventarioComponent hijo : b.getHijos()) {
                sb.append(construirTextoInventario(hijo, nivel + 1));
            }
        } else if (nodo instanceof Estanteria) {
            Estanteria est = (Estanteria) nodo;
            sb.append("  ".repeat(nivel))
              .append(String.format("[Estantería] %s | Zona: %s | SKUs: %d | Stock total: %d uds | Valor: $%.0f COP\n",
                      est.getNombre(), est.getZona(), est.contarProductos(), est.getCantidadTotal(), est.getValorTotal()));
            for (InventarioComponent hijo : est.getHijos()) {
                sb.append(construirTextoInventario(hijo, nivel + 1));
            }
        } else if (nodo instanceof Producto) {
            Producto p = (Producto) nodo;
            String alerta = p.tieneStockCritico() ? " ⚠ STOCK CRÍTICO" : "";
            sb.append("  ".repeat(nivel))
              .append(String.format("[Producto] %-28s | SKU: %-10s | Stock: %4d uds | Precio: $%.0f COP%s\n",
                      p.getNombre(), p.getSku(), p.getCantidadTotal(), p.getPrecio(), alerta));
        }
        return sb.toString();
    }

    // Construye el nodo raíz y la jerarquía para el JTree a partir del dominio
    private DefaultMutableTreeNode construirNodo(InventarioComponent nodo) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(new NodeWrapper(nodo));
        for (InventarioComponent hijo : nodo.getHijos()) {
            treeNode.add(construirNodo(hijo));
        }
        return treeNode;
    }

    private void refrescarArbol() {
        DefaultMutableTreeNode root = construirNodo(empresa);
        treeModel.setRoot(root);
        treeModel.reload();
        for (int i = 0; i < tree.getRowCount(); i++) tree.expandRow(i);
    }

    private void actualizarPanelDetalle(Object nodeObject) {
        detailPanel.removeAll();
        if (!(nodeObject instanceof DefaultMutableTreeNode)) {
            detailPanel.add(new JLabel("Seleccione un nodo"), BorderLayout.CENTER);
        } else {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodeObject;
            Object userObject = node.getUserObject();
            if (!(userObject instanceof NodeWrapper)) {
                detailPanel.add(new JLabel("Seleccione un nodo válido"), BorderLayout.CENTER);
            } else {
                NodeWrapper wrapper = (NodeWrapper) userObject;
                InventarioComponent encontrado = wrapper.getComponent();
                JPanel p = new JPanel(new BorderLayout(8, 8));
                JLabel titulo = new JLabel(wrapper.toString());
                titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 14f));
                p.add(titulo, BorderLayout.NORTH);

                JPanel info = new JPanel(new GridLayout(0, 1, 4, 4));
                info.add(new JLabel("Tipo: " + wrapper.getTipo()));
                info.add(new JLabel("Nombre actual: " + encontrado.getNombre()));

                if (encontrado instanceof Empresa) {
                    Empresa e = (Empresa) encontrado;
                    info.add(new JLabel("NIT: " + e.getNit()));
                    info.add(new JLabel("Bodegas: " + e.getHijos().size()));
                } else if (encontrado instanceof Bodega) {
                    Bodega b = (Bodega) encontrado;
                    info.add(new JLabel("Ciudad: " + b.getCiudad()));
                    info.add(new JLabel("Estanterías: " + b.getHijos().size()));
                } else if (encontrado instanceof Estanteria) {
                    Estanteria est = (Estanteria) encontrado;
                    info.add(new JLabel("Zona: " + est.getZona()));
                    info.add(new JLabel("Productos: " + est.getHijos().size()));
                } else if (encontrado instanceof Producto) {
                    Producto prod = (Producto) encontrado;
                    info.add(new JLabel("SKU: " + prod.getSku()));
                    info.add(new JLabel("Stock: " + prod.getStock()));
                    info.add(new JLabel("Precio: $" + prod.getPrecio()));
                    info.add(new JLabel("Stock mínimo: " + prod.getStockMinimo()));
                }

                JPanel renamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 4));
                renamePanel.add(new JLabel("Nuevo nombre:"));
                JTextField nombreField = new JTextField(encontrado.getNombre(), 18);
                JButton renameBtn = new JButton("Renombrar");
                renameBtn.addActionListener(ev -> {
                    String nuevoNombre = nombreField.getText().trim();
                    if (!nuevoNombre.isEmpty()) {
                        encontrado.setNombre(nuevoNombre);
                        refrescarArbol();
                        mostrarInventario();
                        actualizarPanelDetalle(nodeObject);
                    }
                });
                renamePanel.add(nombreField);
                renamePanel.add(renameBtn);
                info.add(renamePanel);

                p.add(info, BorderLayout.CENTER);

                DefaultListModel<String> listaHijos = new DefaultListModel<>();
                for (InventarioComponent hijo : encontrado.getHijos()) {
                    listaHijos.addElement(hijo.getNombre());
                }
                JList<String> hijosList = new JList<>(listaHijos);
                hijosList.setVisibleRowCount(6);
                hijosList.setBorder(BorderFactory.createTitledBorder("Contenido interno"));
                p.add(new JScrollPane(hijosList), BorderLayout.SOUTH);
                detailPanel.add(p, BorderLayout.CENTER);
            }
        }
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private static class NodeWrapper {
        private final InventarioComponent componente;

        public NodeWrapper(InventarioComponent componente) {
            this.componente = componente;
        }

        public InventarioComponent getComponent() {
            return componente;
        }

        public String getTipo() {
            return componente.getClass().getSimpleName();
        }

        @Override
        public String toString() {
            return String.format("%s: %s", getTipo(), componente.getNombre());
        }
    }

    private void agregarBodegaDemo() {
        Bodega nuevaBodega = new Bodega("Centro de Distribución Sur", "Bogotá");
        Estanteria estNueva = new Estanteria("Estantería D-01", "Medicamentos");
        estNueva.agregarProducto(new Producto("Ampicilina 500mg", "MED-001", "FarmaBog", 8500, 150, 40));
        estNueva.agregarProducto(new Producto("Vitamina C 500mg", "MED-002", "FarmaBog", 6500, 70, 30));
        nuevaBodega.agregarEstanteria(estNueva);
        empresa.agregarBodega(nuevaBodega);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            MainCompositeGUI gui = new MainCompositeGUI();
            gui.setVisible(true);
        });
    }
}
