/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class VentanaM extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(VentanaM.class.getName());

    // Componentes para la vista de empleados (tabla)
    private JTable tablaEmpleados;
    private DefaultTableModel modeloTablaEmpleados;
    private JScrollPane scrollEmpleados;
    private JPanel panelEmpleados;

    // Componentes para la vista de nóminas
    private JTable tablaNominas;
    private DefaultTableModel modeloTablaNominas;
    private JScrollPane scrollNominas;
    private JPanel panelNominas;

    // Contenedor central con CardLayout
    private JPanel panelCentral;
    private CardLayout cardLayout;

    public VentanaM() {
        initComponents(); // Crea los componentes del diseñador (jPanel1, jPanel2, botones, etc.)

        // --- Reorganizar el layout del contentPane ---
        getContentPane().removeAll();
        getContentPane().setLayout(new BorderLayout(10, 10));
        
        // Panel izquierdo (ocupa toda la altura)
        getContentPane().add(jPanel1, BorderLayout.WEST);

        // Panel izquierdo: asegurar que se expanda verticalmente
        jPanel1.setPreferredSize(new Dimension(jPanel1.getPreferredSize().width, Integer.MAX_VALUE));
        getContentPane().add(jPanel1, BorderLayout.WEST);

        // Panel superior: ocupará todo el ancho restante
        getContentPane().add(jPanel2, BorderLayout.NORTH);
        
        configurarVistaEmpleados();
        configurarVistaNominas();
        
        panelCentral = new JPanel();
        cardLayout = new CardLayout();
        panelCentral.setLayout(cardLayout);
        panelCentral.add(panelEmpleados, "empleados");
        panelCentral.add(panelNominas, "nominas");
        
        getContentPane().add(panelCentral, BorderLayout.CENTER);
        
        cargarEmpleadosEnGUI();
        cargarNominasEnGUI();
        
        cardLayout.show(panelCentral, "empleados");
        
        setMinimumSize(new Dimension(900, 500));
        pack();

        // --- Añadir listeners para los botones de datos ---
        jButton4.addActionListener(evt -> {
        main.cargarDatosGUI();
        cargarEmpleadosEnGUI();
        cargarNominasEnGUI();
        // Mantener la vista actual
        if (panelNominas.isShowing()) {
            cardLayout.show(panelCentral, "nominas");
        } else {
            cardLayout.show(panelCentral, "empleados");
        }
        JOptionPane.showMessageDialog(this, "Datos cargados correctamente.", "Cargar", JOptionPane.INFORMATION_MESSAGE);
    });
    
    jButton5.addActionListener(evt -> {
        main.guardarDatosGUI();
        JOptionPane.showMessageDialog(this, "Datos guardados correctamente.", "Guardar", JOptionPane.INFORMATION_MESSAGE);
    });
}

    // Configura la tabla de empleados (estilo similar a la de nóminas)
    private void configurarVistaEmpleados() {
        modeloTablaEmpleados = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4; // columna de acciones
            }
        };
        modeloTablaEmpleados.addColumn("Nombre");
        modeloTablaEmpleados.addColumn("ID");
        modeloTablaEmpleados.addColumn("Tipo");
        modeloTablaEmpleados.addColumn("Salario/Tarifa");
        modeloTablaEmpleados.addColumn("Acciones");

        tablaEmpleados = new JTable(modeloTablaEmpleados);
        tablaEmpleados.setBackground(Color.WHITE);
        tablaEmpleados.setRowHeight(30);
        tablaEmpleados.setShowGrid(true);
        tablaEmpleados.setGridColor(Color.LIGHT_GRAY);

        // Cabecera azul con texto blanco
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(0, 102, 204));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        for (int i = 0; i < tablaEmpleados.getColumnCount(); i++) {
            tablaEmpleados.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JTableHeader header = tablaEmpleados.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        tablaEmpleados.getColumnModel().getColumn(4).setCellRenderer(new BotonesEmpleadosRenderer());
        tablaEmpleados.getColumnModel().getColumn(4).setCellEditor(new BotonesEmpleadosEditor());
        tablaEmpleados.getTableHeader().setReorderingAllowed(false);

        tablaEmpleados.getColumnModel().getColumn(0).setPreferredWidth(150);
        tablaEmpleados.getColumnModel().getColumn(1).setPreferredWidth(50);
        tablaEmpleados.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaEmpleados.getColumnModel().getColumn(3).setPreferredWidth(100);
        tablaEmpleados.getColumnModel().getColumn(4).setPreferredWidth(130);

        scrollEmpleados = new JScrollPane(tablaEmpleados);
        panelEmpleados = new JPanel(new BorderLayout());
        panelEmpleados.add(scrollEmpleados, BorderLayout.CENTER);
    }

    // Renderer para botones de empleados
    private class BotonesEmpleadosRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");

        public BotonesEmpleadosRenderer() {
            setLayout(new java.awt.GridLayout(1, 2, 5, 0));
            setBorder(new EmptyBorder(2, 2, 2, 2));
            Dimension btnSize = new Dimension(60, 25);
            btnEditar.setPreferredSize(btnSize);
            btnEditar.setBackground(new Color(100, 150, 255));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnEliminar.setPreferredSize(btnSize);
            btnEliminar.setBackground(Color.RED);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            add(btnEditar);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) setBackground(table.getSelectionBackground());
            else setBackground(table.getBackground());
            return this;
        }
    }

    // Editor para botones de empleados
    private class BotonesEmpleadosEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel = new JPanel(new java.awt.GridLayout(1, 2, 5, 0));
        private final JButton btnEditar = new JButton("Editar");
        private final JButton btnEliminar = new JButton("Eliminar");
        private int row;

        public BotonesEmpleadosEditor() {
            panel.setBorder(new EmptyBorder(2, 2, 2, 2));
            Dimension btnSize = new Dimension(60, 25);
            btnEditar.setPreferredSize(btnSize);
            btnEditar.setBackground(new Color(100, 150, 255));
            btnEditar.setForeground(Color.WHITE);
            btnEditar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnEliminar.setPreferredSize(btnSize);
            btnEliminar.setBackground(Color.RED);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnEditar.addActionListener(e -> {
                int id = obtenerIdEmpleado(row);
                editarEmpleadoGUI(id);
                fireEditingStopped();
            });
            btnEliminar.addActionListener(e -> {
                int id = obtenerIdEmpleado(row);
                eliminarEmpleadoGUI(id);
                fireEditingStopped();
            });
            panel.add(btnEditar);
            panel.add(btnEliminar);
        }

        private int obtenerIdEmpleado(int row) {
            return Integer.parseInt(modeloTablaEmpleados.getValueAt(row, 1).toString());
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    // Carga los empleados en la tabla
    private void cargarEmpleadosEnGUI() {
        modeloTablaEmpleados.setRowCount(0);
        List<Empleado> empleados = main.getEmpleados();
        for (Empleado emp : empleados) {
            String nombre = emp.getNombre();
            String id = String.valueOf(emp.getId());
            String tipo = (emp instanceof EmpleadoAsalariado) ? "Asalariado" : "Por Horas";
            String valor = "";
            if (emp instanceof EmpleadoAsalariado)
                valor = String.valueOf(((EmpleadoAsalariado) emp).getSalarioMensual());
            else
                valor = String.valueOf(((EmpleadoPorHoras) emp).getTarifaHora());
            modeloTablaEmpleados.addRow(new Object[]{nombre, id, tipo, valor, ""});
        }
    }

    // Configura la vista de nóminas
    private void configurarVistaNominas() {
        modeloTablaNominas = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };
        modeloTablaNominas.addColumn("Fecha");
        modeloTablaNominas.addColumn("Empleado");
        modeloTablaNominas.addColumn("Total");
        modeloTablaNominas.addColumn("Acciones");

        tablaNominas = new JTable(modeloTablaNominas);
        tablaNominas.setBackground(Color.WHITE);
        tablaNominas.setRowHeight(30);
        tablaNominas.setShowGrid(true);
        tablaNominas.setGridColor(Color.LIGHT_GRAY);

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                c.setBackground(new Color(0, 102, 204));
                c.setForeground(Color.WHITE);
                c.setFont(new Font("Segoe UI", Font.BOLD, 13));
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        for (int i = 0; i < tablaNominas.getColumnCount(); i++) {
            tablaNominas.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JTableHeader header = tablaNominas.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        tablaNominas.getColumnModel().getColumn(3).setCellRenderer(new BotonesNominasRenderer());
        tablaNominas.getColumnModel().getColumn(3).setCellEditor(new BotonesNominasEditor());
        tablaNominas.getTableHeader().setReorderingAllowed(false);

        tablaNominas.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaNominas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaNominas.getColumnModel().getColumn(2).setPreferredWidth(80);
        tablaNominas.getColumnModel().getColumn(3).setPreferredWidth(130);

        scrollNominas = new JScrollPane(tablaNominas);
        panelNominas = new JPanel(new BorderLayout());
        panelNominas.add(scrollNominas, BorderLayout.CENTER);
    }

    // Renderer para botones de nóminas
    private class BotonesNominasRenderer extends JPanel implements TableCellRenderer {
        private final JButton btnDetalle = new JButton("Ver");
        private final JButton btnEliminar = new JButton("Eliminar");

        public BotonesNominasRenderer() {
            setLayout(new java.awt.GridLayout(1, 2, 5, 0));
            setBorder(new EmptyBorder(2, 2, 2, 2));
            Dimension btnSize = new Dimension(60, 25);
            btnDetalle.setPreferredSize(btnSize);
            btnDetalle.setBackground(new Color(100, 150, 255));
            btnDetalle.setForeground(Color.WHITE);
            btnDetalle.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnEliminar.setPreferredSize(btnSize);
            btnEliminar.setBackground(Color.RED);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            add(btnDetalle);
            add(btnEliminar);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) setBackground(table.getSelectionBackground());
            else setBackground(table.getBackground());
            return this;
        }
    }

    // Editor para botones de nóminas
    private class BotonesNominasEditor extends AbstractCellEditor implements javax.swing.table.TableCellEditor {
        private final JPanel panel = new JPanel(new java.awt.GridLayout(1, 2, 5, 0));
        private final JButton btnDetalle = new JButton("Ver");
        private final JButton btnEliminar = new JButton("Eliminar");
        private int row;

        public BotonesNominasEditor() {
            panel.setBorder(new EmptyBorder(2, 2, 2, 2));
            Dimension btnSize = new Dimension(60, 25);
            btnDetalle.setPreferredSize(btnSize);
            btnDetalle.setBackground(new Color(100, 150, 255));
            btnDetalle.setForeground(Color.WHITE);
            btnDetalle.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnEliminar.setPreferredSize(btnSize);
            btnEliminar.setBackground(Color.RED);
            btnEliminar.setForeground(Color.WHITE);
            btnEliminar.setFont(new Font("Segoe UI", Font.BOLD, 11));
            btnDetalle.addActionListener(e -> {
                verDetalleNomina(row);
                fireEditingStopped();
            });
            btnEliminar.addActionListener(e -> {
                eliminarNomina(row);
                fireEditingStopped();
            });
            panel.add(btnDetalle);
            panel.add(btnEliminar);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.row = row;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }

    private void cargarNominasEnGUI() {
        modeloTablaNominas.setRowCount(0);
        List<Nomina> nominas = main.getRegistroNominas().getNominas();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Nomina n : nominas) {
            String fecha = sdf.format(n.get_Fecha());
            String empleado = n.get_Empleado().getNombre() + " (ID " + n.get_Empleado().getId() + ")";
            String total = String.format("%.2f", n.get_Total());
            modeloTablaNominas.addRow(new Object[]{fecha, empleado, total, ""});
        }
    }

    private void verDetalleNomina(int row) {
        List<Nomina> nominas = main.getRegistroNominas().getNominas();
        if (row < 0 || row >= nominas.size()) return;
        Nomina n = nominas.get(row);
        String detalles = n.toString();
        JTextArea textArea = new JTextArea(detalles);
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(500, 300));
        JOptionPane.showMessageDialog(this, scroll, "Detalles de Nómina", JOptionPane.INFORMATION_MESSAGE);
    }

    private void eliminarNomina(int row) {
        List<Nomina> nominas = main.getRegistroNominas().getNominas();
        if (row < 0 || row >= nominas.size()) return;
        Nomina n = nominas.get(row);
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar nómina de " + n.get_Empleado().getNombre() + " del " + n.get_Fecha() + "?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean ok = main.eliminarNominaGUI(row);
            if (ok) {
                cargarNominasEnGUI();
                JOptionPane.showMessageDialog(this, "Nómina eliminada.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Diálogo para agregar empleado
    private void mostrarDialogoAgregarEmpleado() {
        String[] opciones = {"Asalariado", "Por Horas"};
        int tipo = JOptionPane.showOptionDialog(this, "Seleccione el tipo de empleado:", "Nuevo Empleado",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[0]);
        if (tipo == JOptionPane.CLOSED_OPTION) return;
        boolean esAsalariado = (tipo == 0);

        // Validar nombre (no vacío, no solo números)
        String nombre = "";
        while (true) {
            nombre = JOptionPane.showInputDialog(this, "Nombre completo:", "Agregar Empleado", JOptionPane.QUESTION_MESSAGE);
            if (nombre == null) return;
            nombre = nombre.trim();
            if (nombre.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (nombre.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El nombre no puede consistir solo en números.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }

        // Validar puesto (no vacío, no solo números)
        String puesto = "";
        while (true) {
            puesto = JOptionPane.showInputDialog(this, "Puesto:", "Agregar Empleado", JOptionPane.QUESTION_MESSAGE);
            if (puesto == null) return;
            puesto = puesto.trim();
            if (puesto.isEmpty()) {
                JOptionPane.showMessageDialog(this, "El puesto no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            if (puesto.matches("\\d+")) {
                JOptionPane.showMessageDialog(this, "El puesto no puede consistir solo en números.", "Error", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }

        // Validar valor monetario (positivo)
        double valor = 0;
        String mensajeValor = esAsalariado ? "Salario mensual:" : "Tarifa por hora:";
        while (true) {
            String input = JOptionPane.showInputDialog(this, mensajeValor, "Agregar Empleado", JOptionPane.QUESTION_MESSAGE);
            if (input == null) return;
            try {
                valor = Double.parseDouble(input.trim());
                if (valor <= 0) {
                    JOptionPane.showMessageDialog(this, "El valor debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Entrada inválida. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        try {
            if (esAsalariado)
                main.agregarEmpleadoAsalariadoGUI(nombre, puesto, valor);
            else
                main.agregarEmpleadoPorHorasGUI(nombre, puesto, valor);
            cargarEmpleadosEnGUI();
            JOptionPane.showMessageDialog(this, "Empleado agregado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al agregar empleado: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarEmpleadoGUI(int id) {
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp == null) return;

        String nombreActual = emp.getNombre();
        String puestoActual = emp.getPuesto();
        String valorActual = "";
        if (emp instanceof EmpleadoAsalariado)
            valorActual = String.valueOf(((EmpleadoAsalariado) emp).getSalarioMensual());
        else
            valorActual = String.valueOf(((EmpleadoPorHoras) emp).getTarifaHora());

        // Panel de edición
        JPanel panel = new JPanel(new java.awt.GridLayout(0, 2, 5, 5));
        panel.add(new JLabel("Nombre:"));
        JTextField txtNombre = new JTextField(nombreActual, 15);
        panel.add(txtNombre);
        panel.add(new JLabel("Puesto:"));
        JTextField txtPuesto = new JTextField(puestoActual, 15);
        panel.add(txtPuesto);
        String labelValor = (emp instanceof EmpleadoAsalariado) ? "Salario mensual:" : "Tarifa por hora:";
        panel.add(new JLabel(labelValor));
        JTextField txtValor = new JTextField(valorActual, 15);
        panel.add(txtValor);

        int option = JOptionPane.showConfirmDialog(this, panel, "Editar empleado ID " + id,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (option != JOptionPane.OK_OPTION) return;

        String nuevoNombre = txtNombre.getText().trim();
        String nuevoPuesto = txtPuesto.getText().trim();
        String nuevoValorStr = txtValor.getText().trim();

        // Validaciones
        if (!nuevoNombre.isEmpty() && nuevoNombre.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El nombre no puede consistir solo en números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!nuevoPuesto.isEmpty() && nuevoPuesto.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "El puesto no puede consistir solo en números.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double nuevoValor = 0;
        if (!nuevoValorStr.isEmpty()) {
            try {
                nuevoValor = Double.parseDouble(nuevoValorStr);
                if (nuevoValor <= 0) {
                    JOptionPane.showMessageDialog(this, "El valor debe ser un número positivo.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Valor inválido. Debe ser un número.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Aplicar cambios (si el campo está vacío se conserva el original)
        boolean exito;
        if (emp instanceof EmpleadoAsalariado) {
            exito = main.editarEmpleadoGUI(id, 
                    nuevoNombre.isEmpty() ? null : nuevoNombre,
                    nuevoPuesto.isEmpty() ? null : nuevoPuesto,
                    nuevoValorStr.isEmpty() ? null : nuevoValor,
                    null);
        } else {
            exito = main.editarEmpleadoGUI(id,
                    nuevoNombre.isEmpty() ? null : nuevoNombre,
                    nuevoPuesto.isEmpty() ? null : nuevoPuesto,
                    null,
                    nuevoValorStr.isEmpty() ? null : nuevoValor);
        }

        if (exito) {
            cargarEmpleadosEnGUI();
            JOptionPane.showMessageDialog(this, "Empleado actualizado correctamente.");
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo actualizar el empleado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarEmpleadoGUI(int id) {
        Empleado emp = buscarEmpleadoPorID(id);
        if (emp == null) return;
        int confirm = JOptionPane.showConfirmDialog(this,
                "¿Eliminar a " + emp.getNombre() + " (ID " + id + ")?",
                "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean exito = main.eliminarEmpleadoGUI(id);
            if (exito) {
                cargarEmpleadosEnGUI();
                JOptionPane.showMessageDialog(this, "Empleado eliminado.");
            } else {
                JOptionPane.showMessageDialog(this, "No se pudo eliminar.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Empleado buscarEmpleadoPorID(int id) {
        for (Empleado e : main.getEmpleados()) if (e.getId() == id) return e;
        return null;
    }

    // Métodos para cambiar de vista (llamados desde los botones)
    private void mostrarVistaEmpleados() {
        if (cardLayout != null) {
            cardLayout.show(panelCentral, "empleados");
            cargarEmpleadosEnGUI();
        }
    }

    private void mostrarVistaNominas() {
        if (cardLayout != null) {
            cargarNominasEnGUI();
            cardLayout.show(panelCentral, "nominas");
        }
    }


    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión de Nóminas");
        setBackground(new java.awt.Color(204, 204, 204));
        setLocation(new java.awt.Point(300, 200));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jButton1.setText("Lista de empleados");
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jButton1.addActionListener(evt -> mostrarVistaEmpleados());

        jLabel1.setText("Nominas");

        jButton2.setText("Procesar nomina");
        jButton2.addActionListener(evt -> {
            main.procesarNominasGUI(this);
            mostrarVistaNominas();
        });

        jButton3.setText("Lista de nominas ");
        jButton3.addActionListener(evt -> mostrarVistaNominas());

        jLabel2.setText("Datos");

        jButton4.setText("Cargar Datos");

        jButton5.setText("Guardar Datos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
                            .addComponent(jSeparator1)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(63, 63, 63))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(18, 18, 18)
                .addComponent(jButton5)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jButton6.setBackground(new java.awt.Color(0, 102, 204));
        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Agregar empleado");
        jButton6.addActionListener(this::jButton6ActionPerformed);

        jTextField1.setBackground(new java.awt.Color(250, 250, 250));
        jTextField1.setMaximumSize(new java.awt.Dimension(64, 22));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(55, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jButton6)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16))
        );

        panelCentral = new JPanel();
        cardLayout = new CardLayout();
        panelCentral.setLayout(cardLayout);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

     
    
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        mostrarDialogoAgregarEmpleado();        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new VentanaM().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
